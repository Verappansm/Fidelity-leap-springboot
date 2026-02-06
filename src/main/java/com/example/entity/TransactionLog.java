package com.example.entity;

import com.example.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class TransactionLog {
    private final String id;
    private final String fromAccountId;
    private final String toAccountId;
    private final BigDecimal amount;
    private TransactionStatus status;
    private String failureReason;
    private final String idempotencyKey;
    private final Instant createdOn = Instant.now();

    public TransactionLog(String id, String fromAccountId, String toAccountId,
                          BigDecimal amount, String idempotencyKey) {
        this.id = Objects.requireNonNull(id);
        this.fromAccountId = Objects.requireNonNull(fromAccountId);
        this.toAccountId = Objects.requireNonNull(toAccountId);
        this.amount = Objects.requireNonNull(amount);
        this.idempotencyKey = Objects.requireNonNull(idempotencyKey);
    }

    public String getId() { return id; }
    public String getFromAccountId() { return fromAccountId; }
    public String getToAccountId() { return toAccountId; }
    public BigDecimal getAmount() { return amount; }
    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public Instant getCreatedOn() { return createdOn; }
}