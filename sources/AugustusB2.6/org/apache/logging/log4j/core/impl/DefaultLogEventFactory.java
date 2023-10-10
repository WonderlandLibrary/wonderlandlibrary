// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Property;
import java.util.List;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;

public class DefaultLogEventFactory implements LogEventFactory, LocationAwareLogEventFactory
{
    private static final DefaultLogEventFactory instance;
    
    public static DefaultLogEventFactory getInstance() {
        return DefaultLogEventFactory.instance;
    }
    
    @Override
    public LogEvent createEvent(final String loggerName, final Marker marker, final String fqcn, final Level level, final Message data, final List<Property> properties, final Throwable t) {
        return new Log4jLogEvent(loggerName, marker, fqcn, level, data, properties, t);
    }
    
    @Override
    public LogEvent createEvent(final String loggerName, final Marker marker, final String fqcn, final StackTraceElement location, final Level level, final Message data, final List<Property> properties, final Throwable t) {
        return new Log4jLogEvent(loggerName, marker, fqcn, location, level, data, properties, t);
    }
    
    static {
        instance = new DefaultLogEventFactory();
    }
}
