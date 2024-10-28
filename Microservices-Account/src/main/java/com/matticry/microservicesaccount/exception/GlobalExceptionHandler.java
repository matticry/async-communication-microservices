package com.matticry.microservicesaccount.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleClientNotFoundException(ClientNotFoundException ex) {
        Map<String, Object> response = createErrorResponse(
                "CLIENT_NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountCreationException.class)
    public ResponseEntity<Map<String, Object>> handleAccountCreationException(AccountCreationException ex) {
        Map<String, Object> response = createErrorResponse(
                "ACCOUNT_CREATION_ERROR",
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ClientServiceException.class)
    public ResponseEntity<Map<String, Object>> handleClientServiceException(ClientServiceException ex) {
        Map<String, Object> response = createErrorResponse(
                "CLIENT_SERVICE_ERROR",
                ex.getMessage(),
                HttpStatus.SERVICE_UNAVAILABLE
        );
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    private Map<String, Object> createErrorResponse(String error, String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("error", error);
        response.put("message", message);
        response.put("status", status.value());
        return response;
    }
}
