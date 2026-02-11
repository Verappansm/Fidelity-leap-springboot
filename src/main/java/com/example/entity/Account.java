package com.example.entity;

import com.example.enums.AccountStatus;
import com.example.enums.AccountType;
import com.example.exception.InsufficientBalanceException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "holder_name", nullable = false, length = 100)
    private String holderName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AccountStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false, length = 20)
    private AccountType accountType;

    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return switch (this.status) {
            case ACTIVE -> true;
            case LOCKED, CLOSED -> false;
        };
    }

    public void debit(BigDecimal amount) {
        BigDecimal minimumBalance = this.accountType.getMinimumBalance();
        BigDecimal resultingBalance = this.balance.subtract(amount);

        if (resultingBalance.compareTo(minimumBalance) < 0) {
            throw new InsufficientBalanceException(
                    """
                    Insufficient balance. Account %d (%s) requires minimum balance of %s. \
                    Current: %s, Debit: %s, Resulting: %s""".formatted(
                            this.id, this.accountType, minimumBalance,
                            this.balance, amount, resultingBalance));
        }

        this.balance = resultingBalance;
    }

    public void credit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }
}
