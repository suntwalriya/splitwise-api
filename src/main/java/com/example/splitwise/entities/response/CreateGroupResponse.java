package com.example.splitwise.entities.response;

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
