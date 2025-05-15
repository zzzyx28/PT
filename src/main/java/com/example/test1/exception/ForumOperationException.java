package com.example.test1.exception;

public class ForumOperationException extends RuntimeException {
    public ForumOperationException(String message) {
        super(message);
    }

    public ForumOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
