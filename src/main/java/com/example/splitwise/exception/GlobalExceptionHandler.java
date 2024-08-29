package com.example.splitwise.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GroupAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleGroupAlreadyExistsException(GroupAlreadyExistsException ex) {
        System.out.println("Handling UserAlreadyExistsException");
        ErrorResponse errorResponse = new ErrorResponse("GROUP_ALREADY_EXISTS", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("USER_NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPasswordException(InvalidPasswordException ex) {
        ErrorResponse errorResponse = new ErrorResponse("INVALID_PASSWORD", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        // Log to confirm this handler is invoked
        System.out.println("Handling UserAlreadyExistsException");
        ErrorResponse errorResponse = new ErrorResponse("USER_ALREADY_EXISTS", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGroupNotFoundException(GroupNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("GROUP_NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidSplitException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSplitException(InvalidSplitException ex) {
        ErrorResponse errorResponse = new ErrorResponse("INVALID_SPLIT", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateExpenseException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateExpenseException(DuplicateExpenseException ex) {
        ErrorResponse errorResponse = new ErrorResponse("DUPLICATE_EXPENSE", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
