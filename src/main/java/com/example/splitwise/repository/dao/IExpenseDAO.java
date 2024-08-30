package com.example.splitwise.repository.dao;

import com.example.splitwise.entities.enums.Currency;
import com.example.splitwise.entities.enums.SplitType;
import com.example.splitwise.repository.table.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IExpenseDAO extends JpaRepository<Expense, Integer> {
    List<Expense> findByDescriptionAndAmountAndCurrencyAndSplitTypeAndGroupIdAndPaidByAndCreatedBy(
            String description,
            double amount,
            Currency currency,
            SplitType splitType,
            Integer groupId,
            Integer paidBy,
            Integer createdBy
    );

    List<Expense> findByGroupId(Integer groupId);

    @Query("SELECT e FROM Expense e WHERE (e.paidBy = :userId AND e.createdBy = :friendId) OR (e.paidBy = :friendId AND e.createdBy = :userId)")
    List<Expense> findByUserAndFriend(@Param("userId") int userId, @Param("friendId") int friendId);

    @Query("SELECT e FROM Expense e WHERE e.paidBy = :userId OR e.createdBy = :userId OR e.groupId IN :groupIds")
    List<Expense> findByUserOrGroupIds(@Param("userId") Integer userId, @Param("groupIds") List<Integer> groupIds);

}
