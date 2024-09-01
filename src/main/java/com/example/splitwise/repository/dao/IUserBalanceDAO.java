package com.example.splitwise.repository.dao;

import com.example.splitwise.repository.table.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IUserBalanceDAO extends JpaRepository<UserBalance, Integer> {
    Optional<UserBalance> findByUserIdAndFriendId(int userId, int friendId);

    @Query("SELECT SUM(b.balance) FROM UserBalance b WHERE b.userId = :userId")
    Optional<Double> calculateOverallBalance(@Param("userId") int userId);

    List<UserBalance> findByUserId(int userId);
}