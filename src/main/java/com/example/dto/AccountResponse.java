package com.example.dto;

import com.example.enums.AccountStatus;

import java.math.BigDecimal;

public record AccountResponse(String id, String holderName, BigDecimal balance, AccountStatus status) { }