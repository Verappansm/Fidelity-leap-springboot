package com.example;

import com.example.dto.TransferRequest;
import com.example.entity.Account;
import com.example.enums.AccountStatus;
import com.example.service.TransferService;

public class Main {
    public static void main(String[] args) {
        Account from = new Account("1", "Alice", 100.0, AccountStatus.ACTIVE);
        Account to = new Account("2", "Bob", 50.0, AccountStatus.ACTIVE);

        TransferService service = new TransferService();
        TransferRequest req = new TransferRequest("1", "2", 30.0, "key123");

        var response = service.transfer(req, from, to);
        System.out.println(response);
    }
}


// idempotency key should be randomly generated and all details should be stored in the DB

// duplicate transaction - idempotency key for a transaction to ensure no duplicate transfers - so every time a transfer is recorder unique key i
// generated (assume keeping it increasing i.e i++).
// if transfer fails next time same transfer will have a new key. At the time of debiting if that idempotency key status comes as failure then remove that key, this is to
// ensure that in the queue there is no other