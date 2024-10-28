package com.matticry.microservicesproject.exception;

public class InvalidClientDataException extends ClientServiceException {
    public InvalidClientDataException(String message) {
        super(message);
    }
}
