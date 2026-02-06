package com.example.entity;

import com.example.enums.TransactionStatus;
import java.time.Instant;

public class TransactionLog {
    private final String id;
    private final String fromAccountId;
    private final String toAccountId;
    private final double amount;
    private TransactionStatus status;
    private String failureReason;
    private String idempotencyKey;
    private final Instant createdOn = Instant.now();

    public TransactionLog(String id, String fromAccountId, String toAccountId, double amount, String idempotencyKey) {
        this.id = id;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.idempotencyKey = idempotencyKey;
    }
}
