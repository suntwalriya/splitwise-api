package com.example.splitwise.controller;

import com.example.splitwise.entities.response.FetchBalanceResponse;
import com.example.splitwise.service.impl.FetchBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fetch")
public class FetchBalanceController {
    @Autowired
    private FetchBalanceService fetchBalanceService;

    @GetMapping("/details/{userId}")
    public ResponseEntity<FetchBalanceResponse> fetchDetails(@PathVariable("userId") Integer userId) {
        FetchBalanceResponse response = fetchBalanceService.getBalanceDetails(userId);
        return ResponseEntity.ok(response);
    }
}
