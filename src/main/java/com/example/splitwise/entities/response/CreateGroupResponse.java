package com.example.splitwise.entities.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGroupResponse {
    private String message;
    private int groupId;
}
