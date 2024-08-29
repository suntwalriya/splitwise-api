package com.example.splitwise.service.impl;

import com.example.splitwise.entities.enums.ExpenseSettled;
import com.example.splitwise.entities.request.CreateGroupRequest;
import com.example.splitwise.entities.response.CreateGroupResponse;
import com.example.splitwise.exception.GroupAlreadyExistsException;
import com.example.splitwise.repository.dao.IUserDAO;
import com.example.splitwise.repository.table.User;
import com.example.splitwise.repository.table.UserGroups;
import com.example.splitwise.repository.dao.IGroupDAO;
import com.example.splitwise.service.IGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GroupService implements IGroupService{

    @Autowired
    private IGroupDAO iGroupDAO;

    @Autowired
    private IUserDAO iUserDAO;

    public CreateGroupResponse createGroup(CreateGroupRequest request) {

        log.debug("Inside createGroup method");

        // Check if a group with the same name exists for the user
        Optional<UserGroups> existingGroup = iGroupDAO.findByNameAndCreatedById(request.getName(), request.getCreatedById());
        if (existingGroup.isPresent()) {
            throw new GroupAlreadyExistsException("Group with the same name already exists.");
        }

        // Fetch the user who is creating the group
        User creator = iUserDAO.findById(request.getCreatedById())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create the group
        UserGroups userGroups = new UserGroups();
        userGroups.setName(request.getName());
        userGroups.setDescription(request.getDescription());
        userGroups.setDefaultCurrency(request.getCurrency());
        userGroups.setCreatedBy(creator);

        // Set the isSettled field with a default value, e.g., UNSETTLED
        userGroups.setIsSettled(ExpenseSettled.UNSETTLED);

        // Validate and add users to the group
        List<User> usersToAdd = new ArrayList<>();
        List<String> nonRegisteredUsers = new ArrayList<>();
        for (User userDTO : request.getUsersList()) {
            Optional<User> user = iUserDAO.findByEmailId(userDTO.getEmailId());
            if (user.isPresent()) {
                usersToAdd.add(user.get());
            } else {
                nonRegisteredUsers.add(userDTO.getEmailId());
            }
        }

        // Set users in the group and save
        userGroups.setUsers(usersToAdd);
        iGroupDAO.save(userGroups);

        if (nonRegisteredUsers.isEmpty()) {
            return new CreateGroupResponse(true, "Group created successfully.", null);
        } else {
            return new CreateGroupResponse(true, "Group created successfully with some users added. Some users were not added as they are not registered.", nonRegisteredUsers);
        }
    }
}
