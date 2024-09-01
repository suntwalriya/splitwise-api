package com.example.splitwise.service.impl;

import com.example.splitwise.entities.request.CreateGroupRequest;
import com.example.splitwise.entities.response.CreateGroupResponse;
import com.example.splitwise.exception.GroupAlreadyExistsException;
import com.example.splitwise.exception.UserNotFoundException;
import com.example.splitwise.repository.dao.IUserDAO;
import com.example.splitwise.repository.dao.IUserGroupDAO;
import com.example.splitwise.repository.dao.IUserGroupMappingDAO;
import com.example.splitwise.repository.table.UserGroupMapping;
import com.example.splitwise.repository.table.UserGroups;
import com.example.splitwise.service.IGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class GroupService implements IGroupService{

    @Autowired
    private IUserDAO iUserDAO;

    @Autowired
    private IUserGroupDAO iUserGroupDAO;

    @Autowired
    private IUserGroupMappingDAO iUserGroupMappingDAO;

    // To create a group
    public CreateGroupResponse createGroup(CreateGroupRequest request) {

        // Step 1: Perform validation and preprocessing
        validateAndPreprocessCreateGroupRequest(request);

        // Step 2: Create the group
        UserGroups group = createGroupEntity(request);

        // Step 3: Create user-group mappings
        createUserGroupMappings(group.getId(), request.getUserIds());

        // Step 4: Return the response
        return buildCreateGroupResponse(group);
    }

    // Consolidated validation and preprocessing function
    private void validateAndPreprocessCreateGroupRequest(CreateGroupRequest request) {

        // Check if a group with the same name already exists
        if (groupExists(request.getName(), request.getCreatedById())) {
            throw new GroupAlreadyExistsException("Group with the same name already exists");
        }

        // Validate that all users exist
        for (int userId : request.getUserIds()) {
            if (!iUserDAO.existsById(userId)) {
                throw new UserNotFoundException("User with ID " + userId + " not found.");
            }
        }
    }

    // To validate if a group exists for a groupName and createdBy
    private boolean groupExists(String groupName, int createdById) {
        return iUserGroupDAO.findByNameAndCreatedBy(groupName, createdById).isPresent();
    }

    // To create group entity
    private UserGroups createGroupEntity(CreateGroupRequest request) {
        UserGroups group = new UserGroups();
        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setCreatedBy(request.getCreatedById());
        return iUserGroupDAO.save(group);
    }

    // To create user group mapping for a groupId and list of userIds
    private void createUserGroupMappings(int groupId, List<Integer> userIds) {
        for (int userId : userIds) {
            UserGroupMapping mapping = new UserGroupMapping();
            mapping.setUserId(userId);
            mapping.setGroupId(groupId);
            mapping.setCreatedAt(new Date());
            mapping.setUpdatedAt(new Date());

            iUserGroupMappingDAO.save(mapping);
        }
    }

    private CreateGroupResponse buildCreateGroupResponse(UserGroups group) {
        List<UserGroupMapping> userMappings = iUserGroupMappingDAO.findByGroupId(group.getId());

        CreateGroupResponse response = new CreateGroupResponse();
        response.setGroup(group);
        response.setUserMappings(userMappings);

        return response;
    }
}
