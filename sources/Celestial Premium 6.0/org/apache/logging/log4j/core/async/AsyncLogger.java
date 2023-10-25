/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.lmax.disruptor.BlockingWaitStrategy
 *  com.lmax.disruptor.EventFactory
 *  com.lmax.disruptor.EventHandler
 *  com.lmax.disruptor.EventTranslator
 *  com.lmax.disruptor.ExceptionHandler
 *  com.lmax.disruptor.RingBuffer
 *  com.lmax.disruptor.SleepingWaitStrategy
 *  com.lmax.disruptor.WaitStrategy
 *  com.lmax.disruptor.YieldingWaitStrategy
 *  com.lmax.disruptor.dsl.Disruptor
 *  com.lmax.disruptor.dsl.ProducerType
 *  com.lmax.disruptor.util.Util
 */
package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.Util;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.async.DaemonThreadFactory;
import org.apache.logging.log4j.core.async.RingBufferLogEvent;
import org.apache.logging.log4j.core.async.RingBufferLogEventHandler;
import org.apache.logging.log4j.core.async.RingBufferLogEventTranslator;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.helpers.Clock;
import org.apache.logging.log4j.core.helpers.ClockFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.status.StatusLogger;

public class AsyncLogger
extends Logger {
    private static final int HALF_A_SECOND = 500;
    private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 20;
    private static final int RINGBUFFER_MIN_SIZE = 128;
    private static final int RINGBUFFER_DEFAULT_SIZE = 262144;
    private static final StatusLogger LOGGER = StatusLogger.getLogger();
    private static volatile Disruptor<RingBufferLogEvent> disruptor;
    private static Clock clock;
    private static ExecutorService executor;
    private final ThreadLocal<Info> threadlocalInfo = new ThreadLocal();

    private static int calculateRingBufferSize() {
        int ringBufferSize = 262144;
        String userPreferredRBSize = System.getProperty("AsyncLogger.RingBufferSize", String.valueOf(ringBufferSize));
        try {
            int size = Integer.parseInt(userPreferredRBSize);
            if (size < 128) {
                size = 128;
                LOGGER.warn("Invalid RingBufferSize {}, using minimum size {}.", userPreferredRBSize, 128);
            }
            ringBufferSize = size;
        }
        catch (Exception ex) {
            LOGGER.warn("Invalid RingBufferSize {}, using default size {}.", userPreferredRBSize, ringBufferSize);
        }
        return Util.ceilingNextPowerOfTwo((int)ringBufferSize);
    }

    private static WaitStrategy createWaitStrategy() {
        String strategy = System.getProperty("AsyncLogger.WaitStrategy");
        LOGGER.debug("property AsyncLogger.WaitStrategy={}", strategy);
        if ("Sleep".equals(strategy)) {
            LOGGER.debug("disruptor event handler uses SleepingWaitStrategy");
            return new SleepingWaitStrategy();
        }
        if ("Yield".equals(strategy)) {
            LOGGER.debug("disruptor event handler uses YieldingWaitStrategy");
            return new YieldingWaitStrategy();
        }
        if ("Block".equals(strategy)) {
            LOGGER.debug("disruptor event handler uses BlockingWaitStrategy");
            return new BlockingWaitStrategy();
        }
        LOGGER.debug("disruptor event handler uses SleepingWaitStrategy");
        return new SleepingWaitStrategy();
    }

    private static ExceptionHandler getExceptionHandler() {
        String cls = System.getProperty("AsyncLogger.ExceptionHandler");
        if (cls == null) {
            LOGGER.debug("No AsyncLogger.ExceptionHandler specified");
            return null;
        }
        try {
            Class<?> klass = Class.forName(cls);
            ExceptionHandler result = (ExceptionHandler)klass.newInstance();
            LOGGER.debug("AsyncLogger.ExceptionHandler=" + (Object)result);
            return result;
        }
        catch (Exception ignored) {
            LOGGER.debug("AsyncLogger.ExceptionHandler not set: error creating " + cls + ": ", (Throwable)ignored);
            return null;
        }
    }

    public AsyncLogger(LoggerContext context, String name, MessageFactory messageFactory) {
        super(context, name, messageFactory);
    }

    @Override
    public void log(Marker marker, String fqcn, Level level, Message data, Throwable t) {
        Info info = this.threadlocalInfo.get();
        if (info == null) {
            info = new Info();
            info.translator = new RingBufferLogEventTranslator();
            info.cachedThreadName = Thread.currentThread().getName();
            this.threadlocalInfo.set(info);
        }
        boolean includeLocation = this.config.loggerConfig.isIncludeLocation();
        info.translator.setValues(this, this.getName(), marker, fqcn, level, data, t, ThreadContext.getImmutableContext(), ThreadContext.getImmutableStack(), info.cachedThreadName, includeLocation ? this.location(fqcn) : null, clock.currentTimeMillis());
        disruptor.publishEvent((EventTranslator)info.translator);
    }

    private StackTraceElement location(String fqcnOfLogger) {
        return Log4jLogEvent.calcLocation(fqcnOfLogger);
    }

    public void actualAsyncLog(RingBufferLogEvent event) {
        Map<Property, Boolean> properties = this.config.loggerConfig.getProperties();
        event.mergePropertiesIntoContextMap(properties, this.config.config.getStrSubstitutor());
        this.config.logEvent(event);
    }

    public static void stop() {
        Disruptor<RingBufferLogEvent> temp = disruptor;
        disruptor = null;
        temp.shutdown();
        RingBuffer ringBuffer = temp.getRingBuffer();
        for (int i = 0; i < 20 && !ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize()); ++i) {
            try {
                Thread.sleep(500L);
                continue;
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
        executor.shutdown();
    }

    static {
        clock = ClockFactory.getClock();
        executor = Executors.newSingleThreadExecutor(new DaemonThreadFactory("AsyncLogger-"));
        int ringBufferSize = AsyncLogger.calculateRingBufferSize();
        WaitStrategy waitStrategy = AsyncLogger.createWaitStrategy();
        disruptor = new Disruptor((EventFactory)RingBufferLogEvent.FACTORY, ringBufferSize, (Executor)executor, ProducerType.MULTI, waitStrategy);
        RingBufferLogEventHandler[] handlers = new RingBufferLogEventHandler[]{new RingBufferLogEventHandler()};
        disruptor.handleExceptionsWith(AsyncLogger.getExceptionHandler());
        disruptor.handleEventsWith((EventHandler[])handlers);
        LOGGER.debug("Starting AsyncLogger disruptor with ringbuffer size {}...", disruptor.getRingBuffer().getBufferSize());
        disruptor.start();
    }

    private static class Info {
        private RingBufferLogEventTranslator translator;
        private String cachedThreadName;

        private Info() {
        }
    }
}

