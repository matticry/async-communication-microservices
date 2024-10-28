package com.matticry.microservicesproject.api;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

public interface ApiAccountInterface {

    @NonNull Single<List<?>> getAccountsAndMovementsByClientId(Long id);


}
