package com.example.newsService.exceptions;

public class EntityIsNotFoundException extends RuntimeException {

    public EntityIsNotFoundException(String message) {
        super(message);
    }
}
