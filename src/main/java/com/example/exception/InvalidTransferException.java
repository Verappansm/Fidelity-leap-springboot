package com.example.exception;

public final class InvalidTransferException extends TransferException {

    public InvalidTransferException(String message) {
        super(message);
    }
}
