// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.core.impl.LogEventFactory;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.jmx.RingBufferAdmin;

public interface AsyncLoggerConfigDelegate
{
    RingBufferAdmin createRingBufferAdmin(final String contextName, final String loggerConfigName);
    
    EventRoute getEventRoute(final Level level);
    
    void enqueueEvent(final LogEvent event, final AsyncLoggerConfig asyncLoggerConfig);
    
    boolean tryEnqueue(final LogEvent event, final AsyncLoggerConfig asyncLoggerConfig);
    
    void setLogEventFactory(final LogEventFactory logEventFactory);
}
