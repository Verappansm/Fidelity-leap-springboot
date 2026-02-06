package com.example.dto;

public record TransferRequest(
        String fromAccountId,
        String toAccountId,
        Double amount,
        String idempotencyKey
) {
    public TransferRequest {
        if (fromAccountId == null || toAccountId == null || amount == null || idempotencyKey == null) {
            throw new IllegalArgumentException("Fields must not be null");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}
