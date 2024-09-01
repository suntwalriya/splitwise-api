package com.example.splitwise.repository.dao;

import com.example.splitwise.repository.table.UserGroups;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserGroupDAO extends JpaRepository<UserGroups, Integer> {
    Optional<Object> findByNameAndCreatedBy(String groupName, int createdById);
}
