package com.example.repo;

import com.example.entity.TransactionLog;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryTransactionLogRepository implements TransactionLogRepository {
    private final Map<String, TransactionLog> byId = new ConcurrentHashMap<>();
    private final Map<String, String> byIdempotency = new ConcurrentHashMap<>();

    @Override public TransactionLog save(TransactionLog log) {
        byId.put(log.getId(), log);
        byIdempotency.putIfAbsent(log.getIdempotencyKey(), log.getId());
        return log;
    }

    @Override public List<TransactionLog> findByAccountId(String accountId) {
        return byId.values().stream()
                .filter(t -> t.getFromAccountId().equals(accountId) || t.getToAccountId().equals(accountId))
                .sorted(Comparator.comparing(TransactionLog::getCreatedOn).reversed())
                .collect(Collectors.toList());
    }

    @Override public Optional<TransactionLog> findByIdempotencyKey(String key) {
        String id = byIdempotency.get(key);
        return id == null ? Optional.empty() : Optional.ofNullable(byId.get(id));
    }

    @Override public List<TransactionLog> findAll() {
        return new ArrayList<>(byId.values());
    }
}