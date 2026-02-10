package com.example;

import com.example.enums.AccountStatus;
import com.example.enums.AccountType;
import com.example.dto.TransferRequest;
import com.example.dto.TransferResponse;
import com.example.entity.Account;
import com.example.exception.InsufficientBalanceException;
import com.example.repository.AccountRepository;
import com.example.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class MoneyTransferSystemApplication implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final TransferService transferService;

    public static void main(String[] args) {
        SpringApplication.run(MoneyTransferSystemApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("=".repeat(60));
        log.info("  MONEY TRANSFER SYSTEM — MODULE 1 DEMO");
        log.info("=".repeat(60));

        Account alice = accountRepository.save(
                new Account(null, "Alice", AccountStatus.ACTIVE, AccountType.SAVINGS, new BigDecimal("50000.00"), null, null, null));

        Account bob = accountRepository.save(
                new Account(null, "Bob", AccountStatus.ACTIVE, AccountType.STUDENT, new BigDecimal("10000.00"), null, null, null));

        log.info("Created Account — Alice (ID: {}, Balance: {}, Type: {})",
                alice.getId(), alice.getBalance(), alice.getAccountType());
        log.info("Created Account — Bob   (ID: {}, Balance: {}, Type: {})",
                bob.getId(), bob.getBalance(), bob.getAccountType());

        log.info("-".repeat(60));
        log.info("DEMO 1: Successful transfer (Alice -> Bob, 5000)");
        log.info("-".repeat(60));

        TransferResponse tx1 = transferService.transfer(
                new TransferRequest(alice.getId(), bob.getId(), new BigDecimal("5000.00"), "TXN-001"));

        alice = accountRepository.findById(alice.getId()).orElseThrow();
        bob = accountRepository.findById(bob.getId()).orElseThrow();

        log.info("Transfer SUCCESS — TX ID: {}", tx1.getTransactionId());
        log.info("Alice Balance: {}", alice.getBalance());
        log.info("Bob   Balance: {}", bob.getBalance());

        log.info("-".repeat(60));
        log.info("DEMO 2: Duplicate idempotencyKey (TXN-001 again)");
        log.info("-".repeat(60));

        TransferResponse tx2 = transferService.transfer(
                new TransferRequest(alice.getId(), bob.getId(), new BigDecimal("5000.00"), "TXN-001"));

        alice = accountRepository.findById(alice.getId()).orElseThrow();
        bob = accountRepository.findById(bob.getId()).orElseThrow();

        log.info("Idempotency handled — returned existing TX ID: {}", tx2.getTransactionId());
        log.info("Same TX? {}", tx1.getTransactionId().equals(tx2.getTransactionId()));
        log.info("Alice Balance (unchanged): {}", alice.getBalance());
        log.info("Bob   Balance (unchanged): {}", bob.getBalance());

        log.info("-".repeat(60));
        log.info("DEMO 3: Transfer violating minimum balance rule (SAVINGS min=5000)");
        log.info("-".repeat(60));

        try {
            transferService.transfer(
                    new TransferRequest(alice.getId(), bob.getId(), new BigDecimal("41000.00"), "TXN-002"));
        } catch (InsufficientBalanceException ex) {
            log.warn("Expected exception caught: {}", ex.getMessage());
        }

        alice = accountRepository.findById(alice.getId()).orElseThrow();
        log.info("Alice Balance (unchanged after failed transfer): {}", alice.getBalance());

        log.info("=".repeat(60));
        log.info("  DEMO COMPLETE");
        log.info("=".repeat(60));
    }
}
