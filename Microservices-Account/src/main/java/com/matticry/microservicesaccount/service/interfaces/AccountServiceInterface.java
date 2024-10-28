package com.matticry.microservicesaccount.service.interfaces;

import com.matticry.microservicesaccount.dto.AccountRequestDTO;
import com.matticry.microservicesaccount.dto.AccountResponseDTO;
import io.reactivex.rxjava3.core.Single;

public interface AccountServiceInterface {
    Single<AccountResponseDTO> createAccount(AccountRequestDTO accountRequestDTO);

}
