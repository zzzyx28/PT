package com.example.test1.exception;

public class TorrentProcessingException extends RuntimeException {
    public TorrentProcessingException(String message) {
        super(message);
    }

    public TorrentProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}