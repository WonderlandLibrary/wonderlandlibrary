// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core;

import org.apache.logging.log4j.core.time.Instant;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.ThreadContext;
import java.util.Collections;
import java.util.Map;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.core.time.MutableInstant;

public abstract class AbstractLogEvent implements LogEvent
{
    private static final long serialVersionUID = 1L;
    private volatile MutableInstant instant;
    
    @Override
    public LogEvent toImmutable() {
        return this;
    }
    
    @Override
    public ReadOnlyStringMap getContextData() {
        return null;
    }
    
    @Override
    public Map<String, String> getContextMap() {
        return Collections.emptyMap();
    }
    
    @Override
    public ThreadContext.ContextStack getContextStack() {
        return ThreadContext.EMPTY_STACK;
    }
    
    @Override
    public Level getLevel() {
        return null;
    }
    
    @Override
    public String getLoggerFqcn() {
        return null;
    }
    
    @Override
    public String getLoggerName() {
        return null;
    }
    
    @Override
    public Marker getMarker() {
        return null;
    }
    
    @Override
    public Message getMessage() {
        return null;
    }
    
    @Override
    public StackTraceElement getSource() {
        return null;
    }
    
    @Override
    public long getThreadId() {
        return 0L;
    }
    
    @Override
    public String getThreadName() {
        return null;
    }
    
    @Override
    public int getThreadPriority() {
        return 0;
    }
    
    @Override
    public Throwable getThrown() {
        return null;
    }
    
    @Override
    public ThrowableProxy getThrownProxy() {
        return null;
    }
    
    @Override
    public long getTimeMillis() {
        return 0L;
    }
    
    @Override
    public Instant getInstant() {
        return this.getMutableInstant();
    }
    
    protected final MutableInstant getMutableInstant() {
        if (this.instant == null) {
            this.instant = new MutableInstant();
        }
        return this.instant;
    }
    
    @Override
    public boolean isEndOfBatch() {
        return false;
    }
    
    @Override
    public boolean isIncludeLocation() {
        return false;
    }
    
    @Override
    public void setEndOfBatch(final boolean endOfBatch) {
    }
    
    @Override
    public void setIncludeLocation(final boolean locationRequired) {
    }
    
    @Override
    public long getNanoTime() {
        return 0L;
    }
}
