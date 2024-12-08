package com.example.newsService.exceptions;

public class OperationIsNotAvailableException extends RuntimeException {

    public OperationIsNotAvailableException(String message) {
        super(message);
    }
}
