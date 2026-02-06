package org.example;

import org.example.entity.Account;
import org.example.enums.AccountStatus;
import org.example.exception.InsufficientBalanceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void testDebit_Success() {
        Account acc = new Account("1", "Alice", 100.0, AccountStatus.ACTIVE);
        acc.debit(50.0);
        assertEquals(50.0, acc.getBalance());
    }

    @Test
    void testDebit_InsufficientBalance() {
        Account acc = new Account("1", "Alice", 10.0, AccountStatus.ACTIVE);
        assertThrows(InsufficientBalanceException.class, () -> acc.debit(50.0));
    }

    @Test
    void testCredit_Success() {
        Account acc = new Account("1", "Alice", 100.0, AccountStatus.ACTIVE);
        acc.credit(50.0);
        assertEquals(150.0, acc.getBalance());
    }
}
