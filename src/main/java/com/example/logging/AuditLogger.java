package com.example.logging;

import java.util.logging.Logger;

public final class AuditLogger {
    private static final Logger LOG = Logger.getLogger("AUDIT");

    private AuditLogger() {}

    public static void info(String msg) {
        LOG.info(msg);
    }

    public static void error(String msg, Throwable t) {
        LOG.severe(msg + " | ex=" + t.getMessage());
    }
}