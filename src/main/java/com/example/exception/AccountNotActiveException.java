package com.example.exception;

public final class AccountNotActiveException extends BankingException {
    public AccountNotActiveException(String accountId) {
        super("Account not active: " + accountId);
    }
}
