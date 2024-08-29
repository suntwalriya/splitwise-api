package com.example.splitwise.entities.response;

import com.example.splitwise.entities.enums.Currency;
import com.example.splitwise.repository.table.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGroupResponse {
    private boolean success;
    private String message;
    private List<String> nonRegisteredUsers;
}
