package com.example.exception;

public final class DuplicateTransferException extends BankingException {
    public DuplicateTransferException(String key) {
        super("Duplicate transfer with idempotency key: " + key);
    }
}