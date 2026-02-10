package com.example;

import com.example.enums.AccountStatus;
import com.example.enums.AccountType;
import com.example.enums.TransactionStatus;
import com.example.dto.TransferRequest;
import com.example.dto.TransferResponse;
import com.example.entity.Account;
import com.example.exception.InsufficientBalanceException;
import com.example.exception.InvalidTransferException;
import com.example.repository.AccountRepository;
import com.example.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransferServiceTest {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountRepository accountRepository;

    private Account savings;
    private Account student;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();

        savings = accountRepository.save(
                new Account(null, "Alice", AccountStatus.ACTIVE, AccountType.SAVINGS, new BigDecimal("50000.00"), null, null, null));

        student = accountRepository.save(
                new Account(null, "Bob", AccountStatus.ACTIVE, AccountType.STUDENT, new BigDecimal("10000.00"), null, null, null));
    }

    @Test
    @DisplayName("Successful transfer debits source and credits destination")
    void testSuccessfulTransfer() {
        TransferResponse response = transferService.transfer(
                new TransferRequest(savings.getId(), student.getId(), new BigDecimal("5000.00"), "IDEM-001"));

        assertNotNull(response.getTransactionId());
        assertEquals(TransactionStatus.SUCCESS, response.getStatus());

        Account updatedSavings = accountRepository.findById(savings.getId()).orElseThrow();
        Account updatedStudent = accountRepository.findById(student.getId()).orElseThrow();

        assertEquals(0, new BigDecimal("45000.00").compareTo(updatedSavings.getBalance()));
        assertEquals(0, new BigDecimal("15000.00").compareTo(updatedStudent.getBalance()));
    }

    @Test
    @DisplayName("Duplicate idempotencyKey returns existing transaction without re-executing")
    void testIdempotency() {
        TransferResponse r1 = transferService.transfer(
                new TransferRequest(savings.getId(), student.getId(), new BigDecimal("1000.00"), "IDEM-DUP"));

        TransferResponse r2 = transferService.transfer(
                new TransferRequest(savings.getId(), student.getId(), new BigDecimal("1000.00"), "IDEM-DUP"));

        assertEquals(r1.getTransactionId(), r2.getTransactionId());

        Account updatedSavings = accountRepository.findById(savings.getId()).orElseThrow();
        assertEquals(0, new BigDecimal("49000.00").compareTo(updatedSavings.getBalance()));
    }

    @Test
    @DisplayName("Transfer below minimum balance for SAVINGS account is rejected")
    void testMinimumBalanceRule() {
        assertThrows(InsufficientBalanceException.class, () ->
                transferService.transfer(
                        new TransferRequest(savings.getId(), student.getId(), new BigDecimal("46000.00"), "IDEM-MIN")));

        Account updatedSavings = accountRepository.findById(savings.getId()).orElseThrow();
        assertEquals(0, new BigDecimal("50000.00").compareTo(updatedSavings.getBalance()));
    }

    @Test
    @DisplayName("Transfer to same account is rejected")
    void testSameAccountTransfer() {
        assertThrows(InvalidTransferException.class, () ->
                transferService.transfer(
                        new TransferRequest(savings.getId(), savings.getId(), new BigDecimal("100.00"), "IDEM-SAME")));
    }

    @Test
    @DisplayName("STUDENT account allows balance to reach zero")
    void testStudentMinimumBalanceZero() {
        TransferResponse response = transferService.transfer(
                new TransferRequest(student.getId(), savings.getId(), new BigDecimal("10000.00"), "IDEM-STU"));

        assertEquals(TransactionStatus.SUCCESS, response.getStatus());

        Account updatedStudent = accountRepository.findById(student.getId()).orElseThrow();
        assertEquals(0, BigDecimal.ZERO.compareTo(updatedStudent.getBalance()));
    }

    @Test
    @DisplayName("Optimistic locking version field is present on Account")
    void testVersionFieldExists() {
        Account account = accountRepository.findById(savings.getId()).orElseThrow();
        assertNotNull(account.getVersion());
    }
}
