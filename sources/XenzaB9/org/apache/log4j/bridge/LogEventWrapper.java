// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.bridge;

import org.apache.logging.log4j.util.TriConsumer;
import org.apache.logging.log4j.util.BiConsumer;
import java.util.HashMap;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.log4j.spi.ThrowableInformation;
import java.util.Iterator;
import org.apache.log4j.spi.LocationInfo;
import org.apache.logging.log4j.core.time.MutableInstant;
import org.apache.logging.log4j.core.time.Instant;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.log4j.Priority;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import java.util.Objects;
import java.util.List;
import org.apache.log4j.NDC;
import java.util.Map;
import org.apache.logging.log4j.spi.MutableThreadContextStack;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.logging.log4j.core.LogEvent;

public class LogEventWrapper implements LogEvent
{
    private final LoggingEvent event;
    private final ContextDataMap contextData;
    private final MutableThreadContextStack contextStack;
    private Thread thread;
    
    public LogEventWrapper(final LoggingEvent event) {
        this.event = event;
        this.contextData = new ContextDataMap(event.getProperties());
        this.contextStack = new MutableThreadContextStack(NDC.cloneStack());
        this.thread = (Objects.equals(event.getThreadName(), Thread.currentThread().getName()) ? Thread.currentThread() : null);
    }
    
    @Override
    public LogEvent toImmutable() {
        return this;
    }
    
    @Override
    public Map<String, String> getContextMap() {
        return this.contextData;
    }
    
    @Override
    public ReadOnlyStringMap getContextData() {
        return this.contextData;
    }
    
    @Override
    public ThreadContext.ContextStack getContextStack() {
        return this.contextStack;
    }
    
    @Override
    public String getLoggerFqcn() {
        return null;
    }
    
    @Override
    public Level getLevel() {
        return OptionConverter.convertLevel(this.event.getLevel());
    }
    
    @Override
    public String getLoggerName() {
        return this.event.getLoggerName();
    }
    
    @Override
    public Marker getMarker() {
        return null;
    }
    
    @Override
    public Message getMessage() {
        return new SimpleMessage(this.event.getRenderedMessage());
    }
    
    @Override
    public long getTimeMillis() {
        return this.event.getTimeStamp();
    }
    
    @Override
    public Instant getInstant() {
        final MutableInstant mutable = new MutableInstant();
        mutable.initFromEpochMilli(this.event.getTimeStamp(), 0);
        return mutable;
    }
    
    @Override
    public StackTraceElement getSource() {
        final LocationInfo info = this.event.getLocationInformation();
        return new StackTraceElement(info.getClassName(), info.getMethodName(), info.getFileName(), Integer.parseInt(info.getLineNumber()));
    }
    
    @Override
    public String getThreadName() {
        return this.event.getThreadName();
    }
    
    @Override
    public long getThreadId() {
        final Thread thread = this.getThread();
        return (thread != null) ? thread.getId() : 0L;
    }
    
    @Override
    public int getThreadPriority() {
        final Thread thread = this.getThread();
        return (thread != null) ? thread.getPriority() : 0;
    }
    
    private Thread getThread() {
        if (this.thread == null && this.event.getThreadName() != null) {
            for (final Thread thread : Thread.getAllStackTraces().keySet()) {
                if (thread.getName().equals(this.event.getThreadName())) {
                    return this.thread = thread;
                }
            }
        }
        return this.thread;
    }
    
    @Override
    public Throwable getThrown() {
        final ThrowableInformation throwableInformation = this.event.getThrowableInformation();
        return (throwableInformation == null) ? null : throwableInformation.getThrowable();
    }
    
    @Override
    public ThrowableProxy getThrownProxy() {
        return null;
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
    
    private static class ContextDataMap extends HashMap<String, String> implements ReadOnlyStringMap
    {
        ContextDataMap(final Map<String, String> map) {
            if (map != null) {
                super.putAll(map);
            }
        }
        
        @Override
        public Map<String, String> toMap() {
            return this;
        }
        
        @Override
        public boolean containsKey(final String key) {
            return super.containsKey(key);
        }
        
        @Override
        public <V> void forEach(final BiConsumer<String, ? super V> action) {
            super.forEach((k, v) -> action.accept(k, v));
        }
        
        @Override
        public <V, S> void forEach(final TriConsumer<String, ? super V, S> action, final S state) {
            super.forEach((k, v) -> action.accept(k, v, state));
        }
        
        @Override
        public <V> V getValue(final String key) {
            return (V)super.get(key);
        }
    }
}
