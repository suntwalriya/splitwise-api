package com.example.splitwise.exception;

public class GroupAlreadyExistsException extends RuntimeException {
    public GroupAlreadyExistsException(String message) {
        super(message);
    }
}
