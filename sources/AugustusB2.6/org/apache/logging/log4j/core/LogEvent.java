// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core;

import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.time.Instant;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import java.util.Map;
import java.io.Serializable;

public interface LogEvent extends Serializable
{
    LogEvent toImmutable();
    
    @Deprecated
    Map<String, String> getContextMap();
    
    ReadOnlyStringMap getContextData();
    
    ThreadContext.ContextStack getContextStack();
    
    String getLoggerFqcn();
    
    Level getLevel();
    
    String getLoggerName();
    
    Marker getMarker();
    
    Message getMessage();
    
    long getTimeMillis();
    
    Instant getInstant();
    
    StackTraceElement getSource();
    
    String getThreadName();
    
    long getThreadId();
    
    int getThreadPriority();
    
    Throwable getThrown();
    
    ThrowableProxy getThrownProxy();
    
    boolean isEndOfBatch();
    
    boolean isIncludeLocation();
    
    void setEndOfBatch(final boolean endOfBatch);
    
    void setIncludeLocation(final boolean locationRequired);
    
    long getNanoTime();
}
