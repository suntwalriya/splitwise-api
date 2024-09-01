package com.example.splitwise.service;

import com.example.splitwise.entities.request.CreateExpenseRequest;
import com.example.splitwise.entities.response.CreateExpenseResponse;

public interface IExpenseService {
    CreateExpenseResponse createExpense(CreateExpenseRequest request);
}
