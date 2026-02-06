package com.example.repo;

import com.example.entity.Account;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<String, Account> store = new ConcurrentHashMap<>();

    @Override public Account save(Account account) {
        store.put(account.getId(), account);
        return account;
    }

    @Override public Optional<Account> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override public boolean existsById(String id) {
        return store.containsKey(id);
    }

    @Override public Collection<Account> findAll() {
        return Collections.unmodifiableCollection(store.values());
    }
}
