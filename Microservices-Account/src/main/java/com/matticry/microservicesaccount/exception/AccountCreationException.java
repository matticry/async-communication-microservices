package com.matticry.microservicesaccount.exception;

public class AccountCreationException extends RuntimeException {

    public AccountCreationException(String message) {
        super(message);
    }
}
