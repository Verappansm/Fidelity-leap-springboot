package com.example.entity;

import com.example.enums.AccountStatus;
import com.example.exception.InsufficientBalanceException;

import java.math.BigDecimal;
import java.time.Instant;

public class Account {
    private final String id;
    private String holderName;
    private BigDecimal balance;
    private AccountStatus status;
    private long version;
    private Instant lastUpdated;

    public Account(String id, String holderName, BigDecimal openingBalance, AccountStatus status) {
        if (id == null || holderName == null || openingBalance == null || status == null) {
            throw new IllegalArgumentException("Account fields must not be null");
        }
        if (openingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Opening balance cannot be negative");
        }
        this.id = id;
        this.holderName = holderName;
        this.balance = openingBalance;
        this.status = status;
        this.version = 1;
        this.lastUpdated = Instant.now();
    }

    public void debit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Debit amount must be positive");
        if (balance.compareTo(amount) < 0)
            throw new InsufficientBalanceException(id, amount);
        balance = balance.subtract(amount);
        version++;
        lastUpdated = Instant.now();
    }

    public void credit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Credit amount must be positive");
        balance = balance.add(amount);
        version++;
        lastUpdated = Instant.now();
    }

    public boolean isActive() { return status == AccountStatus.ACTIVE; }
    public String getId() { return id; }
    public String getHolderName() { return holderName; }
    public BigDecimal getBalance() { return balance; }
    public AccountStatus getStatus() { return status; }
    public Instant getLastUpdated() { return lastUpdated; }
}