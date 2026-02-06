package com.example.exception;

public final class InsufficientBalanceException extends BankingException {
    public InsufficientBalanceException(String accountId, double amount) {
        super("Insufficient balance in account " + accountId + " for amount " + amount);
    }
}
