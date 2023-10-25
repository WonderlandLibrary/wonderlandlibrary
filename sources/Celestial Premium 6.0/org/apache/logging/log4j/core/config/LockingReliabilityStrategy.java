/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LocationAwareReliabilityStrategy;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.ReliabilityStrategy;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.Supplier;

public class LockingReliabilityStrategy
implements ReliabilityStrategy,
LocationAwareReliabilityStrategy {
    private final LoggerConfig loggerConfig;
    private final ReadWriteLock reconfigureLock = new ReentrantReadWriteLock();
    private volatile boolean isStopping;

    public LockingReliabilityStrategy(LoggerConfig loggerConfig) {
        this.loggerConfig = Objects.requireNonNull(loggerConfig, "loggerConfig was null");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void log(Supplier<LoggerConfig> reconfigured, String loggerName, String fqcn, Marker marker, Level level, Message data, Throwable t) {
        LoggerConfig config = this.getActiveLoggerConfig(reconfigured);
        try {
            config.log(loggerName, fqcn, marker, level, data, t);
        }
        finally {
            config.getReliabilityStrategy().afterLogEvent();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void log(Supplier<LoggerConfig> reconfigured, String loggerName, String fqcn, StackTraceElement location, Marker marker, Level level, Message data, Throwable t) {
        LoggerConfig config = this.getActiveLoggerConfig(reconfigured);
        try {
            config.log(loggerName, fqcn, location, marker, level, data, t);
        }
        finally {
            config.getReliabilityStrategy().afterLogEvent();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void log(Supplier<LoggerConfig> reconfigured, LogEvent event) {
        LoggerConfig config = this.getActiveLoggerConfig(reconfigured);
        try {
            config.log(event);
        }
        finally {
            config.getReliabilityStrategy().afterLogEvent();
        }
    }

    @Override
    public LoggerConfig getActiveLoggerConfig(Supplier<LoggerConfig> next) {
        LoggerConfig result = this.loggerConfig;
        if (!this.beforeLogEvent()) {
            result = next.get();
            return result == this.loggerConfig ? result : result.getReliabilityStrategy().getActiveLoggerConfig(next);
        }
        return result;
    }

    private boolean beforeLogEvent() {
        this.reconfigureLock.readLock().lock();
        if (this.isStopping) {
            this.reconfigureLock.readLock().unlock();
            return false;
        }
        return true;
    }

    @Override
    public void afterLogEvent() {
        this.reconfigureLock.readLock().unlock();
    }

    @Override
    public void beforeStopAppenders() {
        this.reconfigureLock.writeLock().lock();
        try {
            this.isStopping = true;
        }
        finally {
            this.reconfigureLock.writeLock().unlock();
        }
    }

    @Override
    public void beforeStopConfiguration(Configuration configuration) {
    }
}

