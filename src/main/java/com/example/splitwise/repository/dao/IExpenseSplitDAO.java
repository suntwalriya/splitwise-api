package com.example.splitwise.repository.dao;

import com.example.splitwise.repository.table.ExpenseSplit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IExpenseSplitDAO extends JpaRepository<ExpenseSplit, Integer> {
}
