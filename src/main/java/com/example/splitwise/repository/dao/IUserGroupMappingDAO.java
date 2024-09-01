package com.example.splitwise.repository.dao;

import com.example.splitwise.repository.table.UserGroupMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUserGroupMappingDAO extends JpaRepository<UserGroupMapping, Integer> {
    List<UserGroupMapping> findByGroupId(int id);
}
