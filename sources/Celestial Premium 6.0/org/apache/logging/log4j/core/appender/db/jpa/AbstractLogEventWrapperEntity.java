/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.persistence.Inheritance
 *  javax.persistence.InheritanceType
 *  javax.persistence.MappedSuperclass
 *  javax.persistence.Transient
 */
package org.apache.logging.log4j.core.appender.db.jpa;

import java.util.Map;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;

@MappedSuperclass
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class AbstractLogEventWrapperEntity
implements LogEvent {
    private static final long serialVersionUID = 1L;
    private final LogEvent wrappedEvent;

    protected AbstractLogEventWrapperEntity() {
        this(new NullLogEvent());
    }

    protected AbstractLogEventWrapperEntity(LogEvent wrappedEvent) {
        if (wrappedEvent == null) {
            throw new IllegalArgumentException("The wrapped event cannot be null.");
        }
        this.wrappedEvent = wrappedEvent;
    }

    @Transient
    protected final LogEvent getWrappedEvent() {
        return this.wrappedEvent;
    }

    public void setLevel(Level level) {
    }

    public void setLoggerName(String loggerName) {
    }

    public void setSource(StackTraceElement source) {
    }

    public void setMessage(Message message) {
    }

    public void setMarker(Marker marker) {
    }

    public void setThreadName(String threadName) {
    }

    public void setMillis(long millis) {
    }

    public void setThrown(Throwable throwable) {
    }

    public void setContextMap(Map<String, String> contextMap) {
    }

    public void setContextStack(ThreadContext.ContextStack contextStack) {
    }

    public void setFQCN(String fqcn) {
    }

    @Override
    @Transient
    public final boolean isIncludeLocation() {
        return this.getWrappedEvent().isIncludeLocation();
    }

    @Override
    public final void setIncludeLocation(boolean locationRequired) {
        this.getWrappedEvent().setIncludeLocation(locationRequired);
    }

    @Override
    @Transient
    public final boolean isEndOfBatch() {
        return this.getWrappedEvent().isEndOfBatch();
    }

    @Override
    public final void setEndOfBatch(boolean endOfBatch) {
        this.getWrappedEvent().setEndOfBatch(endOfBatch);
    }

    private static class NullLogEvent
    implements LogEvent {
        private static final long serialVersionUID = 1L;

        private NullLogEvent() {
        }

        @Override
        public Level getLevel() {
            return null;
        }

        @Override
        public String getLoggerName() {
            return null;
        }

        @Override
        public StackTraceElement getSource() {
            return null;
        }

        @Override
        public Message getMessage() {
            return null;
        }

        @Override
        public Marker getMarker() {
            return null;
        }

        @Override
        public String getThreadName() {
            return null;
        }

        @Override
        public long getMillis() {
            return 0L;
        }

        @Override
        public Throwable getThrown() {
            return null;
        }

        @Override
        public Map<String, String> getContextMap() {
            return null;
        }

        @Override
        public ThreadContext.ContextStack getContextStack() {
            return null;
        }

        @Override
        public String getFQCN() {
            return null;
        }

        @Override
        public boolean isIncludeLocation() {
            return false;
        }

        @Override
        public void setIncludeLocation(boolean locationRequired) {
        }

        @Override
        public boolean isEndOfBatch() {
            return false;
        }

        @Override
        public void setEndOfBatch(boolean endOfBatch) {
        }
    }
}

