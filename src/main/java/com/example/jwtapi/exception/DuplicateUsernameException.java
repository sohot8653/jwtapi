package com.example.jwtapi.exception;

public class DuplicateUsernameException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public DuplicateUsernameException(String username) {
        super(String.format("Username '%s' already exists", username));
    }
} 