package com.example.newsService.exceptions;

public class EntityIsNotUniqueException extends RuntimeException {

    public EntityIsNotUniqueException(String message) {
        super(message);
    }
}
