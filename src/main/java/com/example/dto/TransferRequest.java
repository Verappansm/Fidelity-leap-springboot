package com.example.dto;

import java.math.BigDecimal;

public record TransferRequest(String fromAccountId,
                              String toAccountId,
                              BigDecimal amount,
                              String idempotencyKey) {
    public TransferRequest {
        if (fromAccountId == null || toAccountId == null || amount == null || idempotencyKey == null)
            throw new IllegalArgumentException("Fields must not be null");
        if (fromAccountId.equals(toAccountId))
            throw new IllegalArgumentException("Accounts must be different");
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be positive");
    }
}