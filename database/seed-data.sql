-- Money Transfer System - Seed Data
-- Initial admin account and test users

USE money_transfer_db;

-- IMPORTANT: Before running this script, generate BCrypt password hashes
-- Run: mvn exec:java -Dexec.mainClass="com.example.money_transfer_system.util.PasswordHashGenerator"
-- Then replace the password_hash values below with the generated hashes

-- Insert Admin Account
-- Email: admin@system.com
-- Password: admin123
-- TODO: Replace with actual BCrypt hash from PasswordHashGenerator
INSERT INTO accounts (holder_name, email, password_hash, balance, status, approved, role, min_balance, version)
VALUES (
    'System Administrator',
    'admin@system.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    0.00,
    'ACTIVE',
    TRUE,
    'ROLE_ADMIN',
    0.00,
    0
);

-- Insert Test User 1 (Approved, with balance)
-- Email: john@example.com
-- Password: user123
-- TODO: Replace with actual BCrypt hash from PasswordHashGenerator
INSERT INTO accounts (holder_name, email, password_hash, balance, status, approved, role, min_balance, version)
VALUES (
    'John Doe',
    'john@example.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    5000.00,
    'ACTIVE',
    TRUE,
    'ROLE_USER',
    1000.00,
    0
);

-- Insert Test User 2 (Approved, with balance)
-- Email: jane@example.com
-- Password: user123
-- TODO: Replace with actual BCrypt hash from PasswordHashGenerator
INSERT INTO accounts (holder_name, email, password_hash, balance, status, approved, role, min_balance, version)
VALUES (
    'Jane Smith',
    'jane@example.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    3000.00,
    'ACTIVE',
    TRUE,
    'ROLE_USER',
    1000.00,
    0
);

-- Insert Test User 3 (Pending Approval)
-- Email: bob@example.com
-- Password: user123
-- TODO: Replace with actual BCrypt hash from PasswordHashGenerator
INSERT INTO accounts (holder_name, email, password_hash, balance, status, approved, role, min_balance, version)
VALUES (
    'Bob Johnson',
    'bob@example.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    0.00,
    'LOCKED',
    FALSE,
    'ROLE_USER',
    1000.00,
    0
);

-- Alternative: Register users through the application at http://localhost:8080/register.html
-- This will automatically generate proper BCrypt hashes

