package org.apache.logging.log4j.core.impl;

import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.ContextDataInjector;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.async.ThreadNameCachingStrategy;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.core.util.ClockFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.StringMap;

public class ReusableLogEventFactory implements LogEventFactory, LocationAwareLogEventFactory {
  private static final ThreadNameCachingStrategy THREAD_NAME_CACHING_STRATEGY = ThreadNameCachingStrategy.create();
  
  private static final Clock CLOCK = ClockFactory.getClock();
  
  private static final ThreadLocal<MutableLogEvent> mutableLogEventThreadLocal = new ThreadLocal<>();
  
  private final ContextDataInjector injector = ContextDataInjectorFactory.createInjector();
  
  public LogEvent createEvent(String loggerName, Marker marker, String fqcn, Level level, Message message, List<Property> properties, Throwable t) {
    return createEvent(loggerName, marker, fqcn, null, level, message, properties, t);
  }
  
  public LogEvent createEvent(String loggerName, Marker marker, String fqcn, StackTraceElement location, Level level, Message message, List<Property> properties, Throwable t) {
    MutableLogEvent result = getOrCreateMutableLogEvent();
    result.reserved = true;
    result.setLoggerName(loggerName);
    result.setMarker(marker);
    result.setLoggerFqcn(fqcn);
    result.setLevel((level == null) ? Level.OFF : level);
    result.setMessage(message);
    result.initTime(CLOCK, Log4jLogEvent.getNanoClock());
    result.setThrown(t);
    result.setSource(location);
    result.setContextData(this.injector.injectContextData(properties, (StringMap)result.getContextData()));
    result.setContextStack((ThreadContext.getDepth() == 0) ? (ThreadContext.ContextStack)ThreadContext.EMPTY_STACK : ThreadContext.cloneStack());
    if (THREAD_NAME_CACHING_STRATEGY == ThreadNameCachingStrategy.UNCACHED) {
      result.setThreadName(Thread.currentThread().getName());
      result.setThreadPriority(Thread.currentThread().getPriority());
    } 
    return result;
  }
  
  private static MutableLogEvent getOrCreateMutableLogEvent() {
    MutableLogEvent result = mutableLogEventThreadLocal.get();
    return (result == null || result.reserved) ? createInstance(result) : result;
  }
  
  private static MutableLogEvent createInstance(MutableLogEvent existing) {
    MutableLogEvent result = new MutableLogEvent();
    result.setThreadId(Thread.currentThread().getId());
    result.setThreadName(Thread.currentThread().getName());
    result.setThreadPriority(Thread.currentThread().getPriority());
    if (existing == null)
      mutableLogEventThreadLocal.set(result); 
    return result;
  }
  
  public static void release(LogEvent logEvent) {
    if (logEvent instanceof MutableLogEvent) {
      MutableLogEvent mutableLogEvent = (MutableLogEvent)logEvent;
      mutableLogEvent.clear();
      mutableLogEvent.reserved = false;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\impl\ReusableLogEventFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */