/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.lmax.disruptor.EventFactory
 *  com.lmax.disruptor.EventHandler
 *  com.lmax.disruptor.EventTranslatorTwoArg
 *  com.lmax.disruptor.ExceptionHandler
 *  com.lmax.disruptor.RingBuffer
 *  com.lmax.disruptor.Sequence
 *  com.lmax.disruptor.SequenceReportingEventHandler
 *  com.lmax.disruptor.TimeoutException
 *  com.lmax.disruptor.WaitStrategy
 *  com.lmax.disruptor.dsl.Disruptor
 *  com.lmax.disruptor.dsl.ProducerType
 */
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
import org.apache.logging.log4j.core.async.AsyncLoggerConfig;
import org.apache.logging.log4j.core.async.AsyncLoggerConfigDelegate;
import org.apache.logging.log4j.core.async.AsyncQueueFullPolicy;
import org.apache.logging.log4j.core.async.AsyncQueueFullPolicyFactory;
import org.apache.logging.log4j.core.async.DiscardingAsyncQueueFullPolicy;
import org.apache.logging.log4j.core.async.DisruptorUtil;
import org.apache.logging.log4j.core.async.EventRoute;
import org.apache.logging.log4j.core.async.InternalAsyncUtil;
import org.apache.logging.log4j.core.async.RingBufferLogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.impl.LogEventFactory;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.core.impl.ReusableLogEventFactory;
import org.apache.logging.log4j.core.jmx.RingBufferAdmin;
import org.apache.logging.log4j.core.util.Log4jThread;
import org.apache.logging.log4j.core.util.Log4jThreadFactory;
import org.apache.logging.log4j.core.util.Throwables;
import org.apache.logging.log4j.message.ReusableMessage;

