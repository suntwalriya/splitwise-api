package com.example.splitwise.repository.dao;

import com.example.splitwise.repository.table.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IExpenseDAO extends JpaRepository<Expense, Integer> {
}
