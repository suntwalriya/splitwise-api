package com.example.splitwise.repository.dao;

import com.example.splitwise.repository.table.UserGroupMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserGroupMappingDAO extends JpaRepository<UserGroupMapping, Integer> {
}
