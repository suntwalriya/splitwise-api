package com.example.splitwise.controller;

import com.example.splitwise.entities.request.AddExpenseRequest;
import com.example.splitwise.entities.request.SettleExpenseRequest;
import com.example.splitwise.entities.response.AddExpenseResponse;
import com.example.splitwise.entities.response.SettleExpenseResponse;
import com.example.splitwise.repository.table.Expense;
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

    @PostMapping(value = "/addExpense")
    public ResponseEntity<AddExpenseResponse> addExpense(@RequestBody AddExpenseRequest request) {
        AddExpenseResponse expense = expenseService.addExpense(request);
        return ResponseEntity.ok(expense);
    }

    @PostMapping("/settle")
    public ResponseEntity<SettleExpenseResponse> settleExpense(@RequestBody SettleExpenseRequest request) {
        SettleExpenseResponse response = expenseService.settleExpenses(request);
        return ResponseEntity.ok(response);
    }
}
