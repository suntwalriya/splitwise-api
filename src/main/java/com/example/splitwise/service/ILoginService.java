package com.example.splitwise.service;

import com.example.splitwise.entities.request.LoginRequest;

public interface ILoginService {
    String loginUser(LoginRequest loginRequest);
}
