// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db;

import org.apache.logging.log4j.core.appender.AppenderLoggingException;

public class DbAppenderLoggingException extends AppenderLoggingException
{
    private static final long serialVersionUID = 1L;
    
    public DbAppenderLoggingException(final String format, final Object... args) {
        super(format, args);
    }
    
    public DbAppenderLoggingException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public DbAppenderLoggingException(final Throwable cause, final String format, final Object... args) {
        super(cause, format, args);
    }
}
