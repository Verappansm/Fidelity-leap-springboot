package com.example.exception;

public sealed class BankingException extends RuntimeException
        permits AccountNotFoundException, AccountNotActiveException,
        InsufficientBalanceException, DuplicateTransferException {

    public BankingException(String message) {
        super(message);
    }
}
