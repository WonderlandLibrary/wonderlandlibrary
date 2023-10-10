// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.util.Supplier;
import java.util.Objects;

public class DefaultReliabilityStrategy implements ReliabilityStrategy, LocationAwareReliabilityStrategy
{
    private final LoggerConfig loggerConfig;
    
    public DefaultReliabilityStrategy(final LoggerConfig loggerConfig) {
        this.loggerConfig = Objects.requireNonNull(loggerConfig, "loggerConfig is null");
    }
    
    @Override
    public void log(final Supplier<LoggerConfig> reconfigured, final String loggerName, final String fqcn, final Marker marker, final Level level, final Message data, final Throwable t) {
        this.loggerConfig.log(loggerName, fqcn, marker, level, data, t);
    }
    
    @Override
    public void log(final Supplier<LoggerConfig> reconfigured, final String loggerName, final String fqcn, final StackTraceElement location, final Marker marker, final Level level, final Message data, final Throwable t) {
        this.loggerConfig.log(loggerName, fqcn, location, marker, level, data, t);
    }
    
    @Override
    public void log(final Supplier<LoggerConfig> reconfigured, final LogEvent event) {
        this.loggerConfig.log(event);
    }
    
    @Override
    public LoggerConfig getActiveLoggerConfig(final Supplier<LoggerConfig> next) {
        return this.loggerConfig;
    }
    
    @Override
    public void afterLogEvent() {
    }
    
    @Override
    public void beforeStopAppenders() {
    }
    
    @Override
    public void beforeStopConfiguration(final Configuration configuration) {
    }
}
