package koks.api.utils;

import koks.Koks;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */
public class Logger {

    private static Logger logger;

    public void log(String text) {
        System.out.println("[" + Koks.name + "] " + text);
    }

    public static Logger getInstance() {
        if(logger == null) {
            logger = new Logger();
        }
        return logger;
    }
}
