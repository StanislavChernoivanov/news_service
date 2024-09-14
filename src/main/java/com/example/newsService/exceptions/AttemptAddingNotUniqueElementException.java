package com.example.newsService.exceptions;

public class AttemptAddingNotUniqueElementException extends RuntimeException {

    public AttemptAddingNotUniqueElementException(String message) {
        super(message);
    }
}
