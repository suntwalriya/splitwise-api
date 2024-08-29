package com.example.splitwise.entities.response;

import com.example.splitwise.entities.dto.TransactionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettleExpenseResponse {
    private List<TransactionDTO> transactions;
    private boolean success;
    private String message;
}
