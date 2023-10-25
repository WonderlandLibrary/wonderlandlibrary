package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.dsl.Disruptor;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.ContextDataInjector;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.ReliabilityStrategy;
import org.apache.logging.log4j.core.impl.ContextDataFactory;
import org.apache.logging.log4j.core.impl.ContextDataInjectorFactory;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.core.util.ClockFactory;
import org.apache.logging.log4j.core.util.NanoClock;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.StackLocatorUtil;
import org.apache.logging.log4j.util.StringMap;
import org.apache.logging.log4j.util.Supplier;

public class AsyncLogger extends Logger implements EventTranslatorVararg<RingBufferLogEvent> {
  private static final StatusLogger LOGGER = StatusLogger.getLogger();
  
  private static final Clock CLOCK = ClockFactory.getClock();
  
  private static final ContextDataInjector CONTEXT_DATA_INJECTOR = ContextDataInjectorFactory.createInjector();
  
  private static final ThreadNameCachingStrategy THREAD_NAME_CACHING_STRATEGY = ThreadNameCachingStrategy.create();
  
  private final ThreadLocal<RingBufferLogEventTranslator> threadLocalTranslator = new ThreadLocal<>();
  
  private final AsyncLoggerDisruptor loggerDisruptor;
  
  private volatile boolean includeLocation;
  
  private volatile NanoClock nanoClock;
  
  private final TranslatorType threadLocalTranslatorType;
  
  private final TranslatorType varargTranslatorType;
  
  public AsyncLogger(LoggerContext context, String name, MessageFactory messageFactory, AsyncLoggerDisruptor loggerDisruptor) {
    super(context, name, messageFactory);
    this.threadLocalTranslatorType = new TranslatorType() {
        void log(String fqcn, StackTraceElement location, Level level, Marker marker, Message message, Throwable thrown) {
          AsyncLogger.this.logWithThreadLocalTranslator(fqcn, location, level, marker, message, thrown);
        }
        
        void log(String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
          AsyncLogger.this.logWithThreadLocalTranslator(fqcn, level, marker, message, thrown);
        }
      };
    this.varargTranslatorType = new TranslatorType() {
        void log(String fqcn, StackTraceElement location, Level level, Marker marker, Message message, Throwable thrown) {
          AsyncLogger.this.logWithVarargTranslator(fqcn, location, level, marker, message, thrown);
        }
        
        void log(String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
          AsyncLogger.this.logWithVarargTranslator(fqcn, level, marker, message, thrown);
        }
      };
    this.loggerDisruptor = loggerDisruptor;
    this.includeLocation = this.privateConfig.loggerConfig.isIncludeLocation();
    this.nanoClock = context.getConfiguration().getNanoClock();
  }
  
  protected void updateConfiguration(Configuration newConfig) {
    this.nanoClock = newConfig.getNanoClock();
    this.includeLocation = newConfig.getLoggerConfig(this.name).isIncludeLocation();
    super.updateConfiguration(newConfig);
  }
  
  NanoClock getNanoClock() {
    return this.nanoClock;
  }
  
  private RingBufferLogEventTranslator getCachedTranslator() {
    RingBufferLogEventTranslator result = this.threadLocalTranslator.get();
    if (result == null) {
      result = new RingBufferLogEventTranslator();
      this.threadLocalTranslator.set(result);
    } 
    return result;
  }
  
