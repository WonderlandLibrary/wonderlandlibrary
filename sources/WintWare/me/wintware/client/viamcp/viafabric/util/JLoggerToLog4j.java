/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.Logger
 */
package me.wintware.client.viamcp.viafabric.util;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.apache.logging.log4j.Logger;

public class JLoggerToLog4j
extends java.util.logging.Logger {
    private final Logger base;

    public JLoggerToLog4j(Logger logger) {
        super("logger", null);
        this.base = logger;
    }

    @Override
    public void log(LogRecord record) {
        this.log(record.getLevel(), record.getMessage());
    }

    @Override
    public void log(Level level, String msg) {
    }

    @Override
    public void log(Level level, String msg, Object param1) {
    }

    @Override
    public void log(Level level, String msg, Object[] params) {
        this.log(level, MessageFormat.format(msg, params));
    }

    @Override
    public void log(Level level, String msg, Throwable params) {
    }
}

