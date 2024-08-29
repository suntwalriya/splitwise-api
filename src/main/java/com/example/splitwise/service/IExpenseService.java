package com.example.splitwise.service;

import com.example.splitwise.entities.request.AddExpenseRequest;
import com.example.splitwise.entities.response.AddExpenseResponse;
import com.example.splitwise.repository.table.Expense;

public interface IExpenseService {
    AddExpenseResponse addExpense(AddExpenseRequest request);
}
