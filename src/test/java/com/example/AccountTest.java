package com.example;

import com.example.enums.AccountStatus;
import com.example.enums.AccountType;
import com.example.entity.Account;
import com.example.exception.InsufficientBalanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account createSavingsAccount(BigDecimal balance) {
        return new Account(1L, "Alice", AccountStatus.ACTIVE, AccountType.SAVINGS, balance, null, null, null);
    }

    private Account createStudentAccount(BigDecimal balance) {
        return new Account(2L, "Bob", AccountStatus.ACTIVE, AccountType.STUDENT, balance, null, null, null);
    }

    @Test
    @DisplayName("debit() subtracts amount when sufficient balance remains above minimum")
    void testDebit_Success() {
        Account account = createSavingsAccount(new BigDecimal("20000.00"));

        account.debit(new BigDecimal("10000.00"));

        assertEquals(0, new BigDecimal("10000.00").compareTo(account.getBalance()));
    }

    @Test
    @DisplayName("debit() throws InsufficientBalanceException when balance drops below minimum")
    void testDebit_InsufficientBalance() {
        Account account = createSavingsAccount(new BigDecimal("6000.00"));

        assertThrows(InsufficientBalanceException.class, () ->
                account.debit(new BigDecimal("2000.00")));

        assertEquals(0, new BigDecimal("6000.00").compareTo(account.getBalance()));
    }

    @Test
    @DisplayName("credit() adds amount to account balance")
    void testCredit_Success() {
        Account account = createStudentAccount(new BigDecimal("5000.00"));

        account.credit(new BigDecimal("3000.00"));

        assertEquals(0, new BigDecimal("8000.00").compareTo(account.getBalance()));
    }

    @Test
    @DisplayName("debit() exception message contains account details")
    void testDebit_ExceptionMessage() {
        Account account = createSavingsAccount(new BigDecimal("6000.00"));

        InsufficientBalanceException ex = assertThrows(
                InsufficientBalanceException.class,
                () -> account.debit(new BigDecimal("2000.00")));

        String expectedMessage = """
                Insufficient balance. Account 1 (SAVINGS) requires minimum balance of 5000. \
                Current: 6000.00, Debit: 2000.00, Resulting: 4000.00""";

        assertEquals(expectedMessage, ex.getMessage());
    }
}
