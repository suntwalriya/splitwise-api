package com.example.splitwise.repository.table;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_balances", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "friend_id"})
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "friend_id", nullable = false)
    private int friendId;

    @Column(name = "balance", nullable = false)
    private double balance;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public UserBalance(int userId, int friendId, double balance) {
        this.userId = userId;
        this.friendId = friendId;
        this.balance = balance;
    }
}
