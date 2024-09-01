package com.example.splitwise.repository.dao;

import com.example.splitwise.repository.table.UserGroupBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IUserGroupBalanceDAO  extends JpaRepository<UserGroupBalance, Integer> {
    Optional<UserGroupBalance> findByUserIdAndGroupId(int userId, Integer groupId);

    @Query("SELECT ugb.groupId, ugb.balance FROM UserGroupBalance ugb WHERE ugb.userId = :userId")
    List<Object[]> findGroupBalancesByUserId(@Param("userId") int userId);
}
