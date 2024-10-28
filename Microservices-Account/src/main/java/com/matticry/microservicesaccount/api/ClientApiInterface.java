package com.matticry.microservicesaccount.api;

import com.matticry.microservicesaccount.dto.ClientDTO;
import io.reactivex.rxjava3.core.Single;

public interface ClientApiInterface {
    Single<ClientDTO> getClientByName(String name);
    Single<ClientDTO> getClientById(Long id);
}
