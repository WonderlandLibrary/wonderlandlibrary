package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.TimeoutBlockingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Constants;
import org.apache.logging.log4j.core.util.Integers;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.Strings;

final class DisruptorUtil {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static final int RINGBUFFER_MIN_SIZE = 128;
  
  private static final int RINGBUFFER_DEFAULT_SIZE = 262144;
  
  private static final int RINGBUFFER_NO_GC_DEFAULT_SIZE = 4096;
  
  static final boolean ASYNC_LOGGER_SYNCHRONIZE_ENQUEUE_WHEN_QUEUE_FULL = PropertiesUtil.getProperties()
    .getBooleanProperty("AsyncLogger.SynchronizeEnqueueWhenQueueFull", true);
  
  static final boolean ASYNC_CONFIG_SYNCHRONIZE_ENQUEUE_WHEN_QUEUE_FULL = PropertiesUtil.getProperties()
    .getBooleanProperty("AsyncLoggerConfig.SynchronizeEnqueueWhenQueueFull", true);
  
  static WaitStrategy createWaitStrategy(String propertyName) {
    long sleepTimeNs;
    String key;
    int retries;
    String strategy = PropertiesUtil.getProperties().getStringProperty(propertyName, "Timeout");
    LOGGER.trace("property {}={}", propertyName, strategy);
    String strategyUp = Strings.toRootUpperCase(strategy);
    long timeoutMillis = parseAdditionalLongProperty(propertyName, "Timeout", 10L);
    switch (strategyUp) {
      case "SLEEP":
        sleepTimeNs = parseAdditionalLongProperty(propertyName, "SleepTimeNs", 100L);
        key = getFullPropertyKey(propertyName, "Retries");
        retries = PropertiesUtil.getProperties().getIntegerProperty(key, 200);
        return (WaitStrategy)new SleepingWaitStrategy(retries, sleepTimeNs);
      case "YIELD":
        return (WaitStrategy)new YieldingWaitStrategy();
      case "BLOCK":
        return (WaitStrategy)new BlockingWaitStrategy();
      case "BUSYSPIN":
        return (WaitStrategy)new BusySpinWaitStrategy();
      case "TIMEOUT":
        return (WaitStrategy)new TimeoutBlockingWaitStrategy(timeoutMillis, TimeUnit.MILLISECONDS);
    } 
    return (WaitStrategy)new TimeoutBlockingWaitStrategy(timeoutMillis, TimeUnit.MILLISECONDS);
  }
  
  private static String getFullPropertyKey(String strategyKey, String additionalKey) {
    return strategyKey.startsWith("AsyncLogger.") ? ("AsyncLogger." + additionalKey) : ("AsyncLoggerConfig." + additionalKey);
  }
  
  private static long parseAdditionalLongProperty(String propertyName, String additionalKey, long defaultValue) {
    String key = getFullPropertyKey(propertyName, additionalKey);
    return PropertiesUtil.getProperties().getLongProperty(key, defaultValue);
  }
  
  static int calculateRingBufferSize(String propertyName) {
    int ringBufferSize = Constants.ENABLE_THREADLOCALS ? 4096 : 262144;
    String userPreferredRBSize = PropertiesUtil.getProperties().getStringProperty(propertyName, 
        String.valueOf(ringBufferSize));
    try {
      int size = Integer.parseInt(userPreferredRBSize);
      if (size < 128) {
        size = 128;
        LOGGER.warn("Invalid RingBufferSize {}, using minimum size {}.", userPreferredRBSize, 
            Integer.valueOf(128));
      } 
      ringBufferSize = size;
    } catch (Exception ex) {
      LOGGER.warn("Invalid RingBufferSize {}, using default size {}.", userPreferredRBSize, Integer.valueOf(ringBufferSize));
    } 
    return Integers.ceilingNextPowerOfTwo(ringBufferSize);
  }
  
  static ExceptionHandler<RingBufferLogEvent> getAsyncLoggerExceptionHandler() {
    String cls = PropertiesUtil.getProperties().getStringProperty("AsyncLogger.ExceptionHandler");
    if (cls == null)
      return new AsyncLoggerDefaultExceptionHandler(); 
    try {
      Class<? extends ExceptionHandler<RingBufferLogEvent>> klass = Loader.loadClass(cls);
      return klass.newInstance();
    } catch (Exception ignored) {
      LOGGER.debug("Invalid AsyncLogger.ExceptionHandler value: error creating {}: ", cls, ignored);
      return new AsyncLoggerDefaultExceptionHandler();
    } 
  }
  
  static ExceptionHandler<AsyncLoggerConfigDisruptor.Log4jEventWrapper> getAsyncLoggerConfigExceptionHandler() {
    String cls = PropertiesUtil.getProperties().getStringProperty("AsyncLoggerConfig.ExceptionHandler");
    if (cls == null)
      return new AsyncLoggerConfigDefaultExceptionHandler(); 
    try {
      Class<? extends ExceptionHandler<AsyncLoggerConfigDisruptor.Log4jEventWrapper>> klass = Loader.loadClass(cls);
      return klass.newInstance();
    } catch (Exception ignored) {
      LOGGER.debug("Invalid AsyncLoggerConfig.ExceptionHandler value: error creating {}: ", cls, ignored);
      return new AsyncLoggerConfigDefaultExceptionHandler();
    } 
  }
  
  public static long getExecutorThreadId(ExecutorService executor) {
    Future<Long> result = executor.submit(() -> Long.valueOf(Thread.currentThread().getId()));
    try {
      return ((Long)result.get()).longValue();
    } catch (Exception ex) {
      String msg = "Could not obtain executor thread Id. Giving up to avoid the risk of application deadlock.";
      throw new IllegalStateException("Could not obtain executor thread Id. Giving up to avoid the risk of application deadlock.", ex);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\async\DisruptorUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */