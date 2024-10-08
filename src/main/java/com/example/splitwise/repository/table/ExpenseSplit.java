package com.example.splitwise.repository.table;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "expense_splits")
@Data
public class ExpenseSplit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "expense_id", nullable = false)
    private int expenseId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "amount", nullable = false)
    private double amount;
}
