package com.example.splitwise.repository.table;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

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

    @Column(name = "paid_by", nullable = false)
    private int paidBy;

    @Column(name = "created_by", nullable = false)
    private int createdBy;

    @Column(name = "group_id", nullable = true)
    private Integer groupId; // Nullable for friend-to-friend expenses

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
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
