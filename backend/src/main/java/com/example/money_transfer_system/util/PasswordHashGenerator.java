package com.example.money_transfer_system.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class to generate BCrypt password hashes for seed data.
 * Run this class to generate hashes for your seed-data.sql file.
 */
public class PasswordHashGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        System.out.println("=== Password Hash Generator ===\n");
        
        // Generate hash for admin123
        String adminPassword = "admin123";
        String adminHash = encoder.encode(adminPassword);
        System.out.println("Password: " + adminPassword);
        System.out.println("BCrypt Hash: " + adminHash);
        System.out.println();
        
        // Generate hash for user123
        String userPassword = "user123";
        String userHash = encoder.encode(userPassword);
        System.out.println("Password: " + userPassword);
        System.out.println("BCrypt Hash: " + userHash);
        System.out.println();
        
        System.out.println("Copy these hashes to your database/seed-data.sql file");
        System.out.println("Replace the placeholder hashes with the generated ones above.");
    }
}
