package com.example.splitwise.repository.dao;

import com.example.splitwise.repository.table.UserGroups;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGroupDAO extends JpaRepository<UserGroups, Integer> {
}
