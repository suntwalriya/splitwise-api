package com.example.splitwise.repository.dao;

import com.example.splitwise.repository.table.ExpenseSplit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IExpenseSplitDAO extends JpaRepository<ExpenseSplit, Integer> {
    List<ExpenseSplit> findByExpenseIdIn(List<Integer> expenseIds);
}
