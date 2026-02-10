package com.example.exception;

public sealed class TransferException extends RuntimeException
        permits AccountNotFoundException, InsufficientBalanceException, InvalidTransferException, ConcurrencyException {

    public TransferException(String message) {
        super(message);
    }

    public TransferException(String message, Throwable cause) {
        super(message, cause);
    }
}
