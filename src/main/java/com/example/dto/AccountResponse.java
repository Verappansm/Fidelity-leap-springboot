package com.example.dto;

import com.example.config.AccountStatus;
import com.example.config.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

    private Long id;
    private String holderName;
    private AccountStatus status;
    private AccountType accountType;
    private BigDecimal balance;
}