  public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
    getTranslatorType().log(fqcn, level, marker, message, thrown);
  }
  
  public void log(Level level, Marker marker, String fqcn, StackTraceElement location, Message message, Throwable throwable) {
    getTranslatorType().log(fqcn, location, level, marker, message, throwable);
  }
  
  abstract class TranslatorType {
    abstract void log(String param1String, StackTraceElement param1StackTraceElement, Level param1Level, Marker param1Marker, Message param1Message, Throwable param1Throwable);
    
    abstract void log(String param1String, Level param1Level, Marker param1Marker, Message param1Message, Throwable param1Throwable);
  }
  
  private TranslatorType getTranslatorType() {
    return this.loggerDisruptor.isUseThreadLocals() ? this.threadLocalTranslatorType : this.varargTranslatorType;
  }
  
  private boolean isReused(Message message) {
    return message instanceof org.apache.logging.log4j.message.ReusableMessage;
  }
  
  private void logWithThreadLocalTranslator(String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
    RingBufferLogEventTranslator translator = getCachedTranslator();
    initTranslator(translator, fqcn, level, marker, message, thrown);
    initTranslatorThreadValues(translator);
    publish(translator);
  }
  
  private void logWithThreadLocalTranslator(String fqcn, StackTraceElement location, Level level, Marker marker, Message message, Throwable thrown) {
    RingBufferLogEventTranslator translator = getCachedTranslator();
    initTranslator(translator, fqcn, location, level, marker, message, thrown);
    initTranslatorThreadValues(translator);
    publish(translator);
  }
  
  private void publish(RingBufferLogEventTranslator translator) {
    if (!this.loggerDisruptor.tryPublish(translator))
      handleRingBufferFull(translator); 
  }
  
  private void handleRingBufferFull(RingBufferLogEventTranslator translator) {
    if (AbstractLogger.getRecursionDepth() > 1) {
      AsyncQueueFullMessageUtil.logWarningToStatusLogger();
      logMessageInCurrentThread(translator.fqcn, translator.level, translator.marker, translator.message, translator.thrown);
      translator.clear();
      return;
    } 
    EventRoute eventRoute = this.loggerDisruptor.getEventRoute(translator.level);
    switch (eventRoute) {
      case ENQUEUE:
        this.loggerDisruptor.enqueueLogMessageWhenQueueFull(translator);
        return;
      case SYNCHRONOUS:
        logMessageInCurrentThread(translator.fqcn, translator.level, translator.marker, translator.message, translator.thrown);
        translator.clear();
        return;
      case DISCARD:
        translator.clear();
        return;
    } 
    throw new IllegalStateException("Unknown EventRoute " + eventRoute);
  }
  
  private void initTranslator(RingBufferLogEventTranslator translator, String fqcn, StackTraceElement location, Level level, Marker marker, Message message, Throwable thrown) {
    translator.setBasicValues(this, this.name, marker, fqcn, level, message, thrown, 
        
        ThreadContext.getImmutableStack(), location, CLOCK, this.nanoClock);
  }
  
  private void initTranslator(RingBufferLogEventTranslator translator, String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
    translator.setBasicValues(this, this.name, marker, fqcn, level, message, thrown, 
        
        ThreadContext.getImmutableStack(), 
        
        calcLocationIfRequested(fqcn), CLOCK, this.nanoClock);
  }
  
  private void initTranslatorThreadValues(RingBufferLogEventTranslator translator) {
    if (THREAD_NAME_CACHING_STRATEGY == ThreadNameCachingStrategy.UNCACHED)
      translator.updateThreadValues(); 
  }
  
  private StackTraceElement calcLocationIfRequested(String fqcn) {
    return this.includeLocation ? StackLocatorUtil.calcLocation(fqcn) : null;
  }
  
  private void logWithVarargTranslator(String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
    Disruptor<RingBufferLogEvent> disruptor = this.loggerDisruptor.getDisruptor();
    if (disruptor == null) {
      LOGGER.error("Ignoring log event after Log4j has been shut down.");
      return;
    } 
    if (!isReused(message))
      InternalAsyncUtil.makeMessageImmutable(message); 
    StackTraceElement location = null;
    if (!disruptor.getRingBuffer().tryPublishEvent(this, new Object[] { this, 
          
          location = calcLocationIfRequested(fqcn), fqcn, level, marker, message, thrown }))
      handleRingBufferFull(location, fqcn, level, marker, message, thrown); 
  }
  
  private void logWithVarargTranslator(String fqcn, StackTraceElement location, Level level, Marker marker, Message message, Throwable thrown) {
    Disruptor<RingBufferLogEvent> disruptor = this.loggerDisruptor.getDisruptor();
    if (disruptor == null) {
      LOGGER.error("Ignoring log event after Log4j has been shut down.");
      return;
    } 
    if (!isReused(message))
      InternalAsyncUtil.makeMessageImmutable(message); 
    if (!disruptor.getRingBuffer().tryPublishEvent(this, new Object[] { this, location, fqcn, level, marker, message, thrown }))
      handleRingBufferFull(location, fqcn, level, marker, message, thrown); 
  }
  
  public void translateTo(RingBufferLogEvent event, long sequence, Object... args) {
    AsyncLogger asyncLogger = (AsyncLogger)args[0];
    StackTraceElement location = (StackTraceElement)args[1];
    String fqcn = (String)args[2];
    Level level = (Level)args[3];
    Marker marker = (Marker)args[4];
    Message message = (Message)args[5];
    Throwable thrown = (Throwable)args[6];
    ThreadContext.ContextStack contextStack = ThreadContext.getImmutableStack();
    Thread currentThread = Thread.currentThread();
    String threadName = THREAD_NAME_CACHING_STRATEGY.getThreadName();
    event.setValues(asyncLogger, asyncLogger.getName(), marker, fqcn, level, message, thrown, CONTEXT_DATA_INJECTOR
        
        .injectContextData(null, (StringMap)event.getContextData()), contextStack, currentThread
        .getId(), threadName, currentThread.getPriority(), location, CLOCK, this.nanoClock);
  }
  
  void logMessageInCurrentThread(String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
    ReliabilityStrategy strategy = this.privateConfig.loggerConfig.getReliabilityStrategy();
    strategy.log((Supplier)this, getName(), fqcn, marker, level, message, thrown);
  }
  
  private void handleRingBufferFull(StackTraceElement location, String fqcn, Level level, Marker marker, Message msg, Throwable thrown) {
    if (AbstractLogger.getRecursionDepth() > 1) {
      AsyncQueueFullMessageUtil.logWarningToStatusLogger();
      logMessageInCurrentThread(fqcn, level, marker, msg, thrown);
      return;
    } 
    EventRoute eventRoute = this.loggerDisruptor.getEventRoute(level);
    switch (eventRoute) {
      case ENQUEUE:
        this.loggerDisruptor.enqueueLogMessageWhenQueueFull(this, this, location, fqcn, level, marker, msg, thrown);
      case SYNCHRONOUS:
        logMessageInCurrentThread(fqcn, level, marker, msg, thrown);
      case DISCARD:
        return;
    } 
    throw new IllegalStateException("Unknown EventRoute " + eventRoute);
  }
  
  public void actualAsyncLog(RingBufferLogEvent event) {
    LoggerConfig privateConfigLoggerConfig = this.privateConfig.loggerConfig;
    List<Property> properties = privateConfigLoggerConfig.getPropertyList();
    if (properties != null)
      onPropertiesPresent(event, properties); 
    privateConfigLoggerConfig.getReliabilityStrategy().log((Supplier)this, event);
  }
  
  private void onPropertiesPresent(RingBufferLogEvent event, List<Property> properties) {
    StringMap contextData = getContextData(event);
    for (int i = 0, size = properties.size(); i < size; i++) {
      Property prop = properties.get(i);
      if (contextData.getValue(prop.getName()) == null) {
        String value = prop.isValueNeedsLookup() ? this.privateConfig.config.getStrSubstitutor().replace(event, prop.getValue()) : prop.getValue();
        contextData.putValue(prop.getName(), value);
      } 
    } 
    event.setContextData(contextData);
  }
  
  private static StringMap getContextData(RingBufferLogEvent event) {
    StringMap contextData = (StringMap)event.getContextData();
    if (contextData.isFrozen()) {
      StringMap temp = ContextDataFactory.createContextData();
      temp.putAll((ReadOnlyStringMap)contextData);
      return temp;
    } 
    return contextData;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\async\AsyncLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */