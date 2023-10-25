package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceReportingEventHandler;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.AbstractLifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.impl.LogEventFactory;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.core.jmx.RingBufferAdmin;
import org.apache.logging.log4j.core.util.Log4jThreadFactory;
import org.apache.logging.log4j.core.util.Throwables;

public class AsyncLoggerConfigDisruptor extends AbstractLifeCycle implements AsyncLoggerConfigDelegate {
  private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 200;
  
  private static final int SLEEP_MILLIS_BETWEEN_DRAIN_ATTEMPTS = 50;
  
  public static class Log4jEventWrapper {
    private AsyncLoggerConfig loggerConfig;
    
    private LogEvent event;
    
    public Log4jEventWrapper() {}
    
    public Log4jEventWrapper(MutableLogEvent mutableLogEvent) {
      this.event = (LogEvent)mutableLogEvent;
    }
    
    public void clear() {
      this.loggerConfig = null;
      if (this.event instanceof MutableLogEvent) {
        ((MutableLogEvent)this.event).clear();
      } else {
        this.event = null;
      } 
    }
    
    public String toString() {
      return String.valueOf(this.event);
    }
  }
  
  private static class Log4jEventWrapperHandler implements SequenceReportingEventHandler<Log4jEventWrapper> {
    private static final int NOTIFY_PROGRESS_THRESHOLD = 50;
    
    private Sequence sequenceCallback;
    
    private int counter;
    
    private Log4jEventWrapperHandler() {}
    
    public void setSequenceCallback(Sequence sequenceCallback) {
      this.sequenceCallback = sequenceCallback;
    }
    
    public void onEvent(AsyncLoggerConfigDisruptor.Log4jEventWrapper event, long sequence, boolean endOfBatch) throws Exception {
      event.event.setEndOfBatch(endOfBatch);
      event.loggerConfig.logToAsyncLoggerConfigsOnCurrentThread(event.event);
      event.clear();
      notifyIntermediateProgress(sequence);
    }
    
    private void notifyIntermediateProgress(long sequence) {
      if (++this.counter > 50) {
        this.sequenceCallback.set(sequence);
        this.counter = 0;
      } 
    }
  }
  
  private static final EventFactory<Log4jEventWrapper> FACTORY = Log4jEventWrapper::new;
  
  private static final EventFactory<Log4jEventWrapper> MUTABLE_FACTORY = () -> new Log4jEventWrapper(new MutableLogEvent());
  
  private static final EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig> TRANSLATOR;
  
  private static final EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig> MUTABLE_TRANSLATOR;
  
  private int ringBufferSize;
  
  private AsyncQueueFullPolicy asyncQueueFullPolicy;
  
  static {
    TRANSLATOR = ((ringBufferElement, sequence, logEvent, loggerConfig) -> {
        ringBufferElement.event = logEvent;
        ringBufferElement.loggerConfig = loggerConfig;
      });
    MUTABLE_TRANSLATOR = ((ringBufferElement, sequence, logEvent, loggerConfig) -> {
        ((MutableLogEvent)ringBufferElement.event).initFrom(logEvent);
        ringBufferElement.loggerConfig = loggerConfig;
      });
  }
  
  private Boolean mutable = Boolean.FALSE;
  
  private volatile Disruptor<Log4jEventWrapper> disruptor;
  
  private long backgroundThreadId;
  
  private EventFactory<Log4jEventWrapper> factory;
  
  private EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig> translator;
  
  private volatile boolean alreadyLoggedWarning;
  
  private final Object queueFullEnqueueLock = new Object();
  
  public void setLogEventFactory(LogEventFactory logEventFactory) {
    this.mutable = Boolean.valueOf((this.mutable.booleanValue() || logEventFactory instanceof org.apache.logging.log4j.core.impl.ReusableLogEventFactory));
  }
  
