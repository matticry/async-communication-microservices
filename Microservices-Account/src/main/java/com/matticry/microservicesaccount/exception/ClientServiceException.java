package com.matticry.microservicesaccount.exception;

public class ClientServiceException extends RuntimeException {
    public ClientServiceException(String message) {
        super(message);
    }
}
