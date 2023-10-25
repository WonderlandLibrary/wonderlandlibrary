package org.apache.logging.log4j.core.impl;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.MarshalledObject;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.ContextDataInjector;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.async.RingBufferLogEvent;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.time.Instant;
import org.apache.logging.log4j.core.time.MutableInstant;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.core.util.ClockFactory;
import org.apache.logging.log4j.core.util.DummyNanoClock;
import org.apache.logging.log4j.core.util.NanoClock;
import org.apache.logging.log4j.message.LoggerNameAwareMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ReusableMessage;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.message.TimestampMessage;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.StackLocatorUtil;
import org.apache.logging.log4j.util.StringMap;

public class Log4jLogEvent implements LogEvent {
  private static final long serialVersionUID = -8393305700508709443L;
  
  private static final Clock CLOCK = ClockFactory.getClock();
  
  private static volatile NanoClock nanoClock = (NanoClock)new DummyNanoClock();
  
  private static final ContextDataInjector CONTEXT_DATA_INJECTOR = ContextDataInjectorFactory.createInjector();
  
  private final String loggerFqcn;
  
  private final Marker marker;
  
  private final Level level;
  
  private final String loggerName;
  
  private Message message;
  
  private final MutableInstant instant = new MutableInstant();
  
  private final transient Throwable thrown;
  
  private ThrowableProxy thrownProxy;
  
  private final StringMap contextData;
  
  private final ThreadContext.ContextStack contextStack;
  
  private long threadId;
  
  private String threadName;
  
  private int threadPriority;
  
  private StackTraceElement source;
  
  private boolean includeLocation;
  
  private boolean endOfBatch = false;
  
  private final transient long nanoTime;
  
  public static class Builder implements org.apache.logging.log4j.core.util.Builder<LogEvent> {
    private String loggerFqcn;
    
    private Marker marker;
    
    private Level level;
    
    private String loggerName;
    
    private Message message;
    
    private Throwable thrown;
    
    private final MutableInstant instant = new MutableInstant();
    
    private ThrowableProxy thrownProxy;
    
    private StringMap contextData = Log4jLogEvent.createContextData((List)null);
    
    private ThreadContext.ContextStack contextStack = ThreadContext.getImmutableStack();
    
    private long threadId;
    
    private String threadName;
    
    private int threadPriority;
    
    private StackTraceElement source;
    
    private boolean includeLocation;
    
    private boolean endOfBatch = false;
    
    private long nanoTime;
    
    public Builder(LogEvent other) {
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
        Log4jLogEvent evt = (Log4jLogEvent)other;
        this.contextData = evt.contextData;
        this.thrownProxy = evt.thrownProxy;
        this.source = evt.source;
        this.threadId = evt.threadId;
        this.threadName = evt.threadName;
        this.threadPriority = evt.threadPriority;
      } else {
        if (other.getContextData() instanceof StringMap) {
          this.contextData = (StringMap)other.getContextData();
        } else {
          if (this.contextData.isFrozen()) {
            this.contextData = ContextDataFactory.createContextData();
          } else {
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
    
    public Builder setLevel(Level level) {
      this.level = level;
      return this;
    }
    
    public Builder setLoggerFqcn(String loggerFqcn) {
      this.loggerFqcn = loggerFqcn;
      return this;
    }
    
    public Builder setLoggerName(String loggerName) {
      this.loggerName = loggerName;
      return this;
    }
    
    public Builder setMarker(Marker marker) {
      this.marker = marker;
      return this;
    }
    
    public Builder setMessage(Message message) {
      this.message = message;
      return this;
    }
    
    public Builder setThrown(Throwable thrown) {
      this.thrown = thrown;
      return this;
    }
    
    public Builder setTimeMillis(long timeMillis) {
      this.instant.initFromEpochMilli(timeMillis, 0);
      return this;
    }
    
    public Builder setInstant(Instant instant) {
      this.instant.initFrom(instant);
      return this;
    }
    
    public Builder setThrownProxy(ThrowableProxy thrownProxy) {
      this.thrownProxy = thrownProxy;
      return this;
    }
    
    @Deprecated
    public Builder setContextMap(Map<String, String> contextMap) {
      this.contextData = ContextDataFactory.createContextData();
      if (contextMap != null)
        for (Map.Entry<String, String> entry : contextMap.entrySet())
          this.contextData.putValue(entry.getKey(), entry.getValue());  
      return this;
    }
    
    public Builder setContextData(StringMap contextData) {
      this.contextData = contextData;
      return this;
    }
    
    public Builder setContextStack(ThreadContext.ContextStack contextStack) {
      this.contextStack = contextStack;
      return this;
    }
    
    public Builder setThreadId(long threadId) {
      this.threadId = threadId;
      return this;
    }
    
    public Builder setThreadName(String threadName) {
      this.threadName = threadName;
      return this;
    }
    
    public Builder setThreadPriority(int threadPriority) {
      this.threadPriority = threadPriority;
      return this;
    }
    
    public Builder setSource(StackTraceElement source) {
      this.source = source;
      return this;
    }
    
    public Builder setIncludeLocation(boolean includeLocation) {
      this.includeLocation = includeLocation;
      return this;
    }
    
    public Builder setEndOfBatch(boolean endOfBatch) {
      this.endOfBatch = endOfBatch;
      return this;
    }
    
    public Builder setNanoTime(long nanoTime) {
      this.nanoTime = nanoTime;
      return this;
    }
    
    public Log4jLogEvent build() {
      initTimeFields();
      Log4jLogEvent result = new Log4jLogEvent(this.loggerName, this.marker, this.loggerFqcn, this.level, this.message, this.thrown, this.thrownProxy, this.contextData, this.contextStack, this.threadId, this.threadName, this.threadPriority, this.source, this.instant.getEpochMillisecond(), this.instant.getNanoOfMillisecond(), this.nanoTime);
      result.setIncludeLocation(this.includeLocation);
      result.setEndOfBatch(this.endOfBatch);
      return result;
    }
    
    private void initTimeFields() {
      if (this.instant.getEpochMillisecond() == 0L)
        this.instant.initFrom(Log4jLogEvent.CLOCK); 
    }
    
    public Builder() {}
  }
  
  public static Builder newBuilder() {
    return new Builder();
  }
  
  public Log4jLogEvent() {
    this("", null, "", null, null, (Throwable)null, null, null, null, 0L, null, 0, null, CLOCK, nanoClock
        .nanoTime());
  }
  
  @Deprecated
  public Log4jLogEvent(long timestamp) {
    this("", null, "", null, null, (Throwable)null, null, null, null, 0L, null, 0, null, timestamp, 0, nanoClock
        .nanoTime());
  }
  
  @Deprecated
  public Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, Throwable t) {
    this(loggerName, marker, loggerFQCN, level, message, null, t);
  }
  
  public Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, List<Property> properties, Throwable t) {
    this(loggerName, marker, loggerFQCN, level, message, t, null, createContextData(properties), 
        (ThreadContext.getDepth() == 0) ? null : ThreadContext.cloneStack(), 0L, null, 0, null, CLOCK, nanoClock
        
        .nanoTime());
  }
  
  public Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, StackTraceElement source, Level level, Message message, List<Property> properties, Throwable t) {
    this(loggerName, marker, loggerFQCN, level, message, t, null, createContextData(properties), 
        (ThreadContext.getDepth() == 0) ? null : ThreadContext.cloneStack(), 0L, null, 0, source, CLOCK, nanoClock
        
        .nanoTime());
  }
  
  @Deprecated
  public Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, Throwable t, Map<String, String> mdc, ThreadContext.ContextStack ndc, String threadName, StackTraceElement location, long timestampMillis) {
    this(loggerName, marker, loggerFQCN, level, message, t, null, createContextData(mdc), ndc, 0L, threadName, 0, location, timestampMillis, 0, nanoClock
        .nanoTime());
  }
  
  @Deprecated
  public static Log4jLogEvent createEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, Throwable thrown, ThrowableProxy thrownProxy, Map<String, String> mdc, ThreadContext.ContextStack ndc, String threadName, StackTraceElement location, long timestamp) {
    Log4jLogEvent result = new Log4jLogEvent(loggerName, marker, loggerFQCN, level, message, thrown, thrownProxy, createContextData(mdc), ndc, 0L, threadName, 0, location, timestamp, 0, nanoClock.nanoTime());
    return result;
  }
  
  private Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, Throwable thrown, ThrowableProxy thrownProxy, StringMap contextData, ThreadContext.ContextStack contextStack, long threadId, String threadName, int threadPriority, StackTraceElement source, long timestampMillis, int nanoOfMillisecond, long nanoTime) {
    this(loggerName, marker, loggerFQCN, level, message, thrown, thrownProxy, contextData, contextStack, threadId, threadName, threadPriority, source, nanoTime);
    long millis = (message instanceof TimestampMessage) ? ((TimestampMessage)message).getTimestamp() : timestampMillis;
    this.instant.initFromEpochMilli(millis, nanoOfMillisecond);
  }
  
  private Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, Throwable thrown, ThrowableProxy thrownProxy, StringMap contextData, ThreadContext.ContextStack contextStack, long threadId, String threadName, int threadPriority, StackTraceElement source, Clock clock, long nanoTime) {
    this(loggerName, marker, loggerFQCN, level, message, thrown, thrownProxy, contextData, contextStack, threadId, threadName, threadPriority, source, nanoTime);
    if (message instanceof TimestampMessage) {
      this.instant.initFromEpochMilli(((TimestampMessage)message).getTimestamp(), 0);
    } else {
      this.instant.initFrom(clock);
    } 
  }
  
  private Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, Throwable thrown, ThrowableProxy thrownProxy, StringMap contextData, ThreadContext.ContextStack contextStack, long threadId, String threadName, int threadPriority, StackTraceElement source, long nanoTime) {
    this.loggerName = loggerName;
    this.marker = marker;
    this.loggerFqcn = loggerFQCN;
    this.level = (level == null) ? Level.OFF : level;
    this.message = message;
    this.thrown = thrown;
    this.thrownProxy = thrownProxy;
    this.contextData = (contextData == null) ? ContextDataFactory.createContextData() : contextData;
    this.contextStack = (contextStack == null) ? (ThreadContext.ContextStack)ThreadContext.EMPTY_STACK : contextStack;
    this.threadId = threadId;
    this.threadName = threadName;
    this.threadPriority = threadPriority;
    this.source = source;
    if (message instanceof LoggerNameAwareMessage)
      ((LoggerNameAwareMessage)message).setLoggerName(loggerName); 
    this.nanoTime = nanoTime;
  }
  
  private static StringMap createContextData(Map<String, String> contextMap) {
    StringMap result = ContextDataFactory.createContextData();
    if (contextMap != null)
      for (Map.Entry<String, String> entry : contextMap.entrySet())
        result.putValue(entry.getKey(), entry.getValue());  
    return result;
  }
  
  private static StringMap createContextData(List<Property> properties) {
    StringMap reusable = ContextDataFactory.createContextData();
    return CONTEXT_DATA_INJECTOR.injectContextData(properties, reusable);
  }
  
  public static NanoClock getNanoClock() {
    return nanoClock;
  }
  
  public static void setNanoClock(NanoClock nanoClock) {
    Log4jLogEvent.nanoClock = Objects.<NanoClock>requireNonNull(nanoClock, "NanoClock must be non-null");
    StatusLogger.getLogger().trace("Using {} for nanosecond timestamps.", nanoClock.getClass().getSimpleName());
  }
  
  public Builder asBuilder() {
    return new Builder(this);
  }
  
  public Log4jLogEvent toImmutable() {
    if (getMessage() instanceof ReusableMessage)
      makeMessageImmutable(); 
    return this;
  }
  
  public Level getLevel() {
    return this.level;
  }
  
  public String getLoggerName() {
    return this.loggerName;
  }
  
  public Message getMessage() {
    return this.message;
  }
  
  public void makeMessageImmutable() {
    this.message = new MementoMessage(this.message.getFormattedMessage(), this.message.getFormat(), this.message.getParameters());
  }
  
  public long getThreadId() {
    if (this.threadId == 0L)
      this.threadId = Thread.currentThread().getId(); 
    return this.threadId;
  }
  
  public String getThreadName() {
    if (this.threadName == null)
      this.threadName = Thread.currentThread().getName(); 
    return this.threadName;
  }
  
  public int getThreadPriority() {
    if (this.threadPriority == 0)
      this.threadPriority = Thread.currentThread().getPriority(); 
    return this.threadPriority;
  }
  
  public long getTimeMillis() {
    return this.instant.getEpochMillisecond();
  }
  
  public Instant getInstant() {
    return (Instant)this.instant;
  }
  
  public Throwable getThrown() {
    return this.thrown;
  }
  
  public ThrowableProxy getThrownProxy() {
    if (this.thrownProxy == null && this.thrown != null)
      this.thrownProxy = new ThrowableProxy(this.thrown); 
    return this.thrownProxy;
  }
  
  public Marker getMarker() {
    return this.marker;
  }
  
  public String getLoggerFqcn() {
    return this.loggerFqcn;
  }
  
  public ReadOnlyStringMap getContextData() {
    return (ReadOnlyStringMap)this.contextData;
  }
  
  public Map<String, String> getContextMap() {
    return this.contextData.toMap();
  }
  
  public ThreadContext.ContextStack getContextStack() {
    return this.contextStack;
  }
  
  public StackTraceElement getSource() {
    if (this.source != null)
      return this.source; 
    if (this.loggerFqcn == null || !this.includeLocation)
      return null; 
    this.source = StackLocatorUtil.calcLocation(this.loggerFqcn);
    return this.source;
  }
  
  public boolean isIncludeLocation() {
    return this.includeLocation;
  }
  
  public void setIncludeLocation(boolean includeLocation) {
    this.includeLocation = includeLocation;
  }
  
  public boolean isEndOfBatch() {
    return this.endOfBatch;
  }
  
  public void setEndOfBatch(boolean endOfBatch) {
    this.endOfBatch = endOfBatch;
  }
  
  public long getNanoTime() {
    return this.nanoTime;
  }
  
  protected Object writeReplace() {
    getThrownProxy();
    return new LogEventProxy(this, this.includeLocation);
  }
  
  public static Serializable serialize(LogEvent event, boolean includeLocation) {
    if (event instanceof Log4jLogEvent) {
      event.getThrownProxy();
      return new LogEventProxy((Log4jLogEvent)event, includeLocation);
    } 
    return new LogEventProxy(event, includeLocation);
  }
  
  public static Serializable serialize(Log4jLogEvent event, boolean includeLocation) {
    event.getThrownProxy();
    return new LogEventProxy(event, includeLocation);
  }
  
  public static boolean canDeserialize(Serializable event) {
    return event instanceof LogEventProxy;
  }
  
  public static Log4jLogEvent deserialize(Serializable event) {
    Objects.requireNonNull(event, "Event cannot be null");
    if (event instanceof LogEventProxy) {
      LogEventProxy proxy = (LogEventProxy)event;
      Log4jLogEvent result = new Log4jLogEvent(proxy.loggerName, proxy.marker, proxy.loggerFQCN, proxy.level, proxy.message, proxy.thrown, proxy.thrownProxy, proxy.contextData, proxy.contextStack, proxy.threadId, proxy.threadName, proxy.threadPriority, proxy.source, proxy.timeMillis, proxy.nanoOfMillisecond, proxy.nanoTime);
      result.setEndOfBatch(proxy.isEndOfBatch);
      result.setIncludeLocation(proxy.isLocationRequired);
      return result;
    } 
    throw new IllegalArgumentException("Event is not a serialized LogEvent: " + event.toString());
  }
  
  private void readObject(ObjectInputStream stream) throws InvalidObjectException {
    throw new InvalidObjectException("Proxy required");
  }
  
  public static LogEvent createMemento(LogEvent logEvent) {
    return (new Builder(logEvent)).build();
  }
  
  public static Log4jLogEvent createMemento(LogEvent event, boolean includeLocation) {
    return deserialize(serialize(event, includeLocation));
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    String n = this.loggerName.isEmpty() ? "root" : this.loggerName;
    sb.append("Logger=").append(n);
    sb.append(" Level=").append(this.level.name());
    sb.append(" Message=").append((this.message == null) ? null : this.message.getFormattedMessage());
    return sb.toString();
  }
  
  public boolean equals(Object o) {
    if (this == o)
      return true; 
    if (o == null || getClass() != o.getClass())
      return false; 
    Log4jLogEvent that = (Log4jLogEvent)o;
    if (this.endOfBatch != that.endOfBatch)
      return false; 
    if (this.includeLocation != that.includeLocation)
      return false; 
    if (!this.instant.equals(that.instant))
      return false; 
    if (this.nanoTime != that.nanoTime)
      return false; 
    if ((this.loggerFqcn != null) ? !this.loggerFqcn.equals(that.loggerFqcn) : (that.loggerFqcn != null))
      return false; 
    if ((this.level != null) ? !this.level.equals(that.level) : (that.level != null))
      return false; 
    if ((this.source != null) ? !this.source.equals(that.source) : (that.source != null))
      return false; 
    if ((this.marker != null) ? !this.marker.equals(that.marker) : (that.marker != null))
      return false; 
    if ((this.contextData != null) ? !this.contextData.equals(that.contextData) : (that.contextData != null))
      return false; 
    if (!this.message.equals(that.message))
      return false; 
    if (!this.loggerName.equals(that.loggerName))
      return false; 
    if ((this.contextStack != null) ? !this.contextStack.equals(that.contextStack) : (that.contextStack != null))
      return false; 
    if (this.threadId != that.threadId)
      return false; 
    if ((this.threadName != null) ? !this.threadName.equals(that.threadName) : (that.threadName != null))
      return false; 
    if (this.threadPriority != that.threadPriority)
      return false; 
    if ((this.thrown != null) ? !this.thrown.equals(that.thrown) : (that.thrown != null))
      return false; 
    if ((this.thrownProxy != null) ? !this.thrownProxy.equals(that.thrownProxy) : (that.thrownProxy != null))
      return false; 
    return true;
  }
  
  public int hashCode() {
    int result = (this.loggerFqcn != null) ? this.loggerFqcn.hashCode() : 0;
    result = 31 * result + ((this.marker != null) ? this.marker.hashCode() : 0);
    result = 31 * result + ((this.level != null) ? this.level.hashCode() : 0);
    result = 31 * result + this.loggerName.hashCode();
    result = 31 * result + this.message.hashCode();
    result = 31 * result + this.instant.hashCode();
    result = 31 * result + (int)(this.nanoTime ^ this.nanoTime >>> 32L);
    result = 31 * result + ((this.thrown != null) ? this.thrown.hashCode() : 0);
    result = 31 * result + ((this.thrownProxy != null) ? this.thrownProxy.hashCode() : 0);
    result = 31 * result + ((this.contextData != null) ? this.contextData.hashCode() : 0);
    result = 31 * result + ((this.contextStack != null) ? this.contextStack.hashCode() : 0);
    result = 31 * result + (int)(this.threadId ^ this.threadId >>> 32L);
    result = 31 * result + ((this.threadName != null) ? this.threadName.hashCode() : 0);
    result = 31 * result + (this.threadPriority ^ this.threadPriority >>> 32);
    result = 31 * result + ((this.source != null) ? this.source.hashCode() : 0);
    result = 31 * result + (this.includeLocation ? 1 : 0);
    result = 31 * result + (this.endOfBatch ? 1 : 0);
    return result;
  }
  
  static class LogEventProxy implements Serializable {
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
    
    public LogEventProxy(Log4jLogEvent event, boolean includeLocation) {
      this.loggerFQCN = event.loggerFqcn;
      this.marker = event.marker;
      this.level = event.level;
      this.loggerName = event.loggerName;
      this
        
        .message = (event.message instanceof ReusableMessage) ? memento((ReusableMessage)event.message) : event.message;
      this.timeMillis = event.instant.getEpochMillisecond();
      this.nanoOfMillisecond = event.instant.getNanoOfMillisecond();
      this.thrown = event.thrown;
      this.thrownProxy = event.thrownProxy;
      this.contextData = event.contextData;
      this.contextStack = event.contextStack;
      this.source = includeLocation ? event.getSource() : null;
      this.threadId = event.getThreadId();
      this.threadName = event.getThreadName();
      this.threadPriority = event.getThreadPriority();
      this.isLocationRequired = includeLocation;
      this.isEndOfBatch = event.endOfBatch;
      this.nanoTime = event.nanoTime;
    }
    
    public LogEventProxy(LogEvent event, boolean includeLocation) {
      this.loggerFQCN = event.getLoggerFqcn();
      this.marker = event.getMarker();
      this.level = event.getLevel();
      this.loggerName = event.getLoggerName();
      Message temp = event.getMessage();
      this
        .message = (temp instanceof ReusableMessage) ? memento((ReusableMessage)temp) : temp;
      this.timeMillis = event.getInstant().getEpochMillisecond();
      this.nanoOfMillisecond = event.getInstant().getNanoOfMillisecond();
      this.thrown = event.getThrown();
      this.thrownProxy = event.getThrownProxy();
      this.contextData = memento(event.getContextData());
      this.contextStack = event.getContextStack();
      this.source = includeLocation ? event.getSource() : null;
      this.threadId = event.getThreadId();
      this.threadName = event.getThreadName();
      this.threadPriority = event.getThreadPriority();
      this.isLocationRequired = includeLocation;
      this.isEndOfBatch = event.isEndOfBatch();
      this.nanoTime = event.getNanoTime();
    }
    
    private static Message memento(ReusableMessage message) {
      return message.memento();
    }
    
    private static StringMap memento(ReadOnlyStringMap data) {
      StringMap result = ContextDataFactory.createContextData();
      result.putAll(data);
      return result;
    }
    
    private static MarshalledObject<Message> marshall(Message msg) {
      try {
        return new MarshalledObject<>(msg);
      } catch (Exception ex) {
        return null;
      } 
    }
    
    private void writeObject(ObjectOutputStream s) throws IOException {
      this.messageString = this.message.getFormattedMessage();
      this.marshalledMessage = marshall(this.message);
      s.defaultWriteObject();
    }
    
    protected Object readResolve() {
      Log4jLogEvent result = new Log4jLogEvent(this.loggerName, this.marker, this.loggerFQCN, this.level, message(), this.thrown, this.thrownProxy, this.contextData, this.contextStack, this.threadId, this.threadName, this.threadPriority, this.source, this.timeMillis, this.nanoOfMillisecond, this.nanoTime);
      result.setEndOfBatch(this.isEndOfBatch);
      result.setIncludeLocation(this.isLocationRequired);
      return result;
    }
    
    private Message message() {
      if (this.marshalledMessage != null)
        try {
          return this.marshalledMessage.get();
        } catch (Exception exception) {} 
      return (Message)new SimpleMessage(this.messageString);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\impl\Log4jLogEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */