package com.matticry.microservicesaccount.service;

import com.matticry.microservicesaccount.api.ClientApi;
import com.matticry.microservicesaccount.dto.*;
import com.matticry.microservicesaccount.models.Account;
import com.matticry.microservicesaccount.models.Movement;
import com.matticry.microservicesaccount.repository.AccountRepository;
import com.matticry.microservicesaccount.repository.MovementRepository;
import com.matticry.microservicesaccount.service.interfaces.MovementServiceInterface;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class MovementServiceImpl implements MovementServiceInterface {

    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final ClientApi clientApi;

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha inicial no puede ser posterior a la fecha final"
            );
        }

        if (endDate.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha final no puede ser futura"
            );
        }
    }

    public Single<List<MovementReportDTO>> getMovementForDateAndClient(LocalDate startDate, LocalDate endDate, String name) {
        validateDateRange(startDate, endDate);
        return clientApi.getClientByName(name)
                .flatMap(clientDTO -> Single.fromCallable(() -> {
                            List<Movement> movements = movementRepository.findMovementsByDateRangeAndClientId(
                                    startDate,
                                    endDate,
                                    Math.toIntExact(clientDTO.getId())
                            );
                            return convertToDto(movements, name);
                        })
                        .subscribeOn(Schedulers.io())
                        .doOnError(error -> log.error("Error getting movements: ", error))
                        .doOnSuccess(result -> log.info("Successfully retrieved {} movements", result.size())));
    }

    private List<MovementReportDTO> convertToDto(List<Movement> movements, String name) {
        return movements.stream()
                .map(movement -> {
                    Account account = accountRepository.findById(movement.getAccountId())
                            .orElseThrow(() -> new RuntimeException(
                                    String.format("Account not found for movement ID: %d", movement.getIdMov())
                            ));

                    return MovementReportDTO.builder()
                            .date(movement.getDateMov().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                            .client(name)
                            .numberAccount(account.getNumberAccount())
                            .typeAccount(account.getTypeAccount().toString())
                            .initialBalanceAcc(account.getInitialBalanceAcc())
                            .statusAccount(account.getStatusAccount())
                            .value(movement.getValueMov())
                            .balanceMov(movement.getBalanceMov())
                            .transactionType(movement.getTransactionTypeMov().toString())
                            .build();
                })
                .collect(Collectors.toList());
    }


    @Override
    public Single<MovementResponseDTO> createMovement(MovementRequestDTO requestDTO)
    {
        return Single.fromCallable(() -> accountRepository.findById(Long.valueOf(requestDTO.getAccountId())))
                .subscribeOn(Schedulers.io())
                .flatMap(optionalAccount -> {
                    Account account = optionalAccount.orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada"));

                    return Single.fromCallable(() ->
                                    movementRepository.findTopByAccountIdOrderByDateMovDesc(Math.toIntExact(account.getIdAccount())))
                            .subscribeOn(Schedulers.io())
                            .map(optionalLastMovement -> {
                                BigDecimal currentBalance = optionalLastMovement
                                        .map(Movement::getBalanceMov)
                                        .orElse(account.getInitialBalanceAcc());

                                return processMovement(requestDTO, account, currentBalance);
                            });
                })
                .flatMap(movement -> Single.fromCallable(() -> movementRepository.save(movement))
                        .subscribeOn(Schedulers.io())
                            .map(savedMovement -> {
                                MovementResponseDTO responseDTO = modelMapper.map(savedMovement, MovementResponseDTO.class);

                                Account account = accountRepository.findById(savedMovement.getAccountId())
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada"));

                                AccountResponseDTO accountResponseDTO = modelMapper.map(account, AccountResponseDTO.class);
                                ClientDTO clientDTO = new ClientDTO();
                                clientDTO.setId((long) account.getClientId());
                                accountResponseDTO.setClient(clientDTO);

                                responseDTO.setAccount(accountResponseDTO);
                                return responseDTO;
                            }
                        ));
    }

    private Movement processMovement(MovementRequestDTO requestDTO, Account account, BigDecimal currentBalance)
    {
        Movement movement = new Movement();
        movement.setAccountId((account.getIdAccount()));
        movement.setDateMov(LocalDateTime.now());
        movement.setTransactionTypeMov(requestDTO.getTransactionType());
        movement.setValueMov(requestDTO.getValue());

        // Calcular nuevo saldo según tipo de transacción
        BigDecimal newBalance = switch (requestDTO.getTransactionType()) {
            case deposito -> currentBalance.add(requestDTO.getValue());
            case retiro -> {
                if (currentBalance.compareTo(requestDTO.getValue()) < 0) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Saldo insuficiente");
                }
                yield currentBalance.subtract(requestDTO.getValue());
            }
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tipo de transacción no soportada");
        };

        movement.setBalanceMov(newBalance);
        return movement;
    }

}
