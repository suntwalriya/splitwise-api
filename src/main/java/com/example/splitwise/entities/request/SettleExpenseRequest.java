package com.example.splitwise.entities.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettleExpenseRequest {
    private Integer groupId;   // For group settlements
    private Integer userId;    // For individual settlements
    private Integer friendId;  // For individual settlements

}
