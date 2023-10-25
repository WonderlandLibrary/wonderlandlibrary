package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.EventTranslator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.ContextDataInjector;
import org.apache.logging.log4j.core.impl.ContextDataInjectorFactory;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.core.util.NanoClock;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.StringMap;

public class RingBufferLogEventTranslator implements EventTranslator<RingBufferLogEvent> {
  private static final ContextDataInjector INJECTOR = ContextDataInjectorFactory.createInjector();
  
  private AsyncLogger asyncLogger;
  
  String loggerName;
  
  protected Marker marker;
  
  protected String fqcn;
  
  protected Level level;
  
  protected Message message;
  
  protected Throwable thrown;
  
  private ThreadContext.ContextStack contextStack;
  
  private long threadId = Thread.currentThread().getId();
  
  private String threadName = Thread.currentThread().getName();
  
  private int threadPriority = Thread.currentThread().getPriority();
  
  private StackTraceElement location;
  
  private Clock clock;
  
  private NanoClock nanoClock;
  
  public void translateTo(RingBufferLogEvent event, long sequence) {
    try {
      event.setValues(this.asyncLogger, this.loggerName, this.marker, this.fqcn, this.level, this.message, this.thrown, INJECTOR
          
          .injectContextData(null, (StringMap)event.getContextData()), this.contextStack, this.threadId, this.threadName, this.threadPriority, this.location, this.clock, this.nanoClock);
    } finally {
      clear();
    } 
  }
  
  void clear() {
    setBasicValues(null, null, null, null, null, null, null, null, null, null, null);
  }
  
  public void setBasicValues(AsyncLogger anAsyncLogger, String aLoggerName, Marker aMarker, String theFqcn, Level aLevel, Message msg, Throwable aThrowable, ThreadContext.ContextStack aContextStack, StackTraceElement aLocation, Clock aClock, NanoClock aNanoClock) {
    this.asyncLogger = anAsyncLogger;
    this.loggerName = aLoggerName;
    this.marker = aMarker;
    this.fqcn = theFqcn;
    this.level = aLevel;
    this.message = msg;
    this.thrown = aThrowable;
    this.contextStack = aContextStack;
    this.location = aLocation;
    this.clock = aClock;
    this.nanoClock = aNanoClock;
  }
  
  public void updateThreadValues() {
    Thread currentThread = Thread.currentThread();
    this.threadId = currentThread.getId();
    this.threadName = currentThread.getName();
    this.threadPriority = currentThread.getPriority();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\async\RingBufferLogEventTranslator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */