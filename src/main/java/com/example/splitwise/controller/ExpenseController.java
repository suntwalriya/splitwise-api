package com.example.splitwise.controller;

import com.example.splitwise.entities.request.CreateExpenseRequest;
import com.example.splitwise.entities.response.CreateExpenseResponse;
import com.example.splitwise.service.impl.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/create")
    public ResponseEntity<CreateExpenseResponse> createExpense(@RequestBody CreateExpenseRequest request) {
        CreateExpenseResponse response = expenseService.createExpense(request);
        return ResponseEntity.ok(response);
    }
}
