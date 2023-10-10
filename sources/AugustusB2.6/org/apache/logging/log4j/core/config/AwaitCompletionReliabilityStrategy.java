// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.util.Supplier;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AwaitCompletionReliabilityStrategy implements ReliabilityStrategy, LocationAwareReliabilityStrategy
{
    private static final int MAX_RETRIES = 3;
    private final AtomicInteger counter;
    private final AtomicBoolean shutdown;
    private final Lock shutdownLock;
    private final Condition noLogEvents;
    private final LoggerConfig loggerConfig;
    
    public AwaitCompletionReliabilityStrategy(final LoggerConfig loggerConfig) {
        this.counter = new AtomicInteger();
        this.shutdown = new AtomicBoolean(false);
        this.shutdownLock = new ReentrantLock();
        this.noLogEvents = this.shutdownLock.newCondition();
        this.loggerConfig = Objects.requireNonNull(loggerConfig, "loggerConfig is null");
    }
    
    @Override
    public void log(final Supplier<LoggerConfig> reconfigured, final String loggerName, final String fqcn, final Marker marker, final Level level, final Message data, final Throwable t) {
        final LoggerConfig config = this.getActiveLoggerConfig(reconfigured);
        try {
            config.log(loggerName, fqcn, marker, level, data, t);
        }
        finally {
            config.getReliabilityStrategy().afterLogEvent();
        }
    }
    
    @Override
    public void log(final Supplier<LoggerConfig> reconfigured, final String loggerName, final String fqcn, final StackTraceElement location, final Marker marker, final Level level, final Message data, final Throwable t) {
        final LoggerConfig config = this.getActiveLoggerConfig(reconfigured);
        try {
            config.log(loggerName, fqcn, location, marker, level, data, t);
        }
        finally {
            config.getReliabilityStrategy().afterLogEvent();
        }
    }
    
    @Override
    public void log(final Supplier<LoggerConfig> reconfigured, final LogEvent event) {
        final LoggerConfig config = this.getActiveLoggerConfig(reconfigured);
        try {
            config.log(event);
        }
        finally {
            config.getReliabilityStrategy().afterLogEvent();
        }
    }
    
    @Override
    public LoggerConfig getActiveLoggerConfig(final Supplier<LoggerConfig> next) {
        LoggerConfig result = this.loggerConfig;
        if (!this.beforeLogEvent()) {
            result = next.get();
            return (result == this.loggerConfig) ? result : result.getReliabilityStrategy().getActiveLoggerConfig(next);
        }
        return result;
    }
    
    private boolean beforeLogEvent() {
        return this.counter.incrementAndGet() > 0;
    }
    
    @Override
    public void afterLogEvent() {
        if (this.counter.decrementAndGet() == 0 && this.shutdown.get()) {
            this.signalCompletionIfShutdown();
        }
    }
    
    private void signalCompletionIfShutdown() {
        final Lock lock = this.shutdownLock;
        lock.lock();
        try {
            this.noLogEvents.signalAll();
        }
        finally {
            lock.unlock();
        }
    }
    
    @Override
    public void beforeStopAppenders() {
        this.waitForCompletion();
    }
    
    private void waitForCompletion() {
        this.shutdownLock.lock();
        try {
            if (this.shutdown.compareAndSet(false, true)) {
                int retries = 0;
                while (!this.counter.compareAndSet(0, Integer.MIN_VALUE)) {
                    if (this.counter.get() < 0) {
                        return;
                    }
                    try {
                        this.noLogEvents.await(retries + 1, TimeUnit.SECONDS);
                    }
                    catch (InterruptedException ie) {
                        if (++retries > 3) {
                            break;
                        }
                        continue;
                    }
                }
            }
        }
        finally {
            this.shutdownLock.unlock();
        }
    }
    
    @Override
    public void beforeStopConfiguration(final Configuration configuration) {
    }
}
