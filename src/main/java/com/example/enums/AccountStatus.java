package com.example.enums;

public enum AccountStatus {
    ACTIVE,
    LOCKED,
    CLOSED;

    public String getLabel() {
        return switch (this) {
            case ACTIVE -> "Active";
            case LOCKED -> "Locked";
            case CLOSED -> "Closed";
        };
    }
}
