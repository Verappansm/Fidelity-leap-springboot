package com.example.repo;

import com.example.entity.TransactionLog;

import java.util.List;
import java.util.Optional;

public interface TransactionLogRepository {
    TransactionLog save(TransactionLog log);
    List<TransactionLog> findByAccountId(String accountId);
    Optional<TransactionLog> findByIdempotencyKey(String key);
    List<TransactionLog> findAll();
}
