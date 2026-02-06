package com.example.entity;

import com.example.enums.AccountStatus;
import com.example.exception.InsufficientBalanceException;

import java.time.Instant;

public class Account {
    private final String id;
    private String holderName;
    private double balance;
    private AccountStatus status;
    private long version;
    private Instant lastUpdated;

    public Account(String id, String holderName, double balance, AccountStatus status) {
        this.id = id;
        this.holderName = holderName;
        this.balance = balance;
        this.status = status;
        this.version = 1;
        this.lastUpdated = Instant.now();
    }

    public void debit(double amount) {
        if (balance < amount) {
            throw new InsufficientBalanceException(id, amount);
        }
        balance -= amount;
        lastUpdated = Instant.now();
    }

    public void credit(double amount) {
        balance += amount;
        lastUpdated = Instant.now();
    }

    public boolean isActive() {
        return status == AccountStatus.ACTIVE;
    }

    public double getBalance() {
        return balance;
    }

    public String getId() {
        return id;
    }
}
