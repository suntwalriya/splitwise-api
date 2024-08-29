package com.example.splitwise.controller;

import com.example.splitwise.entities.response.FetchTaskResponse;
import com.example.splitwise.service.impl.FetchTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fetch")
public class FetchTaskController {
    @Autowired
    private FetchTaskService fetchTaskService;

    @GetMapping("/details/{userId}")
    public ResponseEntity<FetchTaskResponse> fetchDetails(@PathVariable("userId") Integer userId) {
        FetchTaskResponse response = fetchTaskService.getFetchTaskDetails(userId);
        return ResponseEntity.ok(response);
    }
}
