package com.matticry.microservicesproject.controller;

import com.matticry.microservicesproject.dto.ClientDTO;
import com.matticry.microservicesproject.service.ClientService;
import io.reactivex.rxjava3.core.Single;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@Tag(name = "Client Controller", description = "API endpoints for client management")
@RequiredArgsConstructor
@Slf4j
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) {
        return new ResponseEntity<>(clientService.createClient(clientDTO), HttpStatus.CREATED); // Mejor usar CREATED (201)
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/active")
    public ResponseEntity<List<ClientDTO>> getActiveClients() {
        return ResponseEntity.ok(clientService.getActiveClients());
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateClient(
            @PathVariable Long id) { // Falta @PathVariable
        clientService.desactivateClient(id);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(
            @PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(
            @RequestBody ClientDTO clientDTO,
            @PathVariable Long id) {
        return ResponseEntity.ok(clientService.updateClient(clientDTO, id));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(
            @PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ClientDTO> getClientByName(@PathVariable String name)
    {
        return ResponseEntity.ok(clientService.getClientByName(name));
    }

    @GetMapping("/details/{name}")
    public Single<ResponseEntity<ClientDTO>> getClientDetails(@PathVariable String name) {
        return clientService.getClientDetailByName(name)
                .map(ResponseEntity::ok)
                .doOnError(error -> log.error("Error getting client details: ", error));
    }

}

