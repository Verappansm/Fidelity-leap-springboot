package com.example.exception;

import java.math.BigDecimal;

public final class InsufficientBalanceException extends BankingException {
    public InsufficientBalanceException(String accountId, BigDecimal amount) {
        super("Insufficient balance in account " + accountId + " for amount " + amount);
    }
}