package com.example.splitwise.service;

import com.example.splitwise.entities.request.LoginRequest;
import java.util.Map;

public interface ILoginService {
    Map<String, Object> loginUser(LoginRequest loginRequest) throws Exception;
}
