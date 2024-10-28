package com.matticry.microservicesaccount.controller;

import com.matticry.microservicesaccount.dto.AccountRequestDTO;
import com.matticry.microservicesaccount.dto.AccountResponseDTO;
import com.matticry.microservicesaccount.service.AccountServiceImpl;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountServiceImpl accountService;

    @PostMapping
    public Single<ResponseEntity<AccountResponseDTO>> createAccount(@RequestBody AccountRequestDTO requestDTO) {
        return accountService.createAccount(requestDTO)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }



    @GetMapping("/client/{clientId}")
    public Single<ResponseEntity<List<AccountResponseDTO>>> getAccountsByClientId(
            @PathVariable Integer clientId) {
        return accountService.getAccountsByClientId(clientId)
                .map(ResponseEntity::ok)
                .doOnError(error -> log.error("Error getting accounts: ", error));
    }
}