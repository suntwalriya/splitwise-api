package com.example.splitwise.controller;

import com.example.splitwise.entities.response.FetchBalanceResponse;
import com.example.splitwise.exception.UserNotFoundException;
import com.example.splitwise.service.impl.FetchBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class FetchBalanceController {
    @Autowired
    private FetchBalanceService fetchBalanceService;

    //TODO:: We can have three different API endpoints for the balance API -
    // /api/v1/balances/overall
    // /api/v1/balances/friends
    // /api/v1/balances/group/{groupId}

    //TODO:: Pass user id in header for future use in auth filter

    @GetMapping("/fetch/details/{userId}")
    public ResponseEntity<?> fetchDetails(@PathVariable("userId") Integer userId) {
        try {
            FetchBalanceResponse response = fetchBalanceService.getBalanceDetails(userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
