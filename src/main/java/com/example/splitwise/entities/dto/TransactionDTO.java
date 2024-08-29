package com.example.splitwise.entities.dto;

import lombok.Data;

@Data
public class TransactionDTO {
    private String fromUserName;
    private String toUserName;
    private double amount;
}
