package com.example.splitwise.repository.dao;

import com.example.splitwise.repository.table.UserGroups;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUserGroupDAO extends JpaRepository<UserGroups, Integer> {
    List<UserGroups> findByCreatedById(Integer userId);
}
