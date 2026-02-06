package com.example.dto;

import java.math.BigDecimal;

public record AccountRequest(String holderName, BigDecimal openingBalance) {
    public AccountRequest {
        if (holderName == null || holderName.isBlank())
            throw new IllegalArgumentException("holderName must not be blank");
        if (openingBalance == null || openingBalance.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("openingBalance must be >= 0");
    }
}
