package com.example.splitwise.service;

import com.example.splitwise.entities.request.CreateGroupRequest;
import com.example.splitwise.entities.response.CreateGroupResponse;

public interface IGroupService {
    CreateGroupResponse createGroup(CreateGroupRequest groupData);
}