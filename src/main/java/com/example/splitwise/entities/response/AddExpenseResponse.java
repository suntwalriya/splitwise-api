package com.example.splitwise.entities.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddExpenseResponse {
    private int expenseId;
    private String description;
    private double amount;
    private String currency;
    private String splitType;
    private String paidBy;
    private String createdBy;
    private Date createdAt;
}
