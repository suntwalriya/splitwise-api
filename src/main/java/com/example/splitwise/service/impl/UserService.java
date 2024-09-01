package com.example.splitwise.service.impl;

import com.example.splitwise.exception.UserAlreadyExistsException;
import com.example.splitwise.repository.dao.IUserDAO;
import com.example.splitwise.repository.table.User;
import com.example.splitwise.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService implements IUserService {
    @Autowired
    private IUserDAO iUserDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // TODO: Move register and login functionality to a separate auth service like Keycloak or OAuth.

    public User registerUser(User user) {

        // Step 1. Fetch the user who is registering
        Optional<User> existingUserByUsername = iUserDAO.findByUsername(user.getUsername());
        Optional<User> existingUserByContactNumber = iUserDAO.findByContactNumber(user.getContactNumber());

        // Step 2. Check if the user already exists as a registered user
        if (existingUserByUsername.isPresent() || existingUserByContactNumber.isPresent()) {
            log.debug("User already exists, please login directly.");
            throw new UserAlreadyExistsException("Invalid credentials");
        }

        // Step 3. Set the user password in an encoded manner
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return iUserDAO.save(user);
    }
}