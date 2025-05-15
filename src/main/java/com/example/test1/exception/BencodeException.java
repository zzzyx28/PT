package com.example.test1.exception;

public class BencodeException extends Exception {
    public BencodeException(String message) {
        super(message);
    }

    public BencodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
