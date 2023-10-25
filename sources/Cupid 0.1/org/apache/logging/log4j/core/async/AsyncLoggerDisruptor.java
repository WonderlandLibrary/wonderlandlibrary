package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.AbstractLifeCycle;
import org.apache.logging.log4j.core.jmx.RingBufferAdmin;
import org.apache.logging.log4j.core.util.Log4jThreadFactory;
import org.apache.logging.log4j.core.util.Throwables;
import org.apache.logging.log4j.message.Message;

class AsyncLoggerDisruptor extends AbstractLifeCycle {
  private static final int SLEEP_MILLIS_BETWEEN_DRAIN_ATTEMPTS = 50;
  
  private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 200;
  
  private final Object queueFullEnqueueLock = new Object();
  
  private volatile Disruptor<RingBufferLogEvent> disruptor;
  
  private String contextName;
  
  private boolean useThreadLocalTranslator = true;
  
  private long backgroundThreadId;
  
  private AsyncQueueFullPolicy asyncQueueFullPolicy;
  
  private int ringBufferSize;
  
  AsyncLoggerDisruptor(String contextName) {
    this.contextName = contextName;
  }
  
  public String getContextName() {
    return this.contextName;
  }
  
  public void setContextName(String name) {
    this.contextName = name;
  }
  
  Disruptor<RingBufferLogEvent> getDisruptor() {
    return this.disruptor;
  }
  
  public synchronized void start() {
    if (this.disruptor != null) {
      LOGGER.trace("[{}] AsyncLoggerDisruptor not starting new disruptor for this context, using existing object.", this.contextName);
      return;
    } 
    if (isStarting()) {
      LOGGER.trace("[{}] AsyncLoggerDisruptor is already starting.", this.contextName);
      return;
    } 
    setStarting();
    LOGGER.trace("[{}] AsyncLoggerDisruptor creating new disruptor for this context.", this.contextName);
    this.ringBufferSize = DisruptorUtil.calculateRingBufferSize("AsyncLogger.RingBufferSize");
    WaitStrategy waitStrategy = DisruptorUtil.createWaitStrategy("AsyncLogger.WaitStrategy");
    Log4jThreadFactory log4jThreadFactory = new Log4jThreadFactory("AsyncLogger[" + this.contextName + "]", true, 5) {
        public Thread newThread(Runnable r) {
          Thread result = super.newThread(r);
          AsyncLoggerDisruptor.this.backgroundThreadId = result.getId();
          return result;
        }
      };
    this.asyncQueueFullPolicy = AsyncQueueFullPolicyFactory.create();
    this.disruptor = new Disruptor(RingBufferLogEvent.FACTORY, this.ringBufferSize, (ThreadFactory)log4jThreadFactory, ProducerType.MULTI, waitStrategy);
    ExceptionHandler<RingBufferLogEvent> errorHandler = DisruptorUtil.getAsyncLoggerExceptionHandler();
    this.disruptor.setDefaultExceptionHandler(errorHandler);
    RingBufferLogEventHandler[] handlers = { new RingBufferLogEventHandler() };
    this.disruptor.handleEventsWith((EventHandler[])handlers);
    LOGGER.debug("[{}] Starting AsyncLogger disruptor for this context with ringbufferSize={}, waitStrategy={}, exceptionHandler={}...", this.contextName, 
        Integer.valueOf(this.disruptor.getRingBuffer().getBufferSize()), waitStrategy
        .getClass().getSimpleName(), errorHandler);
    this.disruptor.start();
    LOGGER.trace("[{}] AsyncLoggers use a {} translator", this.contextName, this.useThreadLocalTranslator ? "threadlocal" : "vararg");
    super.start();
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    Disruptor<RingBufferLogEvent> temp = getDisruptor();
    if (temp == null) {
      LOGGER.trace("[{}] AsyncLoggerDisruptor: disruptor for this context already shut down.", this.contextName);
      return true;
    } 
    setStopping();
    LOGGER.debug("[{}] AsyncLoggerDisruptor: shutting down disruptor for this context.", this.contextName);
    this.disruptor = null;
    for (int i = 0; hasBacklog(temp) && i < 200; i++) {
      try {
        Thread.sleep(50L);
      } catch (InterruptedException interruptedException) {}
    } 
    try {
      temp.shutdown(timeout, timeUnit);
    } catch (TimeoutException e) {
      LOGGER.warn("[{}] AsyncLoggerDisruptor: shutdown timed out after {} {}", this.contextName, Long.valueOf(timeout), timeUnit);
      temp.halt();
    } 
    LOGGER.trace("[{}] AsyncLoggerDisruptor: disruptor has been shut down.", this.contextName);
    if (DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy) > 0L)
      LOGGER.trace("AsyncLoggerDisruptor: {} discarded {} events.", this.asyncQueueFullPolicy, 
          Long.valueOf(DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy))); 
    setStopped();
    return true;
  }
  
  private static boolean hasBacklog(Disruptor<?> theDisruptor) {
    RingBuffer<?> ringBuffer = theDisruptor.getRingBuffer();
    return !ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize());
  }
  
  public RingBufferAdmin createRingBufferAdmin(String jmxContextName) {
    RingBuffer<RingBufferLogEvent> ring = (this.disruptor == null) ? null : this.disruptor.getRingBuffer();
    return RingBufferAdmin.forAsyncLogger(ring, jmxContextName);
  }
  
  EventRoute getEventRoute(Level logLevel) {
    int remainingCapacity = remainingDisruptorCapacity();
    if (remainingCapacity < 0)
      return EventRoute.DISCARD; 
    return this.asyncQueueFullPolicy.getRoute(this.backgroundThreadId, logLevel);
  }
  
  private int remainingDisruptorCapacity() {
    Disruptor<RingBufferLogEvent> temp = this.disruptor;
    if (hasLog4jBeenShutDown(temp))
      return -1; 
    return (int)temp.getRingBuffer().remainingCapacity();
  }
  
  private boolean hasLog4jBeenShutDown(Disruptor<RingBufferLogEvent> aDisruptor) {
    if (aDisruptor == null) {
      LOGGER.warn("Ignoring log event after log4j was shut down");
      return true;
    } 
    return false;
  }
  
  boolean tryPublish(RingBufferLogEventTranslator translator) {
    try {
      return this.disruptor.getRingBuffer().tryPublishEvent(translator);
    } catch (NullPointerException npe) {
      logWarningOnNpeFromDisruptorPublish(translator);
      return false;
    } 
  }
  
  void enqueueLogMessageWhenQueueFull(RingBufferLogEventTranslator translator) {
    try {
      if (synchronizeEnqueueWhenQueueFull()) {
        synchronized (this.queueFullEnqueueLock) {
          this.disruptor.publishEvent(translator);
        } 
      } else {
        this.disruptor.publishEvent(translator);
      } 
    } catch (NullPointerException npe) {
      logWarningOnNpeFromDisruptorPublish(translator);
    } 
  }
  
  void enqueueLogMessageWhenQueueFull(EventTranslatorVararg<RingBufferLogEvent> translator, AsyncLogger asyncLogger, StackTraceElement location, String fqcn, Level level, Marker marker, Message msg, Throwable thrown) {
    try {
      if (synchronizeEnqueueWhenQueueFull()) {
        synchronized (this.queueFullEnqueueLock) {
          this.disruptor.getRingBuffer().publishEvent(translator, new Object[] { asyncLogger, location, fqcn, level, marker, msg, thrown });
        } 
      } else {
        this.disruptor.getRingBuffer().publishEvent(translator, new Object[] { asyncLogger, location, fqcn, level, marker, msg, thrown });
      } 
    } catch (NullPointerException npe) {
      logWarningOnNpeFromDisruptorPublish(level, fqcn, msg, thrown);
    } 
  }
  
  private boolean synchronizeEnqueueWhenQueueFull() {
    if (DisruptorUtil.ASYNC_LOGGER_SYNCHRONIZE_ENQUEUE_WHEN_QUEUE_FULL && this.backgroundThreadId != 
      
      Thread.currentThread().getId())
      if (!(Thread.currentThread() instanceof org.apache.logging.log4j.core.util.Log4jThread)); 
    return false;
  }
  
  private void logWarningOnNpeFromDisruptorPublish(RingBufferLogEventTranslator translator) {
    logWarningOnNpeFromDisruptorPublish(translator.level, translator.loggerName, translator.message, translator.thrown);
  }
  
  private void logWarningOnNpeFromDisruptorPublish(Level level, String fqcn, Message msg, Throwable thrown) {
    LOGGER.warn("[{}] Ignoring log event after log4j was shut down: {} [{}] {}{}", this.contextName, level, fqcn, msg
        .getFormattedMessage(), (thrown == null) ? "" : Throwables.toStringList(thrown));
  }
  
  public boolean isUseThreadLocals() {
    return this.useThreadLocalTranslator;
  }
  
  public void setUseThreadLocals(boolean allow) {
    this.useThreadLocalTranslator = allow;
    LOGGER.trace("[{}] AsyncLoggers have been modified to use a {} translator", this.contextName, this.useThreadLocalTranslator ? "threadlocal" : "vararg");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\async\AsyncLoggerDisruptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */