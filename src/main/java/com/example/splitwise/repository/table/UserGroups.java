package com.example.splitwise.repository.table;

import com.example.splitwise.entities.enums.Currency;
import com.example.splitwise.entities.enums.ExpenseSettled;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user_groups")
@Data
public class UserGroups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "total_amount_spent", nullable = false)
    private double totalAmountSpent;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_settled", nullable = false)
    private ExpenseSettled isSettled;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_currency", nullable = false)
    private Currency defaultCurrency;

    @ManyToMany
    @JoinTable(
            name = "user_group_mapping",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    @CreatedDate
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdAt;

    @LastModifiedDate
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
