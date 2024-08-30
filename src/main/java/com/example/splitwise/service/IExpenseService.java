package com.example.splitwise.service;

import com.example.splitwise.entities.request.AddExpenseRequest;
import com.example.splitwise.entities.response.AddExpenseResponse;

public interface IExpenseService {
    AddExpenseResponse addExpense(AddExpenseRequest request);
}
