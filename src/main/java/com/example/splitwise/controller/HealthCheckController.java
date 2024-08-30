package com.example.splitwise.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/api/healthCheck")
    public String healthCheck() {
        return "Welcome to splitwise-api!";
    }
}
