package com.example.dto;

import com.example.enums.TransactionStatus;

import java.math.BigDecimal;

public record TransferResponse(
        String transactionId,
        TransactionStatus status,
        String message,
        String debitedFrom,
        String creditedTo,
        BigDecimal amount
) { }