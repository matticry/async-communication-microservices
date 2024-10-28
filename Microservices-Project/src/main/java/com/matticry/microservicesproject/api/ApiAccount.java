package com.matticry.microservicesproject.api;

import com.matticry.microservicesproject.dto.AccountDTO;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ApiAccount implements ApiAccountInterface {

    private final RestTemplate restTemplate;
    private final String baseUrl;


    public ApiAccount(
            RestTemplate restTemplate,
            @Value("${microservice.accounts.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }


    @Override
    public @NonNull Single<List<?>> getAccountsAndMovementsByClientId(Long clientId) {
        return Single.fromCallable(() -> {
            try {
                String url = baseUrl + "/api/v1/accounts/client/" + clientId;
                log.info("Calling account service with URL: {}", url);

                ResponseEntity<AccountDTO[]> response = restTemplate.getForEntity(
                        url,
                        AccountDTO[].class
                );

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    return Arrays.asList(response.getBody());
                } else {
                    return Collections.emptyList();
                }
            } catch (RestClientException e) {
                log.error("Error calling account service: ", e);
                throw new RuntimeException("Error en la comunicación con el servicio de cuentas", e);
            }
        }).subscribeOn(Schedulers.io());
    }
}
