package com.example.splitwise.controller;

import com.example.splitwise.entities.request.CreateExpenseRequest;
import com.example.splitwise.entities.response.CreateExpenseResponse;
import com.example.splitwise.exception.GroupDoesNotExistException;
import com.example.splitwise.service.impl.ExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    //TODO:: Pass user id in header for future use in auth filter

    @PostMapping("/expenses")
    public ResponseEntity<?> createExpense(@RequestBody CreateExpenseRequest request) {
        try {
            CreateExpenseResponse response = expenseService.createExpense(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED); // Return 201 for success
        } catch (GroupDoesNotExistException ex) {
            log.error("Group does not exist: {}", ex.getMessage());
            return new ResponseEntity<>(new CreateExpenseResponse(ex.getMessage(), 0.0, null), HttpStatus.BAD_REQUEST); // 400 for known exceptions
        } catch (Exception ex) {
            log.error("Internal server error: {}", ex.getMessage());
            return new ResponseEntity<>(new CreateExpenseResponse("An unexpected error occurred", 0.0, null), HttpStatus.INTERNAL_SERVER_ERROR); // 500 for all others
        }
    }
}
