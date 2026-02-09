package com.example.money_transfer_system.service;

import com.example.money_transfer_system.dto.DepositRequest;
import com.example.money_transfer_system.entity.Account;
import com.example.money_transfer_system.entity.TransactionLog;
import com.example.money_transfer_system.enums.AccountStatus;
import com.example.money_transfer_system.enums.Role;
import com.example.money_transfer_system.enums.TransactionStatus;
import com.example.money_transfer_system.enums.TransactionType;
import com.example.money_transfer_system.exception.AccountNotFoundException;
import com.example.money_transfer_system.exception.InvalidAmountException;
import com.example.money_transfer_system.repository.AccountRepository;
import com.example.money_transfer_system.repository.TransactionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.min-balance}")
    private BigDecimal defaultMinBalance;

    @Transactional
    public Account registerAccount(String holderName, String email, String password) {
        if (accountRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        Account account = new Account();
        account.setHolderName(holderName);
        account.setEmail(email);
        account.setPasswordHash(passwordEncoder.encode(password));
        account.setBalance(BigDecimal.ZERO);
        account.setStatus(AccountStatus.LOCKED);
        account.setApproved(false);
        account.setRole(Role.ROLE_USER);
        account.setMinBalance(defaultMinBalance);

        Account saved = accountRepository.save(account);
        log.info("New account registered: {} (ID: {}), pending approval", email, saved.getId());
        return saved;
    }

    @Transactional
    public Account approveAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));

        account.setApproved(true);
        account.setStatus(AccountStatus.ACTIVE);
        account.setBalance(defaultMinBalance); // Set initial balance to minimum balance

        Account approved = accountRepository.save(account);
        log.info("Account approved: {} (ID: {})", account.getEmail(), accountId);
        return approved;
    }

    @Transactional
    public Account rejectAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));

        account.setStatus(AccountStatus.CLOSED);
        Account rejected = accountRepository.save(account);
        log.info("Account rejected: {} (ID: {})", account.getEmail(), accountId);
        return rejected;
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
    }

    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with email: " + email));
    }

    public List<Account> getPendingAccounts() {
        return accountRepository.findByApprovedFalse();
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional
    public void deposit(DepositRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than zero");
        }

        Account account = getAccountById(request.getAccountId());
        
        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        // Log deposit transaction
        TransactionLog log = new TransactionLog();
        log.setToAccountId(account.getId());
        log.setAmount(request.getAmount());
        log.setTransactionType(TransactionType.DEPOSIT);
        log.setStatus(TransactionStatus.SUCCESS);
        transactionLogRepository.save(log);

        this.log.info("Deposited {} to account ID: {}", request.getAmount(), request.getAccountId());
    }

    public BigDecimal getBalance(Long accountId) {
        Account account = getAccountById(accountId);
        return account.getBalance();
    }
}
