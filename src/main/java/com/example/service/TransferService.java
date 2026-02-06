package com.example.service;

import com.example.exception.AccountNotActiveException;
import com.example.exception.AccountNotFoundException;
import com.example.logging.AuditLogger;
import com.example.dto.TransferRequest;
import com.example.dto.TransferResponse;
import com.example.entity.Account;
import com.example.entity.TransactionLog;
import com.example.enums.TransactionStatus;
import com.example.repo.AccountRepository;
import com.example.repo.TransactionLogRepository;
import com.example.util.IdGenerator;

public class TransferService {
    private final AccountRepository accountRepo;
    private final TransactionLogRepository logRepo;

    public TransferService(AccountRepository accountRepo, TransactionLogRepository logRepo) {
        this.accountRepo = accountRepo;
        this.logRepo = logRepo;
    }

    public TransferResponse transfer(TransferRequest request) {
        // Idempotency check
        var existing = logRepo.findByIdempotencyKey(request.idempotencyKey());
        if (existing.isPresent()) {
            var t = existing.get();
            return new TransferResponse(t.getId(), t.getStatus(),
                    t.getFailureReason() == null ? "Duplicate request: returning previous success" : t.getFailureReason(),
                    t.getFromAccountId(), t.getToAccountId(), t.getAmount());
        }

        Account from = accountRepo.findById(request.fromAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.fromAccountId()));
        Account to = accountRepo.findById(request.toAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.toAccountId()));

        if (!from.isActive()) throw new AccountNotActiveException(from.getId());
        if (!to.isActive()) throw new AccountNotActiveException(to.getId());

        String txnId = IdGenerator.newTransactionId();
        var log = new TransactionLog(txnId, from.getId(), to.getId(), request.amount(), request.idempotencyKey());

        try {
            from.debit(request.amount());
            to.credit(request.amount());
            accountRepo.save(from);
            accountRepo.save(to);

            log.setStatus(TransactionStatus.SUCCESS);
            logRepo.save(log);

            String msg = String.format("Transfer SUCCESS %s: %s -> %s amount=%s",
                    txnId, from.getId(), to.getId(), request.amount());
            AuditLogger.info(msg);

            return new TransferResponse(txnId, TransactionStatus.SUCCESS, "Transfer completed",
                    from.getId(), to.getId(), request.amount());
        } catch (RuntimeException ex) {
            log.setStatus(TransactionStatus.FAILED);
            log.setFailureReason(ex.getMessage());
            logRepo.save(log);

            String msg = String.format("Transfer FAILED %s: %s -> %s amount=%s reason=%s",
                    txnId, from.getId(), to.getId(), request.amount(), ex.getMessage());
            AuditLogger.error(msg, ex);

            return new TransferResponse(txnId, TransactionStatus.FAILED, ex.getMessage(),
                    from.getId(), to.getId(), request.amount());
        }
    }
}