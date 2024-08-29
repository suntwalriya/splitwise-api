package com.example.splitwise.entities.request;

import com.example.splitwise.entities.enums.Currency;
import com.example.splitwise.repository.table.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGroupRequest {
    private String name;
    private String description;
    private Currency currency;
    private List<User> usersList;
    private int createdById;
}
