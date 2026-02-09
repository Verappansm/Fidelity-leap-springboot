package com.example.money_transfer_system.service;

import com.example.money_transfer_system.dto.TransferRequest;
import com.example.money_transfer_system.dto.TransferResponse;
import com.example.money_transfer_system.entity.Account;
import com.example.money_transfer_system.entity.TransactionLog;
import com.example.money_transfer_system.enums.AccountStatus;
import com.example.money_transfer_system.enums.TransactionStatus;
import com.example.money_transfer_system.enums.TransactionType;
import com.example.money_transfer_system.exception.*;
import com.example.money_transfer_system.repository.AccountRepository;
import com.example.money_transfer_system.repository.TransactionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;

    @Transactional
    public TransferResponse transfer(TransferRequest request) {
        // PDF Rule: Check idempotency
        if (transactionLogRepository.existsByIdempotencyKey(request.getIdempotencyKey())) {
            throw new DuplicateTransferException("Transfer with this idempotency key already exists (TRX-409)");
        }

        // PDF Rule: Validate amount > 0
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero (VAL-422)");
        }

        // PDF Rule: Accounts must be different
        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new InvalidAmountException("Cannot transfer to the same account (VAL-422)");
        }

        // PDF Rule: Source and destination must exist
        Account fromAccount = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Source account not found (ACC-404)"));

        Account toAccount = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Destination account not found (ACC-404)"));

        // PDF Rule: Both accounts must be ACTIVE
        if (fromAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException("Source account is not active (ACC-403)");
        }

        if (toAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException("Destination account is not active (ACC-403)");
        }

        // Additional Rule: Both accounts must be approved
        if (!fromAccount.getApproved()) {
            throw new AccountNotActiveException("Source account is not approved (ACC-403)");
        }

        if (!toAccount.getApproved()) {
            throw new AccountNotActiveException("Destination account is not approved (ACC-403)");
        }

        // PDF Rule: Source balance >= amount
        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in source account (TRX-400)");
        }

        // Additional Rule: Check minimum balance requirement
        BigDecimal balanceAfterTransfer = fromAccount.getBalance().subtract(request.getAmount());
        if (balanceAfterTransfer.compareTo(fromAccount.getMinBalance()) < 0) {
            throw new InsufficientFundsException(
                    "Transfer would violate minimum balance requirement of " + fromAccount.getMinBalance() + " (TRX-400)"
            );
        }

        // Execute transfer: Debit from source, Credit to destination
        fromAccount.setBalance(balanceAfterTransfer);
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

        // Save accounts (optimistic locking will handle concurrent updates)
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        String transactionId = UUID.randomUUID().toString();

        // Log DEBIT transaction
        TransactionLog debitLog = new TransactionLog();
        debitLog.setId(transactionId + "-DEBIT");
        debitLog.setFromAccountId(fromAccount.getId());
        debitLog.setToAccountId(toAccount.getId());
        debitLog.setAmount(request.getAmount());
        debitLog.setTransactionType(TransactionType.DEBIT);
        debitLog.setStatus(TransactionStatus.SUCCESS);
        debitLog.setIdempotencyKey(request.getIdempotencyKey());
        transactionLogRepository.save(debitLog);

        // Log CREDIT transaction
        TransactionLog creditLog = new TransactionLog();
        creditLog.setId(transactionId + "-CREDIT");
        creditLog.setFromAccountId(fromAccount.getId());
        creditLog.setToAccountId(toAccount.getId());
        creditLog.setAmount(request.getAmount());
        creditLog.setTransactionType(TransactionType.CREDIT);
        creditLog.setStatus(TransactionStatus.SUCCESS);
        transactionLogRepository.save(creditLog);

        log.info("Transfer successful: {} from account {} to account {}", 
                request.getAmount(), fromAccount.getId(), toAccount.getId());

        return new TransferResponse(
                transactionId,
                "SUCCESS",
                "Transfer completed successfully",
                fromAccount.getId(),
                toAccount.getId(),
                request.getAmount()
        );
    }

    public List<TransactionLog> getTransactionHistory(Long accountId) {
        return transactionLogRepository.findByAccountId(accountId);
    }

    public List<TransactionLog> getAllTransactions() {
        return transactionLogRepository.findAllByOrderByCreatedOnDesc();
    }
}
