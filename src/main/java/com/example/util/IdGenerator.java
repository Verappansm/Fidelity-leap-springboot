//package org.example.util;
//
//import java.util.UUID;
//
//public final class IdGenerator {
//    private IdGenerator() {}
//
//    public static String newAccountId() { return "A-" + UUID.randomUUID().toString().substring(0, 8); }
//    public static String newTransactionId() { return "T-" + UUID.randomUUID(); }
//}


package com.example.util;

public final class IdGenerator {

    private static int accountCounter = 1;
    private static int transactionCounter = 1;

    private IdGenerator() {}

    public static String newAccountId() {
        return "A-" + (accountCounter++);
    }

    public static String newTransactionId() {
        return "T-" + (transactionCounter++);
    }
}