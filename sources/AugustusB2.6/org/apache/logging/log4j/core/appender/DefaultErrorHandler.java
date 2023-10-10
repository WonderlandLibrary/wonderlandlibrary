// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Objects;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.ErrorHandler;

public class DefaultErrorHandler implements ErrorHandler
{
    private static final Logger LOGGER;
    private static final int MAX_EXCEPTION_COUNT = 3;
    private static final long EXCEPTION_INTERVAL_NANOS;
    private int exceptionCount;
    private long lastExceptionInstantNanos;
    private final Appender appender;
    
    public DefaultErrorHandler(final Appender appender) {
        this.exceptionCount = 0;
        this.lastExceptionInstantNanos = System.nanoTime() - DefaultErrorHandler.EXCEPTION_INTERVAL_NANOS - 1L;
        this.appender = Objects.requireNonNull(appender, "appender");
    }
    
    @Override
    public void error(final String msg) {
        final boolean allowed = this.acquirePermit();
        if (allowed) {
            DefaultErrorHandler.LOGGER.error(msg);
        }
    }
    
    @Override
    public void error(final String msg, final Throwable error) {
        final boolean allowed = this.acquirePermit();
        if (allowed) {
            DefaultErrorHandler.LOGGER.error(msg, error);
        }
        if (!this.appender.ignoreExceptions() && error != null && !(error instanceof AppenderLoggingException)) {
            throw new AppenderLoggingException(msg, error);
        }
    }
    
    @Override
    public void error(final String msg, final LogEvent event, final Throwable error) {
        final boolean allowed = this.acquirePermit();
        if (allowed) {
            DefaultErrorHandler.LOGGER.error(msg, error);
        }
        if (!this.appender.ignoreExceptions() && error != null && !(error instanceof AppenderLoggingException)) {
            throw new AppenderLoggingException(msg, error);
        }
    }
    
    private boolean acquirePermit() {
        final long currentInstantNanos = System.nanoTime();
        synchronized (this) {
            if (currentInstantNanos - this.lastExceptionInstantNanos > DefaultErrorHandler.EXCEPTION_INTERVAL_NANOS) {
                this.lastExceptionInstantNanos = currentInstantNanos;
                return true;
            }
            if (this.exceptionCount < 3) {
                ++this.exceptionCount;
                this.lastExceptionInstantNanos = currentInstantNanos;
                return true;
            }
            return false;
        }
    }
    
    public Appender getAppender() {
        return this.appender;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        EXCEPTION_INTERVAL_NANOS = TimeUnit.MINUTES.toNanos(5L);
    }
}
