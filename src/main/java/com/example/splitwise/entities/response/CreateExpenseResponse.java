package com.example.splitwise.entities.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateExpenseResponse {
    private String message;
    private double overallBalance;
    private Map<Integer, Double> friendBalances;
}