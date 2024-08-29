package com.example.splitwise.service.impl;

import com.example.splitwise.entities.request.LoginRequest;
import com.example.splitwise.exception.InvalidPasswordException;
import com.example.splitwise.exception.UserNotFoundException;
import com.example.splitwise.repository.dao.IUserDAO;
import com.example.splitwise.repository.table.User;
import com.example.splitwise.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService implements ILoginService {
    @Autowired
    private IUserDAO iUserDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String loginUser(LoginRequest loginRequest) {
        Optional<User> userOptional = iUserDAO.findByUsername(loginRequest.getUsername());

        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("User not found");
        }

        User user = userOptional.get();

        // Check if the user is active
        if (Boolean.FALSE.equals(user.getIsActive())) {
            user.setIsActive(Boolean.TRUE);
            iUserDAO.save(user);
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        // Optionally, we can generate a session token here
        return "Login successful!";
    }
}
