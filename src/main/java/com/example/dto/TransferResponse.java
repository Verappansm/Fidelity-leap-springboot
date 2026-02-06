package com.example.dto;

import com.example.enums.TransactionStatus;

public record TransferResponse(
        String transactionId,
        TransactionStatus status,
        String failureReason
) {}
