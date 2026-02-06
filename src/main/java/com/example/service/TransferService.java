package com.example.service;

import com.example.dto.TransferRequest;
import com.example.dto.TransferResponse;
import com.example.entity.Account;
import com.example.enums.TransactionStatus;
import com.example.exception.*;

public class TransferService {

    public TransferResponse transfer(TransferRequest request, Account from, Account to) {
        if (!from.isActive()) throw new AccountNotActiveException(from.getId());
        if (!to.isActive()) throw new AccountNotActiveException(to.getId());

        try {
            from.debit(request.amount());
            to.credit(request.amount());
            return new TransferResponse("txn123", TransactionStatus.SUCCESS, null);
        } catch (InsufficientBalanceException e) {
            return new TransferResponse("txn123", TransactionStatus.FAILED, e.getMessage());
        }
    }
}
