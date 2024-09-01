package com.example.splitwise.controller;

import com.example.splitwise.entities.request.LoginRequest;
import com.example.splitwise.exception.InvalidPasswordException;
import com.example.splitwise.exception.UserNotFoundException;
import com.example.splitwise.service.impl.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class LoginController {
    @Autowired
    private LoginService loginService;

    // TODO: Move register and login functionality to a separate auth service like Keycloak or OAuth.

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Map<String, Object> response = loginService.loginUser(loginRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserNotFoundException | InvalidPasswordException e) {
            log.error("Login error: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
