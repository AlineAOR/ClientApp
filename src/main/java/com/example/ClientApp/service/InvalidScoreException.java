package com.example.ClientApp.service;

public class InvalidScoreException extends RuntimeException {
    public InvalidScoreException(String message) {
        super(message);
    }
}
