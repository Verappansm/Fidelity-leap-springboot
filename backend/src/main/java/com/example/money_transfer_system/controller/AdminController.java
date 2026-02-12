package com.example.money_transfer_system.controller;

import com.example.money_transfer_system.dto.DepositRequest;
import com.example.money_transfer_system.entity.Account;
import com.example.money_transfer_system.entity.TransactionLog;
import com.example.money_transfer_system.service.AccountService;
import com.example.money_transfer_system.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AccountService accountService;
    private final TransferService transferService;

    @GetMapping("/accounts/pending")
    public ResponseEntity<List<Account>> getPendingAccounts() {
        List<Account> pendingAccounts = accountService.getPendingAccounts();
        return ResponseEntity.ok(pendingAccounts);
    }

    @PostMapping("/accounts/{id}/approve")
    public ResponseEntity<Map<String, Object>> approveAccount(@PathVariable Long id) {
        Account account = accountService.approveAccount(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Account approved successfully");
        response.put("accountId", account.getId());
        response.put("email", account.getEmail());
        response.put("status", account.getStatus());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/accounts/{id}/reject")
    public ResponseEntity<Map<String, Object>> rejectAccount(@PathVariable Long id) {
        Account account = accountService.rejectAccount(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Account rejected");
        response.put("accountId", account.getId());
        response.put("status", account.getStatus());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/accounts/deposit")
    public ResponseEntity<Map<String, Object>> deposit(@Valid @RequestBody DepositRequest request) {
        accountService.deposit(request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Deposit successful");
        response.put("accountId", request.getAccountId());
        response.put("amount", request.getAmount());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionLog>> getAllTransactions() {
        List<TransactionLog> transactions = transferService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
}
