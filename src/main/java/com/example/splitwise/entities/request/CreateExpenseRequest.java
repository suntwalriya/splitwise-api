package com.example.splitwise.entities.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateExpenseRequest {
    private String description;
    private double amount;
    private int paidBy;
    private int createdBy;
    private Integer groupId; // Nullable, to indicate if it's a group expense
    private List<Participant> participants;

    @Data
    public static class Participant {
        private int userId;
        private double amount;
    }
}
