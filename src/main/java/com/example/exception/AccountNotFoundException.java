package com.example.exception;

public final class AccountNotFoundException extends TransferException {

    public AccountNotFoundException(String message) {
        super(message);
    }
}
