package com.example.service;

import com.example.exception.AccountNotFoundException;
import com.example.dto.AccountRequest;
import com.example.dto.AccountResponse;
import com.example.entity.Account;
import com.example.enums.AccountStatus;
import com.example.repo.AccountRepository;
import com.example.repo.TransactionLogRepository;
import com.example.util.IdGenerator;

import java.util.List;
import java.util.stream.Collectors;

public class AccountService {
    private final AccountRepository accountRepo;
    private final TransactionLogRepository logRepo;

    public AccountService(AccountRepository accountRepo, TransactionLogRepository logRepo) {
        this.accountRepo = accountRepo;
        this.logRepo = logRepo;
    }

    public AccountResponse create(AccountRequest request) {
        var account = new Account(
                IdGenerator.newAccountId(),
                request.holderName(),
                request.openingBalance(),
                AccountStatus.ACTIVE
        );
        accountRepo.save(account);
        return new AccountResponse(account.getId(), account.getHolderName(), account.getBalance(), account.getStatus());
    }

    public Account get(String id) {
        return accountRepo.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }

    public AccountResponse getView(String id) {
        var a = get(id);
        return new AccountResponse(a.getId(), a.getHolderName(), a.getBalance(), a.getStatus());
    }

    public List<String> getTransactions(String accountId) {
        get(accountId); // existence check
        return logRepo.findByAccountId(accountId).stream()
                .map(t -> String.format("%s | %s -> %s | %s | %s",
                        t.getId(), t.getFromAccountId(), t.getToAccountId(),
                        t.getAmount(), t.getStatus()))
                .collect(Collectors.toList());
    }
}