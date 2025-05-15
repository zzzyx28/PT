package com.example.test1.exception;

public class MessageOperationException extends RuntimeException {
    public MessageOperationException(String message) {
        super(message);
    }

    public MessageOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
