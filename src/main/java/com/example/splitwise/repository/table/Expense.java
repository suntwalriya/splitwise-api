package com.example.splitwise.repository.table;

import com.example.splitwise.entities.enums.Currency;
import com.example.splitwise.entities.enums.SplitType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "expenses")
@Data
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Column(name = "paid_by", nullable = false)
    private int paidBy;

    @Column(name = "group_id")
    private int groupId;

    @Enumerated(EnumType.STRING)
    @Column(name = "split_type", nullable = false)
    private SplitType splitType;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "expense_id")
    private List<ExpenseSplit> expenseSplits;

    @Column(name = "created_by_id", nullable = false)
    private int createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
