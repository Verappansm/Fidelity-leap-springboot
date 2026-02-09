-- Money Transfer System - Database Schema
-- MySQL 8.x compatible

CREATE DATABASE IF NOT EXISTS money_transfer_db;
USE money_transfer_db;

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS transaction_logs;
DROP TABLE IF EXISTS accounts;

-- Accounts Table
CREATE TABLE accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    holder_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    status ENUM('ACTIVE', 'LOCKED', 'CLOSED') NOT NULL DEFAULT 'LOCKED',
    approved BOOLEAN NOT NULL DEFAULT FALSE,
    role ENUM('ROLE_USER', 'ROLE_ADMIN') NOT NULL DEFAULT 'ROLE_USER',
    min_balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    version INT NOT NULL DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_status (status),
    INDEX idx_approved (approved)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Transaction Logs Table
CREATE TABLE transaction_logs (
    id VARCHAR(36) PRIMARY KEY,
    from_account_id BIGINT,
    to_account_id BIGINT,
    amount DECIMAL(15, 2) NOT NULL,
    transaction_type ENUM('DEBIT', 'CREDIT', 'DEPOSIT') NOT NULL,
    status ENUM('SUCCESS', 'FAILED') NOT NULL DEFAULT 'SUCCESS',
    failure_reason VARCHAR(500),
    idempotency_key VARCHAR(255) UNIQUE,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_from_account (from_account_id),
    INDEX idx_to_account (to_account_id),
    INDEX idx_idempotency (idempotency_key),
    INDEX idx_created_on (created_on),
    FOREIGN KEY (from_account_id) REFERENCES accounts(id) ON DELETE SET NULL,
    FOREIGN KEY (to_account_id) REFERENCES accounts(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
