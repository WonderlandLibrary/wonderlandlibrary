// 
// Decompiled by Procyon v0.5.36
// 

package com.googlecode.pngtastic.core;

import java.util.Arrays;
import java.util.List;

public final class Logger
{
    private static final List<String> LOG_LEVELS;
    private final String logLevel;
    
    Logger(final String logLevel) {
        this.logLevel = ((logLevel == null || !Logger.LOG_LEVELS.contains(logLevel.toUpperCase())) ? "NONE" : logLevel.toUpperCase());
    }
    
    public final void debug(final String message, final Object... args) {
        if ("DEBUG".equals(this.logLevel)) {
            System.out.println(String.format(message, args));
        }
    }
    
    public final void info(final String message, final Object... args) {
        if ("DEBUG".equals(this.logLevel) || "INFO".equals(this.logLevel)) {
            System.out.println(String.format(message, args));
        }
    }
    
    public final void error(final String message, final Object... args) {
        if (!"NONE".equals(this.logLevel)) {
            System.out.println(String.format(message, args));
        }
    }
    
    static {
        LOG_LEVELS = Arrays.asList("NONE", "DEBUG", "INFO", "ERROR");
    }
}
