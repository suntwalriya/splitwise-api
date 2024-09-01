package com.example.splitwise.controller;

import com.example.splitwise.entities.request.CreateGroupRequest;
import com.example.splitwise.entities.response.CreateGroupResponse;
import com.example.splitwise.service.IGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/groups")
@Slf4j
public class GroupController {

    @Autowired
    private IGroupService groupService;

    @PostMapping(value = "/createGroup")
    public ResponseEntity createGroup(@RequestBody CreateGroupRequest request) {
        log.debug("Inside createGroup Controller");
        CreateGroupResponse response = groupService.createGroup(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

