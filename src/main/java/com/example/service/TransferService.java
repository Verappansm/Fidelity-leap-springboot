package com.example.service;

import com.example.enums.AccountStatus;
import com.example.enums.TransactionStatus;
import com.example.dto.TransferRequest;
import com.example.dto.TransferResponse;
import com.example.entity.Account;
import com.example.entity.TransactionLog;
import com.example.exception.AccountNotFoundException;
import com.example.exception.ConcurrencyException;
import com.example.exception.InvalidTransferException;
import com.example.exception.TransferException;
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
        Optional<TransactionLog> existing = transactionLogRepository.findByIdempotencyKey(request.idempotencyKey());
        if (existing.isPresent()) {
            log.info("Duplicate idempotencyKey detected: '{}'. Returning existing transaction.", request.idempotencyKey());
            return toResponse(existing.get());
        }

        try {
            if (request.fromAccountId().equals(request.toAccountId())) {
                throw new InvalidTransferException("Cannot transfer to the same account.");
            }

            Account fromAccount = accountRepository.findById(request.fromAccountId())
                    .orElseThrow(() -> new AccountNotFoundException(
                            "Source account not found. ID: " + request.fromAccountId()));

            Account toAccount = accountRepository.findById(request.toAccountId())
                    .orElseThrow(() -> new AccountNotFoundException(
                            "Destination account not found. ID: " + request.toAccountId()));

            if (!fromAccount.isActive()) {
                throw new InvalidTransferException("Source account is not ACTIVE. ID: " + request.fromAccountId());
            }

            if (!toAccount.isActive()) {
                throw new InvalidTransferException("Destination account is not ACTIVE. ID: " + request.toAccountId());
            }

            fromAccount.debit(request.amount());
            toAccount.credit(request.amount());

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            TransactionLog txLog = new TransactionLog(
                    null, request.fromAccountId(), request.toAccountId(),
                    request.amount(), TransactionStatus.SUCCESS, null,
                    request.idempotencyKey(), null);

            return toResponse(transactionLogRepository.save(txLog));

        } catch (ObjectOptimisticLockingFailureException ex) {
            throw new ConcurrencyException(
                    "Concurrent modification detected during transfer. Please retry.", ex);

        } catch (TransferException ex) {
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
