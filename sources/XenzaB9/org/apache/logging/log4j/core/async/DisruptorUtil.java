// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import org.apache.logging.log4j.core.util.Loader;
import com.lmax.disruptor.ExceptionHandler;
import org.apache.logging.log4j.core.util.Integers;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.core.util.Constants;
import com.lmax.disruptor.WaitStrategy;
import org.apache.logging.log4j.Logger;

final class DisruptorUtil
{
    private static final Logger LOGGER;
    private static final int RINGBUFFER_MIN_SIZE = 128;
    private static final int RINGBUFFER_DEFAULT_SIZE = 262144;
    private static final int RINGBUFFER_NO_GC_DEFAULT_SIZE = 4096;
    static final boolean ASYNC_LOGGER_SYNCHRONIZE_ENQUEUE_WHEN_QUEUE_FULL;
    static final boolean ASYNC_CONFIG_SYNCHRONIZE_ENQUEUE_WHEN_QUEUE_FULL;
    
    private DisruptorUtil() {
    }
    
    static WaitStrategy createWaitStrategy(final String propertyName, final AsyncWaitStrategyFactory asyncWaitStrategyFactory) {
        if (asyncWaitStrategyFactory == null) {
            DisruptorUtil.LOGGER.debug("No AsyncWaitStrategyFactory was configured in the configuration, using default factory...");
            return new DefaultAsyncWaitStrategyFactory(propertyName).createWaitStrategy();
        }
        DisruptorUtil.LOGGER.debug("Using configured AsyncWaitStrategyFactory {}", asyncWaitStrategyFactory.getClass().getName());
        return asyncWaitStrategyFactory.createWaitStrategy();
    }
    
    static int calculateRingBufferSize(final String propertyName) {
        int ringBufferSize = Constants.ENABLE_THREADLOCALS ? 4096 : 262144;
        final String userPreferredRBSize = PropertiesUtil.getProperties().getStringProperty(propertyName, String.valueOf(ringBufferSize));
        try {
            int size = Integers.parseInt(userPreferredRBSize);
            if (size < 128) {
                size = 128;
                DisruptorUtil.LOGGER.warn("Invalid RingBufferSize {}, using minimum size {}.", userPreferredRBSize, 128);
            }
            ringBufferSize = size;
        }
        catch (final Exception ex) {
            DisruptorUtil.LOGGER.warn("Invalid RingBufferSize {}, using default size {}.", userPreferredRBSize, ringBufferSize);
        }
        return Integers.ceilingNextPowerOfTwo(ringBufferSize);
    }
    
    static ExceptionHandler<RingBufferLogEvent> getAsyncLoggerExceptionHandler() {
        final String cls = PropertiesUtil.getProperties().getStringProperty("AsyncLogger.ExceptionHandler");
        if (cls == null) {
            return (ExceptionHandler<RingBufferLogEvent>)new AsyncLoggerDefaultExceptionHandler();
        }
        try {
            final Class<? extends ExceptionHandler<RingBufferLogEvent>> klass = (Class<? extends ExceptionHandler<RingBufferLogEvent>>)Loader.loadClass(cls);
            return (ExceptionHandler<RingBufferLogEvent>)klass.newInstance();
        }
        catch (final Exception ignored) {
            DisruptorUtil.LOGGER.debug("Invalid AsyncLogger.ExceptionHandler value: error creating {}: ", cls, ignored);
            return (ExceptionHandler<RingBufferLogEvent>)new AsyncLoggerDefaultExceptionHandler();
        }
    }
    
    static ExceptionHandler<AsyncLoggerConfigDisruptor.Log4jEventWrapper> getAsyncLoggerConfigExceptionHandler() {
        final String cls = PropertiesUtil.getProperties().getStringProperty("AsyncLoggerConfig.ExceptionHandler");
        if (cls == null) {
            return (ExceptionHandler<AsyncLoggerConfigDisruptor.Log4jEventWrapper>)new AsyncLoggerConfigDefaultExceptionHandler();
        }
        try {
            final Class<? extends ExceptionHandler<AsyncLoggerConfigDisruptor.Log4jEventWrapper>> klass = (Class<? extends ExceptionHandler<AsyncLoggerConfigDisruptor.Log4jEventWrapper>>)Loader.loadClass(cls);
            return (ExceptionHandler<AsyncLoggerConfigDisruptor.Log4jEventWrapper>)klass.newInstance();
        }
        catch (final Exception ignored) {
            DisruptorUtil.LOGGER.debug("Invalid AsyncLoggerConfig.ExceptionHandler value: error creating {}: ", cls, ignored);
            return (ExceptionHandler<AsyncLoggerConfigDisruptor.Log4jEventWrapper>)new AsyncLoggerConfigDefaultExceptionHandler();
        }
    }
    
    public static long getExecutorThreadId(final ExecutorService executor) {
        final Future<Long> result = executor.submit(() -> Thread.currentThread().getId());
        try {
            return result.get();
        }
        catch (final Exception ex) {
            final String msg = "Could not obtain executor thread Id. Giving up to avoid the risk of application deadlock.";
            throw new IllegalStateException("Could not obtain executor thread Id. Giving up to avoid the risk of application deadlock.", ex);
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        ASYNC_LOGGER_SYNCHRONIZE_ENQUEUE_WHEN_QUEUE_FULL = PropertiesUtil.getProperties().getBooleanProperty("AsyncLogger.SynchronizeEnqueueWhenQueueFull", true);
        ASYNC_CONFIG_SYNCHRONIZE_ENQUEUE_WHEN_QUEUE_FULL = PropertiesUtil.getProperties().getBooleanProperty("AsyncLoggerConfig.SynchronizeEnqueueWhenQueueFull", true);
    }
}
