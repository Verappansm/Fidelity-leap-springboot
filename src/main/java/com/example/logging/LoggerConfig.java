package com.example.logging;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.*;

public final class LoggerConfig {
    private static boolean configured = false;

    private LoggerConfig() { }

    public static synchronized void configure() {
        if (configured) return;
        Logger root = Logger.getLogger("");
        Arrays.stream(root.getHandlers()).forEach(root::removeHandler);

        // Console
        ConsoleHandler console = new ConsoleHandler();
        console.setLevel(Level.INFO);
        console.setFormatter(new SimpleFormatter());
        root.addHandler(console);

        // File
        try {
            FileHandler file = new FileHandler("app.log", true);
            file.setLevel(Level.INFO);
            file.setFormatter(new SimpleFormatter());
            root.addHandler(file);
        } catch (IOException e) {
            root.log(Level.SEVERE, "Failed to create log file handler", e);
        }

        root.setLevel(Level.INFO);
        configured = true;
    }
}
