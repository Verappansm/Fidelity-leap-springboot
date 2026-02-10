package com.example.exception;

public final class InsufficientBalanceException extends TransferException {

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
