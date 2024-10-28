package com.matticry.microservicesproject.exception;

// Excepciones específicas
public class ClientNotFoundException extends ClientServiceException {
    public ClientNotFoundException(String message) {
        super(message);
    }
}
