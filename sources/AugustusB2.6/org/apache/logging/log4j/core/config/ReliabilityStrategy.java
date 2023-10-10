// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.util.Supplier;

public interface ReliabilityStrategy
{
    void log(final Supplier<LoggerConfig> reconfigured, final String loggerName, final String fqcn, final Marker marker, final Level level, final Message data, final Throwable t);
    
    void log(final Supplier<LoggerConfig> reconfigured, final LogEvent event);
    
    LoggerConfig getActiveLoggerConfig(final Supplier<LoggerConfig> next);
    
    void afterLogEvent();
    
    void beforeStopAppenders();
    
    void beforeStopConfiguration(final Configuration configuration);
}
