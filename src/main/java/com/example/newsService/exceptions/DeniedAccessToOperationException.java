package com.example.newsService.exceptions;

public class DeniedAccessToOperationException extends RuntimeException{

    public DeniedAccessToOperationException(String message) {
        super(message);
    }
}
