package com.example.exception;

public final class AccountNotFoundException extends BankingException {
    public AccountNotFoundException(String accountId) {
        super("Account not found: " + accountId);
    }
}
