package com.example.splitwise.entities.response;

import com.example.splitwise.repository.table.UserGroupMapping;
import com.example.splitwise.repository.table.UserGroups;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGroupResponse {
    private UserGroups group;
    private List<UserGroupMapping> userMappings;
}
