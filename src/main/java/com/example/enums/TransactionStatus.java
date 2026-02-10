package com.example.enums;

public enum TransactionStatus {
    SUCCESS,
    FAILED;

    public String getLabel() {
        return switch (this) {
            case SUCCESS -> "Successful";
            case FAILED -> "Failed";
        };
    }
}
