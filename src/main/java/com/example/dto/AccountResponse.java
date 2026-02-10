package com.example.dto;

import com.example.enums.AccountStatus;
import com.example.enums.AccountType;
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
