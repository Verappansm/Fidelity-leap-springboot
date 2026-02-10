package com.example.enums;

import java.math.BigDecimal;

public enum AccountType {
    SAVINGS(new BigDecimal("5000")),
    STUDENT(BigDecimal.ZERO);

    private final BigDecimal minimumBalance;

    AccountType(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }
}
