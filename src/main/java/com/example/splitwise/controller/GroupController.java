package com.example.splitwise.controller;

import com.example.splitwise.entities.request.CreateGroupRequest;
import com.example.splitwise.entities.response.CreateGroupResponse;
import com.example.splitwise.exception.GroupAlreadyExistsException;
import com.example.splitwise.exception.UserNotFoundException;
import com.example.splitwise.service.IGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class GroupController {

    @Autowired
    private IGroupService groupService;

    //TODO:: Pass user id in header for future use in auth filter

    @PostMapping(value = "/groups")
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupRequest request) {
        try {
            CreateGroupResponse response = groupService.createGroup(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED); // 201 status for successful creation
        } catch (GroupAlreadyExistsException ex) {
            log.error("Error creating group: {}", ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // 400 status for user-defined exceptions
        } catch (UserNotFoundException ex) {
            log.error("Error creating group: {}", ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // 400 status for user-defined exceptions
        } catch (Exception ex) {
            log.error("An unexpected error occurred: {}", ex.getMessage());
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR); // 500 status for all other exceptions
        }
    }
}

