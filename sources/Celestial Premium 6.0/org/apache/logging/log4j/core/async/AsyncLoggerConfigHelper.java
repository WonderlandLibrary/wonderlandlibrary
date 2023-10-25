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
 *  com.lmax.disruptor.Sequence
 *  com.lmax.disruptor.SequenceReportingEventHandler
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
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceReportingEventHandler;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.Util;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.async.AsyncLoggerConfig;
import org.apache.logging.log4j.core.async.DaemonThreadFactory;
import org.apache.logging.log4j.status.StatusLogger;

class AsyncLoggerConfigHelper {
    private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 20;
    private static final int HALF_A_SECOND = 500;
    private static final int RINGBUFFER_MIN_SIZE = 128;
    private static final int RINGBUFFER_DEFAULT_SIZE = 262144;
    private static final Logger LOGGER = StatusLogger.getLogger();
    private static ThreadFactory threadFactory = new DaemonThreadFactory("AsyncLoggerConfig-");
    private static volatile Disruptor<Log4jEventWrapper> disruptor;
    private static ExecutorService executor;
    private static volatile int count;
    private static final EventFactory<Log4jEventWrapper> FACTORY;
    private final EventTranslator<Log4jEventWrapper> translator = new EventTranslator<Log4jEventWrapper>(){

        public void translateTo(Log4jEventWrapper event, long sequence) {
            event.event = (LogEvent)AsyncLoggerConfigHelper.this.currentLogEvent.get();
            event.loggerConfig = AsyncLoggerConfigHelper.this.asyncLoggerConfig;
        }
    };
    private final ThreadLocal<LogEvent> currentLogEvent = new ThreadLocal();
    private final AsyncLoggerConfig asyncLoggerConfig;

    public AsyncLoggerConfigHelper(AsyncLoggerConfig asyncLoggerConfig) {
        this.asyncLoggerConfig = asyncLoggerConfig;
        AsyncLoggerConfigHelper.claim();
    }

    private static synchronized void initDisruptor() {
        if (disruptor != null) {
            LOGGER.trace("AsyncLoggerConfigHelper not starting new disruptor, using existing object. Ref count is {}.", count);
            return;
        }
        LOGGER.trace("AsyncLoggerConfigHelper creating new disruptor. Ref count is {}.", count);
        int ringBufferSize = AsyncLoggerConfigHelper.calculateRingBufferSize();
        WaitStrategy waitStrategy = AsyncLoggerConfigHelper.createWaitStrategy();
        executor = Executors.newSingleThreadExecutor(threadFactory);
        disruptor = new Disruptor(FACTORY, ringBufferSize, (Executor)executor, ProducerType.MULTI, waitStrategy);
        Log4jEventWrapperHandler[] handlers = new Log4jEventWrapperHandler[]{new Log4jEventWrapperHandler()};
        ExceptionHandler errorHandler = AsyncLoggerConfigHelper.getExceptionHandler();
        disruptor.handleExceptionsWith(errorHandler);
        disruptor.handleEventsWith((EventHandler[])handlers);
        LOGGER.debug("Starting AsyncLoggerConfig disruptor with ringbuffer size={}, waitStrategy={}, exceptionHandler={}...", new Object[]{disruptor.getRingBuffer().getBufferSize(), waitStrategy.getClass().getSimpleName(), errorHandler});
        disruptor.start();
    }

    private static WaitStrategy createWaitStrategy() {
        String strategy = System.getProperty("AsyncLoggerConfig.WaitStrategy");
        LOGGER.debug("property AsyncLoggerConfig.WaitStrategy={}", strategy);
        if ("Sleep".equals(strategy)) {
            return new SleepingWaitStrategy();
        }
        if ("Yield".equals(strategy)) {
            return new YieldingWaitStrategy();
        }
        if ("Block".equals(strategy)) {
            return new BlockingWaitStrategy();
        }
        return new SleepingWaitStrategy();
    }

    private static int calculateRingBufferSize() {
        int ringBufferSize = 262144;
        String userPreferredRBSize = System.getProperty("AsyncLoggerConfig.RingBufferSize", String.valueOf(ringBufferSize));
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

    private static ExceptionHandler getExceptionHandler() {
        String cls = System.getProperty("AsyncLoggerConfig.ExceptionHandler");
        if (cls == null) {
            return null;
        }
        try {
            Class<?> klass = Class.forName(cls);
            ExceptionHandler result = (ExceptionHandler)klass.newInstance();
            return result;
        }
        catch (Exception ignored) {
            LOGGER.debug("AsyncLoggerConfig.ExceptionHandler not set: error creating " + cls + ": ", (Throwable)ignored);
            return null;
        }
    }

    static synchronized void claim() {
        ++count;
        AsyncLoggerConfigHelper.initDisruptor();
    }

    static synchronized void release() {
        if (--count > 0) {
            LOGGER.trace("AsyncLoggerConfigHelper: not shutting down disruptor: ref count is {}.", count);
            return;
        }
        Disruptor<Log4jEventWrapper> temp = disruptor;
        if (temp == null) {
            LOGGER.trace("AsyncLoggerConfigHelper: disruptor already shut down: ref count is {}.", count);
            return;
        }
        LOGGER.trace("AsyncLoggerConfigHelper: shutting down disruptor: ref count is {}.", count);
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
        executor = null;
    }

    public void callAppendersFromAnotherThread(LogEvent event) {
        this.currentLogEvent.set(event);
        disruptor.publishEvent(this.translator);
    }

    static {
        count = 0;
        FACTORY = new EventFactory<Log4jEventWrapper>(){

            public Log4jEventWrapper newInstance() {
                return new Log4jEventWrapper();
            }
        };
    }

    private static class Log4jEventWrapperHandler
    implements SequenceReportingEventHandler<Log4jEventWrapper> {
        private static final int NOTIFY_PROGRESS_THRESHOLD = 50;
        private Sequence sequenceCallback;
        private int counter;

        private Log4jEventWrapperHandler() {
        }

        public void setSequenceCallback(Sequence sequenceCallback) {
            this.sequenceCallback = sequenceCallback;
        }

        public void onEvent(Log4jEventWrapper event, long sequence, boolean endOfBatch) throws Exception {
            event.event.setEndOfBatch(endOfBatch);
            event.loggerConfig.asyncCallAppenders(event.event);
            event.clear();
            if (++this.counter > 50) {
                this.sequenceCallback.set(sequence);
                this.counter = 0;
            }
        }
    }

    private static class Log4jEventWrapper {
        private AsyncLoggerConfig loggerConfig;
        private LogEvent event;

        private Log4jEventWrapper() {
        }

        public void clear() {
            this.loggerConfig = null;
            this.event = null;
        }
    }
}

