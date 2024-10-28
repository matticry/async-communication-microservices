package com.matticry.microservicesaccount.service;

import com.matticry.microservicesaccount.dto.AccountRequestDTO;
import com.matticry.microservicesaccount.dto.AccountResponseDTO;
import com.matticry.microservicesaccount.dto.ClientDTO;
import com.matticry.microservicesaccount.dto.MovementResponseDTO;
import com.matticry.microservicesaccount.models.Account;
import com.matticry.microservicesaccount.models.Movement;
import com.matticry.microservicesaccount.repository.AccountRepository;
import com.matticry.microservicesaccount.repository.MovementRepository;
import com.matticry.microservicesaccount.service.interfaces.AccountServiceInterface;
import com.matticry.microservicesaccount.service.interfaces.MovementServiceInterface;
import com.matticry.microservicesaccount.util.AccountNumberGenerator;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountServiceInterface {

    private final AccountRepository accountRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;
    private final MovementRepository movementRepository;

    @Value("${microservice.clients.url}")
    private String clientServiceUrl;

    @Override
    public Single<AccountResponseDTO> createAccount(AccountRequestDTO accountRequestDTO) {
        return Single.fromCallable(() -> verifyClient(accountRequestDTO.getClientId()))
                .subscribeOn(Schedulers.io())
                .flatMap(clientDTO ->
                        Single.fromCallable(() ->
                                        accountRepository.existsByClientIdAndTypeAccount(
                                                accountRequestDTO.getClientId(),
                                                accountRequestDTO.getTypeAccount()
                                        )
                                )
                                .subscribeOn(Schedulers.io())
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Single.error(new ResponseStatusException(
                                                HttpStatus.BAD_REQUEST,
                                                "El cliente ya tiene una cuenta " + accountRequestDTO.getTypeAccount() + " asociada"
                                        ));
                                    }
                                    Account account = new Account();
                                    account.setNumberAccount(AccountNumberGenerator.generateAccountNumber());
                                    account.setTypeAccount(accountRequestDTO.getTypeAccount());
                                    account.setDateAccount(LocalDateTime.now());
                                    account.setInitialBalanceAcc(accountRequestDTO.getInitialBalanceAcc());
                                    account.setStatusAccount('A');
                                    account.setClientId(accountRequestDTO.getClientId());

                                    return Single.fromCallable(() -> accountRepository.save(account))
                                            .subscribeOn(Schedulers.io())
                                            .map(savedAccount -> {
                                                AccountResponseDTO responseDTO = modelMapper.map(savedAccount, AccountResponseDTO.class);
                                                responseDTO.setClient(clientDTO);
                                                return responseDTO;
                                            });
                                })
                );
    }


    private ClientDTO verifyClient(Long clientId) {
        try {
            String url = clientServiceUrl + "/clients/" + clientId;

            ResponseEntity<ClientDTO> response = restTemplate.getForEntity(
                    url,
                    ClientDTO.class
            );

            if (response.getBody() == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente no encontrado con ID: " + clientId
                );
            }

            return response.getBody();

        } catch (RestClientException e) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Error al comunicarse con el servicio de clientes: " + e.getMessage()
            );
        }
    }
    public Single<List<AccountResponseDTO>> getAccountsByClientId(Integer clientId) {
        return Single.fromCallable(() -> {
                    List<Account> accounts = accountRepository.findByClientId(clientId);
                    return accounts.stream()
                            .map(account -> {
                                AccountResponseDTO dto = modelMapper.map(account, AccountResponseDTO.class);
                                // Obtener movimientos de cada cuenta
                                List<Movement> movements = movementRepository.findByAccountId(Math.toIntExact(account.getIdAccount()));
                                List<MovementResponseDTO> movementDTOs = movements.stream()
                                        .map(movement -> modelMapper.map(movement, MovementResponseDTO.class))
                                        .collect(Collectors.toList());
                                dto.setMovements(movementDTOs);
                                return dto;
                            })
                            .collect(Collectors.toList());
                }).subscribeOn(Schedulers.io())
                .doOnError(error -> log.error("Error getting accounts for client {}: ", clientId, error));
    }


}