package com.example.enums;

import java.math.BigDecimal;

public enum AccountType {
    SAVINGS,
    STUDENT;

    public BigDecimal getMinimumBalance() {
        return switch (this) {
            case SAVINGS -> new BigDecimal("5000");
            case STUDENT -> BigDecimal.ZERO;
        };
    }
}
