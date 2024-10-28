package com.matticry.microservicesproject.exception;

public class ClientAlreadyExistsException extends ClientServiceException {
    public ClientAlreadyExistsException(String message) {
        super(message);
    }
}
