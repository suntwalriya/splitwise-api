package com.example.splitwise.repository.dao;

import com.example.splitwise.repository.table.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserDAO extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByContactNumber(String contactNumber);
}