  public synchronized void start() {
    if (this.disruptor != null) {
      LOGGER.trace("AsyncLoggerConfigDisruptor not starting new disruptor for this configuration, using existing object.");
      return;
    } 
    LOGGER.trace("AsyncLoggerConfigDisruptor creating new disruptor for this configuration.");
    this.ringBufferSize = DisruptorUtil.calculateRingBufferSize("AsyncLoggerConfig.RingBufferSize");
    WaitStrategy waitStrategy = DisruptorUtil.createWaitStrategy("AsyncLoggerConfig.WaitStrategy");
    Log4jThreadFactory log4jThreadFactory = new Log4jThreadFactory("AsyncLoggerConfig", true, 5) {
        public Thread newThread(Runnable r) {
          Thread result = super.newThread(r);
          AsyncLoggerConfigDisruptor.this.backgroundThreadId = result.getId();
          return result;
        }
      };
    this.asyncQueueFullPolicy = AsyncQueueFullPolicyFactory.create();
    this.translator = this.mutable.booleanValue() ? MUTABLE_TRANSLATOR : TRANSLATOR;
    this.factory = this.mutable.booleanValue() ? MUTABLE_FACTORY : FACTORY;
    this.disruptor = new Disruptor(this.factory, this.ringBufferSize, (ThreadFactory)log4jThreadFactory, ProducerType.MULTI, waitStrategy);
    ExceptionHandler<Log4jEventWrapper> errorHandler = DisruptorUtil.getAsyncLoggerConfigExceptionHandler();
    this.disruptor.setDefaultExceptionHandler(errorHandler);
    Log4jEventWrapperHandler[] handlers = { new Log4jEventWrapperHandler() };
    this.disruptor.handleEventsWith((EventHandler[])handlers);
    LOGGER.debug("Starting AsyncLoggerConfig disruptor for this configuration with ringbufferSize={}, waitStrategy={}, exceptionHandler={}...", 
        Integer.valueOf(this.disruptor.getRingBuffer().getBufferSize()), waitStrategy
        .getClass().getSimpleName(), errorHandler);
    this.disruptor.start();
    super.start();
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    Disruptor<Log4jEventWrapper> temp = this.disruptor;
    if (temp == null) {
      LOGGER.trace("AsyncLoggerConfigDisruptor: disruptor for this configuration already shut down.");
      return true;
    } 
    setStopping();
    LOGGER.trace("AsyncLoggerConfigDisruptor: shutting down disruptor for this configuration.");
    this.disruptor = null;
    for (int i = 0; hasBacklog(temp) && i < 200; i++) {
      try {
        Thread.sleep(50L);
      } catch (InterruptedException interruptedException) {}
    } 
    try {
      temp.shutdown(timeout, timeUnit);
    } catch (TimeoutException e) {
      LOGGER.warn("AsyncLoggerConfigDisruptor: shutdown timed out after {} {}", Long.valueOf(timeout), timeUnit);
      temp.halt();
    } 
    LOGGER.trace("AsyncLoggerConfigDisruptor: disruptor has been shut down.");
    if (DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy) > 0L)
      LOGGER.trace("AsyncLoggerConfigDisruptor: {} discarded {} events.", this.asyncQueueFullPolicy, 
          Long.valueOf(DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy))); 
    setStopped();
    return true;
  }
  
  private static boolean hasBacklog(Disruptor<?> theDisruptor) {
    RingBuffer<?> ringBuffer = theDisruptor.getRingBuffer();
    return !ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize());
  }
  
  public EventRoute getEventRoute(Level logLevel) {
    int remainingCapacity = remainingDisruptorCapacity();
    if (remainingCapacity < 0)
      return EventRoute.DISCARD; 
    return this.asyncQueueFullPolicy.getRoute(this.backgroundThreadId, logLevel);
  }
  
  private int remainingDisruptorCapacity() {
    Disruptor<Log4jEventWrapper> temp = this.disruptor;
    if (hasLog4jBeenShutDown(temp))
      return -1; 
    return (int)temp.getRingBuffer().remainingCapacity();
  }
  
  private boolean hasLog4jBeenShutDown(Disruptor<Log4jEventWrapper> aDisruptor) {
    if (aDisruptor == null) {
      LOGGER.warn("Ignoring log event after log4j was shut down");
      return true;
    } 
    return false;
  }
  
  public void enqueueEvent(LogEvent event, AsyncLoggerConfig asyncLoggerConfig) {
    try {
      LogEvent logEvent = prepareEvent(event);
      enqueue(logEvent, asyncLoggerConfig);
    } catch (NullPointerException npe) {
      LOGGER.warn("Ignoring log event after log4j was shut down: {} [{}] {}", event.getLevel(), event
          .getLoggerName(), event.getMessage().getFormattedMessage() + (
          (event.getThrown() == null) ? "" : Throwables.toStringList(event.getThrown())));
    } 
  }
  
  private LogEvent prepareEvent(LogEvent event) {
    Log4jLogEvent log4jLogEvent;
    LogEvent logEvent = ensureImmutable(event);
    if (logEvent.getMessage() instanceof org.apache.logging.log4j.message.ReusableMessage) {
      if (logEvent instanceof Log4jLogEvent) {
        ((Log4jLogEvent)logEvent).makeMessageImmutable();
      } else if (logEvent instanceof MutableLogEvent) {
        if (this.translator != MUTABLE_TRANSLATOR)
          log4jLogEvent = ((MutableLogEvent)logEvent).createMemento(); 
      } else {
        showWarningAboutCustomLogEventWithReusableMessage((LogEvent)log4jLogEvent);
      } 
    } else {
      InternalAsyncUtil.makeMessageImmutable(log4jLogEvent.getMessage());
    } 
    return (LogEvent)log4jLogEvent;
  }
  
  private void showWarningAboutCustomLogEventWithReusableMessage(LogEvent logEvent) {
    if (!this.alreadyLoggedWarning) {
      LOGGER.warn("Custom log event of type {} contains a mutable message of type {}. AsyncLoggerConfig does not know how to make an immutable copy of this message. This may result in ConcurrentModificationExceptions or incorrect log messages if the application modifies objects in the message while the background thread is writing it to the appenders.", logEvent
          
          .getClass().getName(), logEvent.getMessage().getClass().getName());
      this.alreadyLoggedWarning = true;
    } 
  }
  
  private void enqueue(LogEvent logEvent, AsyncLoggerConfig asyncLoggerConfig) {
    if (synchronizeEnqueueWhenQueueFull()) {
      synchronized (this.queueFullEnqueueLock) {
        this.disruptor.getRingBuffer().publishEvent(this.translator, logEvent, asyncLoggerConfig);
      } 
    } else {
      this.disruptor.getRingBuffer().publishEvent(this.translator, logEvent, asyncLoggerConfig);
    } 
  }
  
  private boolean synchronizeEnqueueWhenQueueFull() {
    if (DisruptorUtil.ASYNC_CONFIG_SYNCHRONIZE_ENQUEUE_WHEN_QUEUE_FULL && this.backgroundThreadId != 
      
      Thread.currentThread().getId())
      if (!(Thread.currentThread() instanceof org.apache.logging.log4j.core.util.Log4jThread)); 
    return false;
  }
  
  public boolean tryEnqueue(LogEvent event, AsyncLoggerConfig asyncLoggerConfig) {
    LogEvent logEvent = prepareEvent(event);
    return this.disruptor.getRingBuffer().tryPublishEvent(this.translator, logEvent, asyncLoggerConfig);
  }
  
  private LogEvent ensureImmutable(LogEvent event) {
    LogEvent result = event;
    if (event instanceof RingBufferLogEvent)
      result = ((RingBufferLogEvent)event).createMemento(); 
    return result;
  }
  
  public RingBufferAdmin createRingBufferAdmin(String contextName, String loggerConfigName) {
    return RingBufferAdmin.forAsyncLoggerConfig(this.disruptor.getRingBuffer(), contextName, loggerConfigName);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\async\AsyncLoggerConfigDisruptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */