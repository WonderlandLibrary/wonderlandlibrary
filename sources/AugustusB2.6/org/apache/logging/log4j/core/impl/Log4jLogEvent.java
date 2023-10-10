// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.message.SimpleMessage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.MarshalledObject;
import org.apache.logging.log4j.core.async.RingBufferLogEvent;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.core.util.DummyNanoClock;
import org.apache.logging.log4j.core.util.ClockFactory;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import org.apache.logging.log4j.util.StackLocatorUtil;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.core.time.Instant;
import org.apache.logging.log4j.message.ReusableMessage;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.Objects;
import java.util.Iterator;
import org.apache.logging.log4j.message.LoggerNameAwareMessage;
import org.apache.logging.log4j.message.TimestampMessage;
import java.util.Map;
import org.apache.logging.log4j.core.config.Property;
import java.util.List;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.util.StringMap;
import org.apache.logging.log4j.core.time.MutableInstant;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.ContextDataInjector;
import org.apache.logging.log4j.core.util.NanoClock;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.core.LogEvent;

public class Log4jLogEvent implements LogEvent
{
    private static final long serialVersionUID = -8393305700508709443L;
    private static final Clock CLOCK;
    private static volatile NanoClock nanoClock;
    private static final ContextDataInjector CONTEXT_DATA_INJECTOR;
    private final String loggerFqcn;
    private final Marker marker;
    private final Level level;
    private final String loggerName;
    private Message message;
    private final MutableInstant instant;
    private final transient Throwable thrown;
    private ThrowableProxy thrownProxy;
    private final StringMap contextData;
    private final ThreadContext.ContextStack contextStack;
    private long threadId;
    private String threadName;
    private int threadPriority;
    private StackTraceElement source;
    private boolean includeLocation;
    private boolean endOfBatch;
    private final transient long nanoTime;
    
    public static Builder newBuilder() {
        return new Builder();
    }
    
    public Log4jLogEvent() {
        this("", null, "", null, null, null, null, null, null, 0L, null, 0, null, Log4jLogEvent.CLOCK, Log4jLogEvent.nanoClock.nanoTime());
    }
    
    @Deprecated
    public Log4jLogEvent(final long timestamp) {
        this("", null, "", null, null, null, null, null, null, 0L, null, 0, null, timestamp, 0, Log4jLogEvent.nanoClock.nanoTime());
    }
    
    @Deprecated
    public Log4jLogEvent(final String loggerName, final Marker marker, final String loggerFQCN, final Level level, final Message message, final Throwable t) {
        this(loggerName, marker, loggerFQCN, level, message, null, t);
    }
    
    public Log4jLogEvent(final String loggerName, final Marker marker, final String loggerFQCN, final Level level, final Message message, final List<Property> properties, final Throwable t) {
        this(loggerName, marker, loggerFQCN, level, message, t, null, createContextData(properties), (ThreadContext.getDepth() == 0) ? null : ThreadContext.cloneStack(), 0L, null, 0, null, Log4jLogEvent.CLOCK, Log4jLogEvent.nanoClock.nanoTime());
    }
    
    public Log4jLogEvent(final String loggerName, final Marker marker, final String loggerFQCN, final StackTraceElement source, final Level level, final Message message, final List<Property> properties, final Throwable t) {
        this(loggerName, marker, loggerFQCN, level, message, t, null, createContextData(properties), (ThreadContext.getDepth() == 0) ? null : ThreadContext.cloneStack(), 0L, null, 0, source, Log4jLogEvent.CLOCK, Log4jLogEvent.nanoClock.nanoTime());
    }
    
    @Deprecated
    public Log4jLogEvent(final String loggerName, final Marker marker, final String loggerFQCN, final Level level, final Message message, final Throwable t, final Map<String, String> mdc, final ThreadContext.ContextStack ndc, final String threadName, final StackTraceElement location, final long timestampMillis) {
        this(loggerName, marker, loggerFQCN, level, message, t, null, createContextData(mdc), ndc, 0L, threadName, 0, location, timestampMillis, 0, Log4jLogEvent.nanoClock.nanoTime());
    }
    
    @Deprecated
    public static Log4jLogEvent createEvent(final String loggerName, final Marker marker, final String loggerFQCN, final Level level, final Message message, final Throwable thrown, final ThrowableProxy thrownProxy, final Map<String, String> mdc, final ThreadContext.ContextStack ndc, final String threadName, final StackTraceElement location, final long timestamp) {
        final Log4jLogEvent result = new Log4jLogEvent(loggerName, marker, loggerFQCN, level, message, thrown, thrownProxy, createContextData(mdc), ndc, 0L, threadName, 0, location, timestamp, 0, Log4jLogEvent.nanoClock.nanoTime());
        return result;
    }
    
    private Log4jLogEvent(final String loggerName, final Marker marker, final String loggerFQCN, final Level level, final Message message, final Throwable thrown, final ThrowableProxy thrownProxy, final StringMap contextData, final ThreadContext.ContextStack contextStack, final long threadId, final String threadName, final int threadPriority, final StackTraceElement source, final long timestampMillis, final int nanoOfMillisecond, final long nanoTime) {
        this(loggerName, marker, loggerFQCN, level, message, thrown, thrownProxy, contextData, contextStack, threadId, threadName, threadPriority, source, nanoTime);
        final long millis = (message instanceof TimestampMessage) ? ((TimestampMessage)message).getTimestamp() : timestampMillis;
        this.instant.initFromEpochMilli(millis, nanoOfMillisecond);
    }
    
    private Log4jLogEvent(final String loggerName, final Marker marker, final String loggerFQCN, final Level level, final Message message, final Throwable thrown, final ThrowableProxy thrownProxy, final StringMap contextData, final ThreadContext.ContextStack contextStack, final long threadId, final String threadName, final int threadPriority, final StackTraceElement source, final Clock clock, final long nanoTime) {
        this(loggerName, marker, loggerFQCN, level, message, thrown, thrownProxy, contextData, contextStack, threadId, threadName, threadPriority, source, nanoTime);
        if (message instanceof TimestampMessage) {
            this.instant.initFromEpochMilli(((TimestampMessage)message).getTimestamp(), 0);
        }
        else {
            this.instant.initFrom(clock);
        }
    }
    
    private Log4jLogEvent(final String loggerName, final Marker marker, final String loggerFQCN, final Level level, final Message message, final Throwable thrown, final ThrowableProxy thrownProxy, final StringMap contextData, final ThreadContext.ContextStack contextStack, final long threadId, final String threadName, final int threadPriority, final StackTraceElement source, final long nanoTime) {
        this.instant = new MutableInstant();
        this.endOfBatch = false;
        this.loggerName = loggerName;
        this.marker = marker;
        this.loggerFqcn = loggerFQCN;
        this.level = ((level == null) ? Level.OFF : level);
        this.message = message;
        this.thrown = thrown;
        this.thrownProxy = thrownProxy;
        this.contextData = ((contextData == null) ? ContextDataFactory.createContextData() : contextData);
        this.contextStack = ((contextStack == null) ? ThreadContext.EMPTY_STACK : contextStack);
        this.threadId = threadId;
        this.threadName = threadName;
        this.threadPriority = threadPriority;
        this.source = source;
        if (message instanceof LoggerNameAwareMessage) {
            ((LoggerNameAwareMessage)message).setLoggerName(loggerName);
        }
        this.nanoTime = nanoTime;
    }
    
    private static StringMap createContextData(final Map<String, String> contextMap) {
        final StringMap result = ContextDataFactory.createContextData();
        if (contextMap != null) {
            for (final Map.Entry<String, String> entry : contextMap.entrySet()) {
                result.putValue(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
    
    private static StringMap createContextData(final List<Property> properties) {
        final StringMap reusable = ContextDataFactory.createContextData();
        return Log4jLogEvent.CONTEXT_DATA_INJECTOR.injectContextData(properties, reusable);
    }
    
    public static NanoClock getNanoClock() {
        return Log4jLogEvent.nanoClock;
    }
    
    public static void setNanoClock(final NanoClock nanoClock) {
        Log4jLogEvent.nanoClock = Objects.requireNonNull(nanoClock, "NanoClock must be non-null");
        StatusLogger.getLogger().trace("Using {} for nanosecond timestamps.", nanoClock.getClass().getSimpleName());
    }
    
    public Builder asBuilder() {
        return new Builder(this);
    }
    
    @Override
    public Log4jLogEvent toImmutable() {
        if (this.getMessage() instanceof ReusableMessage) {
            this.makeMessageImmutable();
        }
        return this;
    }
    
    @Override
    public Level getLevel() {
        return this.level;
    }
    
    @Override
    public String getLoggerName() {
        return this.loggerName;
    }
    
    @Override
    public Message getMessage() {
        return this.message;
    }
    
    public void makeMessageImmutable() {
        this.message = new MementoMessage(this.message.getFormattedMessage(), this.message.getFormat(), this.message.getParameters());
    }
    
    @Override
    public long getThreadId() {
        if (this.threadId == 0L) {
            this.threadId = Thread.currentThread().getId();
        }
        return this.threadId;
    }
    
    @Override
    public String getThreadName() {
        if (this.threadName == null) {
            this.threadName = Thread.currentThread().getName();
        }
        return this.threadName;
    }
    
    @Override
    public int getThreadPriority() {
        if (this.threadPriority == 0) {
            this.threadPriority = Thread.currentThread().getPriority();
        }
        return this.threadPriority;
    }
    
    @Override
    public long getTimeMillis() {
        return this.instant.getEpochMillisecond();
    }
    
    @Override
    public Instant getInstant() {
        return this.instant;
    }
    
    @Override
    public Throwable getThrown() {
        return this.thrown;
    }
    
    @Override
    public ThrowableProxy getThrownProxy() {
        if (this.thrownProxy == null && this.thrown != null) {
            this.thrownProxy = new ThrowableProxy(this.thrown);
        }
        return this.thrownProxy;
    }
    
    @Override
    public Marker getMarker() {
        return this.marker;
    }
    
    @Override
    public String getLoggerFqcn() {
        return this.loggerFqcn;
    }
    
    @Override
    public ReadOnlyStringMap getContextData() {
        return this.contextData;
    }
    
    @Override
    public Map<String, String> getContextMap() {
        return this.contextData.toMap();
    }
    
    @Override
    public ThreadContext.ContextStack getContextStack() {
        return this.contextStack;
    }
    
    @Override
    public StackTraceElement getSource() {
        if (this.source != null) {
            return this.source;
        }
        if (this.loggerFqcn == null || !this.includeLocation) {
            return null;
        }
        return this.source = StackLocatorUtil.calcLocation(this.loggerFqcn);
    }
    
    @Override
    public boolean isIncludeLocation() {
        return this.includeLocation;
    }
    
    @Override
    public void setIncludeLocation(final boolean includeLocation) {
        this.includeLocation = includeLocation;
    }
    
    @Override
    public boolean isEndOfBatch() {
        return this.endOfBatch;
    }
    
    @Override
    public void setEndOfBatch(final boolean endOfBatch) {
        this.endOfBatch = endOfBatch;
    }
    
    @Override
    public long getNanoTime() {
        return this.nanoTime;
    }
    
    protected Object writeReplace() {
        this.getThrownProxy();
        return new LogEventProxy(this, this.includeLocation);
    }
    
    public static Serializable serialize(final LogEvent event, final boolean includeLocation) {
        if (event instanceof Log4jLogEvent) {
            event.getThrownProxy();
            return new LogEventProxy((Log4jLogEvent)event, includeLocation);
        }
        return new LogEventProxy(event, includeLocation);
    }
    
    public static Serializable serialize(final Log4jLogEvent event, final boolean includeLocation) {
        event.getThrownProxy();
        return new LogEventProxy(event, includeLocation);
    }
    
    public static boolean canDeserialize(final Serializable event) {
        return event instanceof LogEventProxy;
    }
    
    public static Log4jLogEvent deserialize(final Serializable event) {
        Objects.requireNonNull(event, "Event cannot be null");
        if (event instanceof LogEventProxy) {
            final LogEventProxy proxy = (LogEventProxy)event;
            final Log4jLogEvent result = new Log4jLogEvent(proxy.loggerName, proxy.marker, proxy.loggerFQCN, proxy.level, proxy.message, proxy.thrown, proxy.thrownProxy, proxy.contextData, proxy.contextStack, proxy.threadId, proxy.threadName, proxy.threadPriority, proxy.source, proxy.timeMillis, proxy.nanoOfMillisecond, proxy.nanoTime);
            result.setEndOfBatch(proxy.isEndOfBatch);
            result.setIncludeLocation(proxy.isLocationRequired);
            return result;
        }
        throw new IllegalArgumentException("Event is not a serialized LogEvent: " + event.toString());
    }
    
    private void readObject(final ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
    
    public static LogEvent createMemento(final LogEvent logEvent) {
        return new Builder(logEvent).build();
    }
    
    public static Log4jLogEvent createMemento(final LogEvent event, final boolean includeLocation) {
        return deserialize(serialize(event, includeLocation));
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final String n = this.loggerName.isEmpty() ? "root" : this.loggerName;
        sb.append("Logger=").append(n);
        sb.append(" Level=").append(this.level.name());
        sb.append(" Message=").append((this.message == null) ? null : this.message.getFormattedMessage());
        return sb.toString();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Log4jLogEvent that = (Log4jLogEvent)o;
        if (this.endOfBatch != that.endOfBatch) {
            return false;
        }
        if (this.includeLocation != that.includeLocation) {
            return false;
        }
        if (!this.instant.equals(that.instant)) {
            return false;
        }
        if (this.nanoTime != that.nanoTime) {
            return false;
        }
        Label_0118: {
            if (this.loggerFqcn != null) {
                if (this.loggerFqcn.equals(that.loggerFqcn)) {
                    break Label_0118;
                }
            }
            else if (that.loggerFqcn == null) {
                break Label_0118;
            }
            return false;
        }
        Label_0151: {
            if (this.level != null) {
                if (this.level.equals(that.level)) {
                    break Label_0151;
                }
            }
            else if (that.level == null) {
                break Label_0151;
            }
            return false;
        }
        Label_0184: {
            if (this.source != null) {
                if (this.source.equals(that.source)) {
                    break Label_0184;
                }
            }
            else if (that.source == null) {
                break Label_0184;
            }
            return false;
        }
        Label_0219: {
            if (this.marker != null) {
                if (this.marker.equals(that.marker)) {
                    break Label_0219;
                }
            }
            else if (that.marker == null) {
                break Label_0219;
            }
            return false;
        }
        Label_0254: {
            if (this.contextData != null) {
                if (this.contextData.equals(that.contextData)) {
                    break Label_0254;
                }
            }
            else if (that.contextData == null) {
                break Label_0254;
            }
            return false;
        }
        if (!this.message.equals(that.message)) {
            return false;
        }
        if (!this.loggerName.equals(that.loggerName)) {
            return false;
        }
        Label_0319: {
            if (this.contextStack != null) {
                if (this.contextStack.equals(that.contextStack)) {
                    break Label_0319;
                }
            }
            else if (that.contextStack == null) {
                break Label_0319;
            }
            return false;
        }
        if (this.threadId != that.threadId) {
            return false;
        }
        Label_0366: {
            if (this.threadName != null) {
                if (this.threadName.equals(that.threadName)) {
                    break Label_0366;
                }
            }
            else if (that.threadName == null) {
                break Label_0366;
            }
            return false;
        }
        if (this.threadPriority != that.threadPriority) {
            return false;
        }
        Label_0412: {
            if (this.thrown != null) {
                if (this.thrown.equals(that.thrown)) {
                    break Label_0412;
                }
            }
            else if (that.thrown == null) {
                break Label_0412;
            }
            return false;
        }
        if (this.thrownProxy != null) {
            if (this.thrownProxy.equals(that.thrownProxy)) {
                return true;
            }
        }
        else if (that.thrownProxy == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.loggerFqcn != null) ? this.loggerFqcn.hashCode() : 0;
        result = 31 * result + ((this.marker != null) ? this.marker.hashCode() : 0);
        result = 31 * result + ((this.level != null) ? this.level.hashCode() : 0);
        result = 31 * result + this.loggerName.hashCode();
        result = 31 * result + this.message.hashCode();
        result = 31 * result + this.instant.hashCode();
        result = 31 * result + (int)(this.nanoTime ^ this.nanoTime >>> 32);
        result = 31 * result + ((this.thrown != null) ? this.thrown.hashCode() : 0);
        result = 31 * result + ((this.thrownProxy != null) ? this.thrownProxy.hashCode() : 0);
        result = 31 * result + ((this.contextData != null) ? this.contextData.hashCode() : 0);
        result = 31 * result + ((this.contextStack != null) ? this.contextStack.hashCode() : 0);
        result = 31 * result + (int)(this.threadId ^ this.threadId >>> 32);
        result = 31 * result + ((this.threadName != null) ? this.threadName.hashCode() : 0);
        result = 31 * result + (this.threadPriority ^ this.threadPriority >>> 32);
        result = 31 * result + ((this.source != null) ? this.source.hashCode() : 0);
        result = 31 * result + (this.includeLocation ? 1 : 0);
        result = 31 * result + (this.endOfBatch ? 1 : 0);
        return result;
    }
    
    static {
        CLOCK = ClockFactory.getClock();
        Log4jLogEvent.nanoClock = new DummyNanoClock();
        CONTEXT_DATA_INJECTOR = ContextDataInjectorFactory.createInjector();
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<LogEvent>
    {
        private String loggerFqcn;
        private Marker marker;
        private Level level;
        private String loggerName;
        private Message message;
        private Throwable thrown;
        private final MutableInstant instant;
        private ThrowableProxy thrownProxy;
        private StringMap contextData;
        private ThreadContext.ContextStack contextStack;
        private long threadId;
        private String threadName;
        private int threadPriority;
        private StackTraceElement source;
        private boolean includeLocation;
        private boolean endOfBatch;
        private long nanoTime;
        
        public Builder() {
            this.instant = new MutableInstant();
            this.contextData = createContextData(null);
            this.contextStack = ThreadContext.getImmutableStack();
            this.endOfBatch = false;
        }
        
        public Builder(final LogEvent other) {
            this.instant = new MutableInstant();
            this.contextData = createContextData(null);
            this.contextStack = ThreadContext.getImmutableStack();
            this.endOfBatch = false;
            Objects.requireNonNull(other);
            if (other instanceof RingBufferLogEvent) {
                ((RingBufferLogEvent)other).initializeBuilder(this);
                return;
            }
            if (other instanceof MutableLogEvent) {
                ((MutableLogEvent)other).initializeBuilder(this);
                return;
            }
            this.loggerFqcn = other.getLoggerFqcn();
            this.marker = other.getMarker();
            this.level = other.getLevel();
            this.loggerName = other.getLoggerName();
            this.message = other.getMessage();
            this.instant.initFrom(other.getInstant());
            this.thrown = other.getThrown();
            this.contextStack = other.getContextStack();
            this.includeLocation = other.isIncludeLocation();
            this.endOfBatch = other.isEndOfBatch();
            this.nanoTime = other.getNanoTime();
            if (other instanceof Log4jLogEvent) {
                final Log4jLogEvent evt = (Log4jLogEvent)other;
                this.contextData = evt.contextData;
                this.thrownProxy = evt.thrownProxy;
                this.source = evt.source;
                this.threadId = evt.threadId;
                this.threadName = evt.threadName;
                this.threadPriority = evt.threadPriority;
            }
            else {
                if (other.getContextData() instanceof StringMap) {
                    this.contextData = (StringMap)other.getContextData();
                }
                else {
                    if (this.contextData.isFrozen()) {
                        this.contextData = ContextDataFactory.createContextData();
                    }
                    else {
                        this.contextData.clear();
                    }
                    this.contextData.putAll(other.getContextData());
                }
                this.thrownProxy = other.getThrownProxy();
                this.source = other.getSource();
                this.threadId = other.getThreadId();
                this.threadName = other.getThreadName();
                this.threadPriority = other.getThreadPriority();
            }
        }
        
        public Builder setLevel(final Level level) {
            this.level = level;
            return this;
        }
        
        public Builder setLoggerFqcn(final String loggerFqcn) {
            this.loggerFqcn = loggerFqcn;
            return this;
        }
        
        public Builder setLoggerName(final String loggerName) {
            this.loggerName = loggerName;
            return this;
        }
        
        public Builder setMarker(final Marker marker) {
            this.marker = marker;
            return this;
        }
        
        public Builder setMessage(final Message message) {
            this.message = message;
            return this;
        }
        
        public Builder setThrown(final Throwable thrown) {
            this.thrown = thrown;
            return this;
        }
        
        public Builder setTimeMillis(final long timeMillis) {
            this.instant.initFromEpochMilli(timeMillis, 0);
            return this;
        }
        
        public Builder setInstant(final Instant instant) {
            this.instant.initFrom(instant);
            return this;
        }
        
        public Builder setThrownProxy(final ThrowableProxy thrownProxy) {
            this.thrownProxy = thrownProxy;
            return this;
        }
        
        @Deprecated
        public Builder setContextMap(final Map<String, String> contextMap) {
            this.contextData = ContextDataFactory.createContextData();
            if (contextMap != null) {
                for (final Map.Entry<String, String> entry : contextMap.entrySet()) {
                    this.contextData.putValue(entry.getKey(), entry.getValue());
                }
            }
            return this;
        }
        
        public Builder setContextData(final StringMap contextData) {
            this.contextData = contextData;
            return this;
        }
        
        public Builder setContextStack(final ThreadContext.ContextStack contextStack) {
            this.contextStack = contextStack;
            return this;
        }
        
        public Builder setThreadId(final long threadId) {
            this.threadId = threadId;
            return this;
        }
        
        public Builder setThreadName(final String threadName) {
            this.threadName = threadName;
            return this;
        }
        
        public Builder setThreadPriority(final int threadPriority) {
            this.threadPriority = threadPriority;
            return this;
        }
        
        public Builder setSource(final StackTraceElement source) {
            this.source = source;
            return this;
        }
        
        public Builder setIncludeLocation(final boolean includeLocation) {
            this.includeLocation = includeLocation;
            return this;
        }
        
        public Builder setEndOfBatch(final boolean endOfBatch) {
            this.endOfBatch = endOfBatch;
            return this;
        }
        
        public Builder setNanoTime(final long nanoTime) {
            this.nanoTime = nanoTime;
            return this;
        }
        
        @Override
        public Log4jLogEvent build() {
            this.initTimeFields();
            final Log4jLogEvent result = new Log4jLogEvent(this.loggerName, this.marker, this.loggerFqcn, this.level, this.message, this.thrown, this.thrownProxy, this.contextData, this.contextStack, this.threadId, this.threadName, this.threadPriority, this.source, this.instant.getEpochMillisecond(), this.instant.getNanoOfMillisecond(), this.nanoTime, null);
            result.setIncludeLocation(this.includeLocation);
            result.setEndOfBatch(this.endOfBatch);
            return result;
        }
        
        private void initTimeFields() {
            if (this.instant.getEpochMillisecond() == 0L) {
                this.instant.initFrom(Log4jLogEvent.CLOCK);
            }
        }
    }
    
    static class LogEventProxy implements Serializable
    {
        private static final long serialVersionUID = -8634075037355293699L;
        private final String loggerFQCN;
        private final Marker marker;
        private final Level level;
        private final String loggerName;
        private final transient Message message;
        private MarshalledObject<Message> marshalledMessage;
        private String messageString;
        private final long timeMillis;
        private final int nanoOfMillisecond;
        private final transient Throwable thrown;
        private final ThrowableProxy thrownProxy;
        private final StringMap contextData;
        private final ThreadContext.ContextStack contextStack;
        private final long threadId;
        private final String threadName;
        private final int threadPriority;
        private final StackTraceElement source;
        private final boolean isLocationRequired;
        private final boolean isEndOfBatch;
        private final transient long nanoTime;
        
        public LogEventProxy(final Log4jLogEvent event, final boolean includeLocation) {
            this.loggerFQCN = event.loggerFqcn;
            this.marker = event.marker;
            this.level = event.level;
            this.loggerName = event.loggerName;
            this.message = ((event.message instanceof ReusableMessage) ? memento((ReusableMessage)event.message) : event.message);
            this.timeMillis = event.instant.getEpochMillisecond();
            this.nanoOfMillisecond = event.instant.getNanoOfMillisecond();
            this.thrown = event.thrown;
            this.thrownProxy = event.thrownProxy;
            this.contextData = event.contextData;
            this.contextStack = event.contextStack;
            this.source = (includeLocation ? event.getSource() : null);
            this.threadId = event.getThreadId();
            this.threadName = event.getThreadName();
            this.threadPriority = event.getThreadPriority();
            this.isLocationRequired = includeLocation;
            this.isEndOfBatch = event.endOfBatch;
            this.nanoTime = event.nanoTime;
        }
        
        public LogEventProxy(final LogEvent event, final boolean includeLocation) {
            this.loggerFQCN = event.getLoggerFqcn();
            this.marker = event.getMarker();
            this.level = event.getLevel();
            this.loggerName = event.getLoggerName();
            final Message temp = event.getMessage();
            this.message = ((temp instanceof ReusableMessage) ? memento((ReusableMessage)temp) : temp);
            this.timeMillis = event.getInstant().getEpochMillisecond();
            this.nanoOfMillisecond = event.getInstant().getNanoOfMillisecond();
            this.thrown = event.getThrown();
            this.thrownProxy = event.getThrownProxy();
            this.contextData = memento(event.getContextData());
            this.contextStack = event.getContextStack();
            this.source = (includeLocation ? event.getSource() : null);
            this.threadId = event.getThreadId();
            this.threadName = event.getThreadName();
            this.threadPriority = event.getThreadPriority();
            this.isLocationRequired = includeLocation;
            this.isEndOfBatch = event.isEndOfBatch();
            this.nanoTime = event.getNanoTime();
        }
        
        private static Message memento(final ReusableMessage message) {
            return message.memento();
        }
        
        private static StringMap memento(final ReadOnlyStringMap data) {
            final StringMap result = ContextDataFactory.createContextData();
            result.putAll(data);
            return result;
        }
        
        private static MarshalledObject<Message> marshall(final Message msg) {
            try {
                return new MarshalledObject<Message>(msg);
            }
            catch (Exception ex) {
                return null;
            }
        }
        
        private void writeObject(final ObjectOutputStream s) throws IOException {
            this.messageString = this.message.getFormattedMessage();
            this.marshalledMessage = marshall(this.message);
            s.defaultWriteObject();
        }
        
        protected Object readResolve() {
            final Log4jLogEvent result = new Log4jLogEvent(this.loggerName, this.marker, this.loggerFQCN, this.level, this.message(), this.thrown, this.thrownProxy, this.contextData, this.contextStack, this.threadId, this.threadName, this.threadPriority, this.source, this.timeMillis, this.nanoOfMillisecond, this.nanoTime, null);
            result.setEndOfBatch(this.isEndOfBatch);
            result.setIncludeLocation(this.isLocationRequired);
            return result;
        }
        
        private Message message() {
            if (this.marshalledMessage != null) {
                try {
                    return this.marshalledMessage.get();
                }
                catch (Exception ex) {}
            }
            return new SimpleMessage(this.messageString);
        }
    }
}
