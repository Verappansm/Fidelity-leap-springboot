package com.example;

import com.example.logging.LoggerConfig;
import com.example.repo.AccountRepository;
import com.example.repo.InMemoryAccountRepository;
import com.example.repo.InMemoryTransactionLogRepository;
import com.example.repo.TransactionLogRepository;
import com.example.service.AccountService;
import com.example.service.TransferService;
import com.example.cli.ConsoleApp;

public class Main {
    public static void main(String[] args) {
        LoggerConfig.configure();

        AccountRepository accountRepo = new InMemoryAccountRepository();
        TransactionLogRepository logRepo = new InMemoryTransactionLogRepository();

        AccountService accountService = new AccountService(accountRepo, logRepo);
        TransferService transferService = new TransferService(accountRepo, logRepo);

        new ConsoleApp(accountService, transferService).run();
    }
}
