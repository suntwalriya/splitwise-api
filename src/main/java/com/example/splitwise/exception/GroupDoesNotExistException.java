package com.example.splitwise.exception;

public class GroupDoesNotExistException extends RuntimeException {
    public GroupDoesNotExistException(String message) {
        super(message);
    }
}