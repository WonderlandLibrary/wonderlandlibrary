/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.db;

import org.apache.logging.log4j.core.appender.AppenderLoggingException;

public class DbAppenderLoggingException
extends AppenderLoggingException {
    private static final long serialVersionUID = 1L;

    public DbAppenderLoggingException(String format, Object ... args) {
        super(format, args);
    }

    public DbAppenderLoggingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbAppenderLoggingException(Throwable cause, String format, Object ... args) {
        super(cause, format, args);
    }
}

