package com.example.service;

import com.example.enums.AccountStatus;
import com.example.enums.TransactionStatus;
import com.example.dto.TransferRequest;
import com.example.dto.TransferResponse;
import com.example.entity.Account;
import com.example.entity.TransactionLog;
import com.example.exception.AccountNotFoundException;
import com.example.exception.ConcurrencyException;
import com.example.exception.InsufficientBalanceException;
import com.example.exception.InvalidTransferException;
import com.example.repository.AccountRepository;
import com.example.repository.TransactionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;

    @Transactional
    public TransferResponse transfer(TransferRequest request) {
        Optional<TransactionLog> existing = transactionLogRepository.findByIdempotencyKey(request.getIdempotencyKey());
        if (existing.isPresent()) {
            log.info("Duplicate idempotencyKey detected: '{}'. Returning existing transaction.", request.getIdempotencyKey());
            return toResponse(existing.get());
        }

        try {
            if (request.getFromAccountId().equals(request.getToAccountId())) {
                throw new InvalidTransferException("Cannot transfer to the same account.");
            }

            Account fromAccount = accountRepository.findById(request.getFromAccountId())
                    .orElseThrow(() -> new AccountNotFoundException(
                            "Source account not found. ID: " + request.getFromAccountId()));

            Account toAccount = accountRepository.findById(request.getToAccountId())
                    .orElseThrow(() -> new AccountNotFoundException(
                            "Destination account not found. ID: " + request.getToAccountId()));

            if (!fromAccount.isActive()) {
                throw new InvalidTransferException("Source account is not ACTIVE. ID: " + request.getFromAccountId());
            }

            if (!toAccount.isActive()) {
                throw new InvalidTransferException("Destination account is not ACTIVE. ID: " + request.getToAccountId());
            }

            fromAccount.debit(request.getAmount());
            toAccount.credit(request.getAmount());

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            TransactionLog txLog = new TransactionLog(
                    null, request.getFromAccountId(), request.getToAccountId(),
                    request.getAmount(), TransactionStatus.SUCCESS, null,
                    request.getIdempotencyKey(), null);

            return toResponse(transactionLogRepository.save(txLog));

        } catch (ObjectOptimisticLockingFailureException ex) {
            throw new ConcurrencyException(
                    "Concurrent modification detected during transfer. Please retry.", ex);

        } catch (AccountNotFoundException | InvalidTransferException | InsufficientBalanceException ex) {
            log.error("Transfer failed: {}", ex.getMessage());
            throw ex;
        }
    }

    private TransferResponse toResponse(TransactionLog txLog) {
        return new TransferResponse(
                txLog.getId(),
                txLog.getFromAccountId(),
                txLog.getToAccountId(),
                txLog.getAmount(),
                txLog.getStatus(),
                txLog.getFailureReason(),
                txLog.getIdempotencyKey(),
                txLog.getCreatedAt()
        );
    }
}
