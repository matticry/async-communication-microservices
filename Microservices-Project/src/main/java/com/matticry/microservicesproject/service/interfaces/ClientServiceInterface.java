package com.matticry.microservicesproject.service.interfaces;

import com.matticry.microservicesproject.dto.ClientDTO;

import java.util.List;

public interface ClientServiceInterface {
    ClientDTO createClient(ClientDTO clientDTO);

    ClientDTO updateClient(ClientDTO clientDTO, Long id);

    ClientDTO getClientById(Long id);

    ClientDTO getClientByName (String name);

    ClientDTO getClientByDni(String dni);

    List<ClientDTO> getAllClients();

    List<ClientDTO> getActiveClients();
    void deleteClient(Long id);
    void desactivateClient(Long id);



}
