package com.example.repo;

import com.example.entity.Account;

import java.util.Collection;
import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findById(String id);
    boolean existsById(String id);
    Collection<Account> findAll();
}