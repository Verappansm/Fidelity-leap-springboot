package com.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(

        @NotNull(message = "Source account ID must not be null")
        Long fromAccountId,

        @NotNull(message = "Destination account ID must not be null")
        Long toAccountId,

        @NotNull(message = "Amount must not be null")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        BigDecimal amount,

        @NotBlank(message = "Idempotency key must not be blank")
        String idempotencyKey
) {
}
