package com.example.dto;

import com.example.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransferResponse(
        UUID transactionId,
        Long fromAccountId,
        Long toAccountId,
        BigDecimal amount,
        TransactionStatus status,
        String failureReason,
        String idempotencyKey,
        LocalDateTime createdAt
) {
}
