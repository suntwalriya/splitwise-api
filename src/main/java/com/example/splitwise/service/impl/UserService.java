package com.example.splitwise.service.impl;

import com.example.splitwise.exception.UserAlreadyExistsException;
import com.example.splitwise.repository.dao.IUserDAO;
import com.example.splitwise.repository.table.User;
import com.example.splitwise.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserDAO iUserDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        Optional<User> existingUserByUsername = iUserDAO.findByUsername(user.getUsername());
        Optional<User> existingUserByContactNumber = iUserDAO.findByContactNumber(user.getContactNumber());

        if (existingUserByUsername.isPresent() || existingUserByContactNumber.isPresent()) {
            throw new UserAlreadyExistsException("User already exists, please login directly.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return iUserDAO.save(user);
    }
}