public class AsyncLoggerConfigDisruptor
extends AbstractLifeCycle
implements AsyncLoggerConfigDelegate {
    private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 200;
    private static final int SLEEP_MILLIS_BETWEEN_DRAIN_ATTEMPTS = 50;
    private static final EventFactory<Log4jEventWrapper> FACTORY = Log4jEventWrapper::new;
    private static final EventFactory<Log4jEventWrapper> MUTABLE_FACTORY = () -> new Log4jEventWrapper(new MutableLogEvent());
    private static final EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig> TRANSLATOR = (ringBufferElement, sequence, logEvent, loggerConfig) -> {
        ((Log4jEventWrapper)ringBufferElement).event = logEvent;
        ((Log4jEventWrapper)ringBufferElement).loggerConfig = loggerConfig;
    };
    private static final EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig> MUTABLE_TRANSLATOR = (ringBufferElement, sequence, logEvent, loggerConfig) -> {
        ((MutableLogEvent)((Log4jEventWrapper)ringBufferElement).event).initFrom((LogEvent)logEvent);
        ((Log4jEventWrapper)ringBufferElement).loggerConfig = loggerConfig;
    };
    private int ringBufferSize;
    private AsyncQueueFullPolicy asyncQueueFullPolicy;
    private Boolean mutable = Boolean.FALSE;
    private volatile Disruptor<Log4jEventWrapper> disruptor;
    private long backgroundThreadId;
    private EventFactory<Log4jEventWrapper> factory;
    private EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig> translator;
    private volatile boolean alreadyLoggedWarning;
    private final Object queueFullEnqueueLock = new Object();

    @Override
    public void setLogEventFactory(LogEventFactory logEventFactory) {
        this.mutable = this.mutable != false || logEventFactory instanceof ReusableLogEventFactory;
    }

    @Override
    public synchronized void start() {
        if (this.disruptor != null) {
            LOGGER.trace("AsyncLoggerConfigDisruptor not starting new disruptor for this configuration, using existing object.");
            return;
        }
        LOGGER.trace("AsyncLoggerConfigDisruptor creating new disruptor for this configuration.");
        this.ringBufferSize = DisruptorUtil.calculateRingBufferSize("AsyncLoggerConfig.RingBufferSize");
        WaitStrategy waitStrategy = DisruptorUtil.createWaitStrategy("AsyncLoggerConfig.WaitStrategy");
        Log4jThreadFactory threadFactory = new Log4jThreadFactory("AsyncLoggerConfig", true, 5){

            @Override
            public Thread newThread(Runnable r) {
                Thread result = super.newThread(r);
                AsyncLoggerConfigDisruptor.this.backgroundThreadId = result.getId();
                return result;
            }
        };
        this.asyncQueueFullPolicy = AsyncQueueFullPolicyFactory.create();
        this.translator = this.mutable != false ? MUTABLE_TRANSLATOR : TRANSLATOR;
        this.factory = this.mutable != false ? MUTABLE_FACTORY : FACTORY;
        this.disruptor = new Disruptor(this.factory, this.ringBufferSize, (ThreadFactory)threadFactory, ProducerType.MULTI, waitStrategy);
        ExceptionHandler<Log4jEventWrapper> errorHandler = DisruptorUtil.getAsyncLoggerConfigExceptionHandler();
        this.disruptor.setDefaultExceptionHandler(errorHandler);
        Log4jEventWrapperHandler[] handlers = new Log4jEventWrapperHandler[]{new Log4jEventWrapperHandler()};
        this.disruptor.handleEventsWith((EventHandler[])handlers);
        LOGGER.debug("Starting AsyncLoggerConfig disruptor for this configuration with ringbufferSize={}, waitStrategy={}, exceptionHandler={}...", this.disruptor.getRingBuffer().getBufferSize(), waitStrategy.getClass().getSimpleName(), errorHandler);
        this.disruptor.start();
        super.start();
    }

    @Override
    public boolean stop(long timeout, TimeUnit timeUnit) {
        Disruptor<Log4jEventWrapper> temp = this.disruptor;
        if (temp == null) {
            LOGGER.trace("AsyncLoggerConfigDisruptor: disruptor for this configuration already shut down.");
            return true;
        }
        this.setStopping();
        LOGGER.trace("AsyncLoggerConfigDisruptor: shutting down disruptor for this configuration.");
        this.disruptor = null;
        for (int i = 0; AsyncLoggerConfigDisruptor.hasBacklog(temp) && i < 200; ++i) {
            try {
                Thread.sleep(50L);
                continue;
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
        try {
            temp.shutdown(timeout, timeUnit);
        }
        catch (TimeoutException e) {
            LOGGER.warn("AsyncLoggerConfigDisruptor: shutdown timed out after {} {}", timeout, (Object)timeUnit);
            temp.halt();
        }
        LOGGER.trace("AsyncLoggerConfigDisruptor: disruptor has been shut down.");
        if (DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy) > 0L) {
            LOGGER.trace("AsyncLoggerConfigDisruptor: {} discarded {} events.", this.asyncQueueFullPolicy, DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy));
        }
        this.setStopped();
        return true;
    }

    private static boolean hasBacklog(Disruptor<?> theDisruptor) {
        RingBuffer ringBuffer = theDisruptor.getRingBuffer();
        return !ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize());
    }

    @Override
    public EventRoute getEventRoute(Level logLevel) {
        int remainingCapacity = this.remainingDisruptorCapacity();
        if (remainingCapacity < 0) {
            return EventRoute.DISCARD;
        }
        return this.asyncQueueFullPolicy.getRoute(this.backgroundThreadId, logLevel);
    }

    private int remainingDisruptorCapacity() {
        Disruptor<Log4jEventWrapper> temp = this.disruptor;
        if (this.hasLog4jBeenShutDown(temp)) {
            return -1;
        }
        return (int)temp.getRingBuffer().remainingCapacity();
    }

    private boolean hasLog4jBeenShutDown(Disruptor<Log4jEventWrapper> aDisruptor) {
        if (aDisruptor == null) {
            LOGGER.warn("Ignoring log event after log4j was shut down");
            return true;
        }
        return false;
    }

    @Override
    public void enqueueEvent(LogEvent event, AsyncLoggerConfig asyncLoggerConfig) {
        try {
            LogEvent logEvent = this.prepareEvent(event);
            this.enqueue(logEvent, asyncLoggerConfig);
        }
        catch (NullPointerException npe) {
            LOGGER.warn("Ignoring log event after log4j was shut down: {} [{}] {}", (Object)event.getLevel(), event.getLoggerName(), event.getMessage().getFormattedMessage() + (event.getThrown() == null ? "" : Throwables.toStringList(event.getThrown())));
        }
    }

    private LogEvent prepareEvent(LogEvent event) {
        LogEvent logEvent = this.ensureImmutable(event);
        if (logEvent.getMessage() instanceof ReusableMessage) {
            if (logEvent instanceof Log4jLogEvent) {
                ((Log4jLogEvent)logEvent).makeMessageImmutable();
            } else if (logEvent instanceof MutableLogEvent) {
                if (this.translator != MUTABLE_TRANSLATOR) {
                    logEvent = ((MutableLogEvent)logEvent).createMemento();
                }
            } else {
                this.showWarningAboutCustomLogEventWithReusableMessage(logEvent);
            }
        } else {
            InternalAsyncUtil.makeMessageImmutable(logEvent.getMessage());
        }
        return logEvent;
    }

    private void showWarningAboutCustomLogEventWithReusableMessage(LogEvent logEvent) {
        if (!this.alreadyLoggedWarning) {
            LOGGER.warn("Custom log event of type {} contains a mutable message of type {}. AsyncLoggerConfig does not know how to make an immutable copy of this message. This may result in ConcurrentModificationExceptions or incorrect log messages if the application modifies objects in the message while the background thread is writing it to the appenders.", logEvent.getClass().getName(), logEvent.getMessage().getClass().getName());
            this.alreadyLoggedWarning = true;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void enqueue(LogEvent logEvent, AsyncLoggerConfig asyncLoggerConfig) {
        if (this.synchronizeEnqueueWhenQueueFull()) {
            Object object = this.queueFullEnqueueLock;
            synchronized (object) {
                this.disruptor.getRingBuffer().publishEvent(this.translator, (Object)logEvent, (Object)asyncLoggerConfig);
            }
        } else {
            this.disruptor.getRingBuffer().publishEvent(this.translator, (Object)logEvent, (Object)asyncLoggerConfig);
        }
    }

    private boolean synchronizeEnqueueWhenQueueFull() {
        return DisruptorUtil.ASYNC_CONFIG_SYNCHRONIZE_ENQUEUE_WHEN_QUEUE_FULL && this.backgroundThreadId != Thread.currentThread().getId() && !(Thread.currentThread() instanceof Log4jThread);
    }

    @Override
    public boolean tryEnqueue(LogEvent event, AsyncLoggerConfig asyncLoggerConfig) {
        LogEvent logEvent = this.prepareEvent(event);
        return this.disruptor.getRingBuffer().tryPublishEvent(this.translator, (Object)logEvent, (Object)asyncLoggerConfig);
    }

    private LogEvent ensureImmutable(LogEvent event) {
        LogEvent result = event;
        if (event instanceof RingBufferLogEvent) {
            result = ((RingBufferLogEvent)event).createMemento();
        }
        return result;
    }

    @Override
    public RingBufferAdmin createRingBufferAdmin(String contextName, String loggerConfigName) {
        return RingBufferAdmin.forAsyncLoggerConfig(this.disruptor.getRingBuffer(), contextName, loggerConfigName);
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
            event.loggerConfig.logToAsyncLoggerConfigsOnCurrentThread(event.event);
            event.clear();
            this.notifyIntermediateProgress(sequence);
        }

        private void notifyIntermediateProgress(long sequence) {
            if (++this.counter > 50) {
                this.sequenceCallback.set(sequence);
                this.counter = 0;
            }
        }
    }

    public static class Log4jEventWrapper {
        private AsyncLoggerConfig loggerConfig;
        private LogEvent event;

        public Log4jEventWrapper() {
        }

        public Log4jEventWrapper(MutableLogEvent mutableLogEvent) {
            this.event = mutableLogEvent;
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
}

