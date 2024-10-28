package com.matticry.microservicesproject.service;

import com.matticry.microservicesproject.api.ApiAccount;
import com.matticry.microservicesproject.dto.AccountDTO;
import com.matticry.microservicesproject.dto.ClientDTO;
import com.matticry.microservicesproject.exception.ClientAlreadyExistsException;
import com.matticry.microservicesproject.exception.ClientNotFoundException;
import com.matticry.microservicesproject.exception.InvalidClientDataException;
import com.matticry.microservicesproject.model.Client;
import com.matticry.microservicesproject.repository.ClientRepository;
import com.matticry.microservicesproject.service.interfaces.ClientServiceInterface;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService implements ClientServiceInterface {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ApiAccount accountApi;


    private boolean isValidEcuadorianDNI(String dni) {
        if (dni == null || dni.length() != 10 || !dni.matches("\\d{10}")) {
            return false;
        }

        try {
            int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
            int total = 0;
            int verificador = Character.getNumericValue(dni.charAt(9));

            for (int i = 0; i < 9; i++) {
                int valor = Character.getNumericValue(dni.charAt(i)) * coeficientes[i];
                total += valor > 9 ? valor - 9 : valor;
            }

            int digitoVerificador = total % 10 != 0 ? 10 - (total % 10) : 0;

            return digitoVerificador == verificador;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public ClientDTO createClient(ClientDTO clientDTO) {
        log.info("Creating client {}", clientDTO);
        if (!isValidEcuadorianDNI(clientDTO.getDni())) {
            throw new InvalidClientDataException("The ID provided is not a valid Ecuadorian ID");
        }
        Optional<Client> existingClient = clientRepository.findByDni(clientDTO.getDni());
        if (existingClient.isPresent()) {
            throw new ClientAlreadyExistsException("Client already exists");
        }

        Client client = modelMapper.map(clientDTO, Client.class);
        client.setPasswordClient(bCryptPasswordEncoder.encode(clientDTO.getPasswordClient()));
        return modelMapper.map(clientRepository.save(client), ClientDTO.class);
    }

    @Override
    public ClientDTO updateClient(ClientDTO clientDTO, Long id) {
        log.info("Updating client with ID: {} with data: {}", id, clientDTO);
        validateClientData(clientDTO);  // Agregar validaciones
        Client client = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("Client not found with id: " + id));
        modelMapper.map(clientDTO, client);
        client.setId(id);
        if (!isValidEcuadorianDNI(clientDTO.getDni())) {
            throw new InvalidClientDataException("The ID provided is not a valid Ecuadorian ID");
        }
        // Solo actualizar la contraseña si se proporciona una nueva
        if (clientDTO.getPasswordClient() != null && !clientDTO.getPasswordClient().isEmpty()) {
            client.setPasswordClient(bCryptPasswordEncoder.encode(clientDTO.getPasswordClient()));
        }
        Client updatedClient = clientRepository.save(client);
        return modelMapper.map(updatedClient, ClientDTO.class);
    }

    @Override
    public ClientDTO getClientById(Long id) {
        log.info("Fetching client with ID: {}", id);
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new ClientNotFoundException("Client not found");
        }
        return modelMapper.map(client.get(), ClientDTO.class);
    }

    @Override
    public ClientDTO getClientByName(String name) {
        log.info("Fetching client with name: {}", name);
        Optional<Client> client = clientRepository.findByName(name);
        if (client.isEmpty())
        {
            throw  new ClientNotFoundException("Client not found");
        }
        return modelMapper.map(client.get(), ClientDTO.class);
    }

    @Override
    public ClientDTO getClientByDni(String dni) {
        log.info("Fetching client with dni: {}", dni);
        Optional<Client> client = clientRepository.findByDni(dni);
        if (client.isEmpty()) {
            throw new ClientNotFoundException("Client not found");
        }
        return modelMapper.map(client.get(), ClientDTO.class);
    }

    @Override
    public List<ClientDTO> getAllClients() {
        log.info("Fetching all clients");
        return clientRepository.findAll().stream().map(client -> modelMapper.map(client, ClientDTO.class)).toList();
    }

    @Override
    public List<ClientDTO> getActiveClients() {
        log.info("Fetching all active clients");
        return clientRepository.findByStatusClient('A').stream().map(client -> modelMapper.map(client, ClientDTO.class)).toList();
    }

    @Override
    public void deleteClient(Long id) {
        log.info("Deleting client with ID: {}", id);
        if (!clientRepository.existsById(id)) {
            throw new ClientNotFoundException("Client not found");
        }
        clientRepository.deleteById(id);

    }

    /**
     * Deactivates a client.
     * <p>
     * This method will find a client by the given id and set the status to 'I' (Inactive).
     * If no client is found, a RuntimeException will be thrown.
     * <p>
     * @param id the id of the client to be deactivated
     */
    @Override
    public void desactivateClient(Long id) {
        log.info("Deactivating client with ID: {}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with id: " + id));
        client.setStatusClient('I');
        clientRepository.save(client);
    }

    private void validateClientData(ClientDTO clientDTO) {
        if (clientDTO.getDni() == null || clientDTO.getDni().trim().isEmpty()) {
            throw new InvalidClientDataException("DNI cannot be empty");
        }
        if (clientDTO.getName() == null || clientDTO.getName().trim().isEmpty()) {
            throw new InvalidClientDataException("Name cannot be empty");
        }
        // Más validaciones según necesites
    }


    public Single<ClientDTO> getClientDetailByName(String name) {
        return Single.fromCallable(() -> clientRepository.findByName(name))
                .flatMap(client -> {
                    if (client.isEmpty()) {
                        return Single.error(new ClientNotFoundException("Cliente no encontrado: " + name));
                    }

                    return accountApi.getAccountsAndMovementsByClientId(client.get().getId())
                            .map(accounts -> {
                                ClientDTO clientDetailDTO = modelMapper.map(client, ClientDTO.class);
                                clientDetailDTO.setAccounts((List<AccountDTO>) accounts);
                                return clientDetailDTO;
                            });
                })
                .doOnError(error -> log.error("Error getting client details: ", error));
    }
}
