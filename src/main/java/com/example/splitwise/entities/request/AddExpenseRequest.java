package com.example.splitwise.entities.request;

import com.example.splitwise.entities.enums.Currency;
import com.example.splitwise.entities.enums.SplitType;
import com.example.splitwise.repository.table.User;
import com.example.splitwise.repository.table.UserGroups;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddExpenseRequest {
    private String description;
    private double amount;
    private Currency currency;
    private SplitType splitType;
    private Integer groupId;  // Optional, either groupId or friendId should be provided
    private Integer friendId; // Optional, either groupId or friendId should be provided
    private int paidBy; // ID of the user who paid
    private int createdById; // ID of the user who created the expense
    private List<SplitDetail> splits;  // List of split details based on splitType

    private User validatedPayer;
    private User validatedCreator;
    private UserGroups validatedGroup;
    @Data
    public static class SplitDetail {
        private int userId;   // User to whom this split applies
        private double value; // The split value (amount/percentage/ratio)
    }
}
