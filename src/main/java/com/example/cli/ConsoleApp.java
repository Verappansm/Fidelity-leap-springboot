package com.example.cli;

import com.example.dto.AccountRequest;
import com.example.dto.TransferRequest;
import com.example.service.AccountService;
import com.example.service.TransferService;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleApp {
    private final AccountService accountService;
    private final TransferService transferService;
    private final Scanner sc = new Scanner(System.in);

    public ConsoleApp(AccountService accountService, TransferService transferService) {
        this.accountService = accountService;
        this.transferService = transferService;
    }

    public void run() {
        while (true) {
            System.out.println("""
                    
                    === Money Transfer System ===
                    1. Create Account
                    2. View Account
                    3. Check Balance
                    4. Transfer Funds
                    5. Transaction History
                    0. Exit
                    """);
            System.out.print("Choose: ");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> createAccount();
                    case "2" -> viewAccount();
                    case "3" -> balance();
                    case "4" -> transfer();
                    case "5" -> history();
                    case "0" -> { System.out.println("Bye!"); return; }
                    default -> System.out.println("Invalid option");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    private void createAccount() {
        System.out.print("Holder name: ");
        String name = sc.nextLine().trim();
        System.out.print("Opening balance: ");
        BigDecimal bal = new BigDecimal(sc.nextLine().trim());
        var resp = accountService.create(new AccountRequest(name, bal));
        System.out.printf("Account created: %s (%s) balance=%s status=%s%n",
                resp.id(), resp.holderName(), resp.balance(), resp.status());
    }

    private void viewAccount() {
        System.out.print("Account ID: ");
        String id = sc.nextLine().trim();
        var a = accountService.getView(id);
        System.out.printf("Account: %s | %s | balance=%s | status=%s%n",
                a.id(), a.holderName(), a.balance(), a.status());
    }

    private void balance() {
        System.out.print("Account ID: ");
        String id = sc.nextLine().trim();
        var a = accountService.getView(id);
        System.out.printf("Balance for %s is %s%n", id, a.balance());
    }

    private void transfer() {
        System.out.print("From Account ID: ");
        String from = sc.nextLine().trim();
        System.out.print("To Account ID: ");
        String to = sc.nextLine().trim();
        System.out.print("Amount: ");
        BigDecimal amount = new BigDecimal(sc.nextLine().trim());
        System.out.print("Idempotency Key (any unique string): ");
        String key = sc.nextLine().trim();

        var response = transferService.transfer(new TransferRequest(from, to, amount, key));
        System.out.printf("Transfer %s | txn=%s | %s%n", response.status(), response.transactionId(), response.message());
    }

    private void history() {
        System.out.print("Account ID: ");
        String id = sc.nextLine().trim();
        var lines = accountService.getTransactions(id);
        if (lines.isEmpty()) System.out.println("No transactions.");
        else lines.forEach(System.out::println);
    }
}