// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.velocity.util;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerWrapper extends Logger
{
    private final org.slf4j.Logger base;
    
    public LoggerWrapper(final org.slf4j.Logger logger) {
        super("logger", null);
        this.base = logger;
    }
    
    @Override
    public void log(final LogRecord record) {
        this.log(record.getLevel(), record.getMessage());
    }
    
    @Override
    public void log(final Level level, final String msg) {
        if (level == Level.FINE) {
            this.base.debug(msg);
        }
        else if (level == Level.WARNING) {
            this.base.warn(msg);
        }
        else if (level == Level.SEVERE) {
            this.base.error(msg);
        }
        else if (level == Level.INFO) {
            this.base.info(msg);
        }
        else {
            this.base.trace(msg);
        }
    }
    
    @Override
    public void log(final Level level, final String msg, final Object param1) {
        if (level == Level.FINE) {
            this.base.debug(msg, param1);
        }
        else if (level == Level.WARNING) {
            this.base.warn(msg, param1);
        }
        else if (level == Level.SEVERE) {
            this.base.error(msg, param1);
        }
        else if (level == Level.INFO) {
            this.base.info(msg, param1);
        }
        else {
            this.base.trace(msg, param1);
        }
    }
    
    @Override
    public void log(final Level level, final String msg, final Object[] params) {
        this.log(level, MessageFormat.format(msg, params));
    }
    
    @Override
    public void log(final Level level, final String msg, final Throwable params) {
        if (level == Level.FINE) {
            this.base.debug(msg, params);
        }
        else if (level == Level.WARNING) {
            this.base.warn(msg, params);
        }
        else if (level == Level.SEVERE) {
            this.base.error(msg, params);
        }
        else if (level == Level.INFO) {
            this.base.info(msg, params);
        }
        else {
            this.base.trace(msg, params);
        }
    }
}
