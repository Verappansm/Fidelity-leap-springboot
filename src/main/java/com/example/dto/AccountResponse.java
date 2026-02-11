package com.example.dto;

import com.example.enums.AccountStatus;
import com.example.enums.AccountType;

import java.math.BigDecimal;

public record AccountResponse(
        Long id,
        String holderName,
        AccountStatus status,
        AccountType accountType,
        BigDecimal balance
) {
}
