package com.matticry.microservicesaccount.api;

import com.matticry.microservicesaccount.dto.ClientDTO;
import com.matticry.microservicesaccount.exception.ClientNotFoundException;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ClientApi implements ClientApiInterface {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ClientApi(
            RestTemplate restTemplate,
            @Value("${microservice.clients.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }


    @Override
    public Single<ClientDTO> getClientByName(String name) {
        return Single.fromCallable(() -> {
            try {
                // Corregimos la URL para que coincida con el endpoint correcto
                String url = baseUrl + "/clients/name/" + name;
                log.info("Calling client service with URL: {}", url);  // Para debugging

                ResponseEntity<ClientDTO> response = restTemplate.getForEntity(
                        url,
                        ClientDTO.class
                );

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    return response.getBody();
                } else {
                    throw new ClientNotFoundException("Cliente no encontrado: " + name);
                }
            } catch (RestClientException e) {
                log.error("Error calling client service: ", e);  // Para ver el error específico
                throw new RuntimeException("Error en la comunicación con el servicio de clientes", e);
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<ClientDTO> getClientById(Long id) {
        return Single.fromCallable(() -> {
            try {
                // Corregimos la URL para que coincida con el endpoint correcto
                String url = baseUrl + "/clients/" + id;
                log.info("Calling client service with URL: {}", url);  // Para debugging

                ResponseEntity<ClientDTO> response = restTemplate.getForEntity(
                        url,
                        ClientDTO.class
                );

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    return response.getBody();
                } else {
                    throw new ClientNotFoundException("Cliente no encontrado con ID: " + id);
                }
            } catch (RestClientException e) {
                log.error("Error calling client service: ", e);  // Para ver el error específico
                throw new RuntimeException("Error en la comunicación con el servicio de clientes", e);
            }
        }).subscribeOn(Schedulers.io());
    }
}
