// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.core.util.Throwables;
import org.apache.logging.log4j.core.util.Log4jThread;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.EventTranslator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.jmx.RingBufferAdmin;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadFactory;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.ProducerType;
import org.apache.logging.log4j.core.util.Log4jThreadFactory;
import com.lmax.disruptor.dsl.Disruptor;
import org.apache.logging.log4j.core.AbstractLifeCycle;

class AsyncLoggerDisruptor extends AbstractLifeCycle
{
    private static final int SLEEP_MILLIS_BETWEEN_DRAIN_ATTEMPTS = 50;
    private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 200;
    private final Object queueFullEnqueueLock;
    private volatile Disruptor<RingBufferLogEvent> disruptor;
    private String contextName;
    private boolean useThreadLocalTranslator;
    private long backgroundThreadId;
    private AsyncQueueFullPolicy asyncQueueFullPolicy;
    private int ringBufferSize;
    
    AsyncLoggerDisruptor(final String contextName) {
        this.queueFullEnqueueLock = new Object();
        this.useThreadLocalTranslator = true;
        this.contextName = contextName;
    }
    
    public String getContextName() {
        return this.contextName;
    }
    
    public void setContextName(final String name) {
        this.contextName = name;
    }
    
    Disruptor<RingBufferLogEvent> getDisruptor() {
        return this.disruptor;
    }
    
    @Override
    public synchronized void start() {
        if (this.disruptor != null) {
            AsyncLoggerDisruptor.LOGGER.trace("[{}] AsyncLoggerDisruptor not starting new disruptor for this context, using existing object.", this.contextName);
            return;
        }
        if (this.isStarting()) {
            AsyncLoggerDisruptor.LOGGER.trace("[{}] AsyncLoggerDisruptor is already starting.", this.contextName);
            return;
        }
        this.setStarting();
        AsyncLoggerDisruptor.LOGGER.trace("[{}] AsyncLoggerDisruptor creating new disruptor for this context.", this.contextName);
        this.ringBufferSize = DisruptorUtil.calculateRingBufferSize("AsyncLogger.RingBufferSize");
        final WaitStrategy waitStrategy = DisruptorUtil.createWaitStrategy("AsyncLogger.WaitStrategy");
        final ThreadFactory threadFactory = new Log4jThreadFactory("AsyncLogger[" + this.contextName + "]", true, 5) {
            @Override
            public Thread newThread(final Runnable r) {
                final Thread result = super.newThread(r);
                AsyncLoggerDisruptor.this.backgroundThreadId = result.getId();
                return result;
            }
        };
        this.asyncQueueFullPolicy = AsyncQueueFullPolicyFactory.create();
        this.disruptor = (Disruptor<RingBufferLogEvent>)new Disruptor((EventFactory)RingBufferLogEvent.FACTORY, this.ringBufferSize, threadFactory, ProducerType.MULTI, waitStrategy);
        final ExceptionHandler<RingBufferLogEvent> errorHandler = DisruptorUtil.getAsyncLoggerExceptionHandler();
        this.disruptor.setDefaultExceptionHandler((ExceptionHandler)errorHandler);
        final RingBufferLogEventHandler[] handlers = { new RingBufferLogEventHandler() };
        this.disruptor.handleEventsWith((EventHandler[])handlers);
        AsyncLoggerDisruptor.LOGGER.debug("[{}] Starting AsyncLogger disruptor for this context with ringbufferSize={}, waitStrategy={}, exceptionHandler={}...", this.contextName, this.disruptor.getRingBuffer().getBufferSize(), waitStrategy.getClass().getSimpleName(), errorHandler);
        this.disruptor.start();
        AsyncLoggerDisruptor.LOGGER.trace("[{}] AsyncLoggers use a {} translator", this.contextName, this.useThreadLocalTranslator ? "threadlocal" : "vararg");
        super.start();
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        final Disruptor<RingBufferLogEvent> temp = this.getDisruptor();
        if (temp == null) {
            AsyncLoggerDisruptor.LOGGER.trace("[{}] AsyncLoggerDisruptor: disruptor for this context already shut down.", this.contextName);
            return true;
        }
        this.setStopping();
        AsyncLoggerDisruptor.LOGGER.debug("[{}] AsyncLoggerDisruptor: shutting down disruptor for this context.", this.contextName);
        this.disruptor = null;
        for (int i = 0; hasBacklog(temp) && i < 200; ++i) {
            try {
                Thread.sleep(50L);
            }
            catch (InterruptedException ex) {}
        }
        try {
            temp.shutdown(timeout, timeUnit);
        }
        catch (TimeoutException e) {
            AsyncLoggerDisruptor.LOGGER.warn("[{}] AsyncLoggerDisruptor: shutdown timed out after {} {}", this.contextName, timeout, timeUnit);
            temp.halt();
        }
        AsyncLoggerDisruptor.LOGGER.trace("[{}] AsyncLoggerDisruptor: disruptor has been shut down.", this.contextName);
        if (DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy) > 0L) {
            AsyncLoggerDisruptor.LOGGER.trace("AsyncLoggerDisruptor: {} discarded {} events.", this.asyncQueueFullPolicy, DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy));
        }
        this.setStopped();
        return true;
    }
    
    private static boolean hasBacklog(final Disruptor<?> theDisruptor) {
        final RingBuffer<?> ringBuffer = (RingBuffer<?>)theDisruptor.getRingBuffer();
        return !ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize());
    }
    
    public RingBufferAdmin createRingBufferAdmin(final String jmxContextName) {
        final RingBuffer<RingBufferLogEvent> ring = (RingBuffer<RingBufferLogEvent>)((this.disruptor == null) ? null : this.disruptor.getRingBuffer());
        return RingBufferAdmin.forAsyncLogger(ring, jmxContextName);
    }
    
    EventRoute getEventRoute(final Level logLevel) {
        final int remainingCapacity = this.remainingDisruptorCapacity();
        if (remainingCapacity < 0) {
            return EventRoute.DISCARD;
        }
        return this.asyncQueueFullPolicy.getRoute(this.backgroundThreadId, logLevel);
    }
    
    private int remainingDisruptorCapacity() {
        final Disruptor<RingBufferLogEvent> temp = this.disruptor;
        if (this.hasLog4jBeenShutDown(temp)) {
            return -1;
        }
        return (int)temp.getRingBuffer().remainingCapacity();
    }
    
    private boolean hasLog4jBeenShutDown(final Disruptor<RingBufferLogEvent> aDisruptor) {
        if (aDisruptor == null) {
            AsyncLoggerDisruptor.LOGGER.warn("Ignoring log event after log4j was shut down");
            return true;
        }
        return false;
    }
    
    boolean tryPublish(final RingBufferLogEventTranslator translator) {
        try {
            return this.disruptor.getRingBuffer().tryPublishEvent((EventTranslator)translator);
        }
        catch (NullPointerException npe) {
            this.logWarningOnNpeFromDisruptorPublish(translator);
            return false;
        }
    }
    
    void enqueueLogMessageWhenQueueFull(final RingBufferLogEventTranslator translator) {
        try {
            if (this.synchronizeEnqueueWhenQueueFull()) {
                synchronized (this.queueFullEnqueueLock) {
                    this.disruptor.publishEvent((EventTranslator)translator);
                }
            }
            else {
                this.disruptor.publishEvent((EventTranslator)translator);
            }
        }
        catch (NullPointerException npe) {
            this.logWarningOnNpeFromDisruptorPublish(translator);
        }
    }
    
    void enqueueLogMessageWhenQueueFull(final EventTranslatorVararg<RingBufferLogEvent> translator, final AsyncLogger asyncLogger, final StackTraceElement location, final String fqcn, final Level level, final Marker marker, final Message msg, final Throwable thrown) {
        try {
            if (this.synchronizeEnqueueWhenQueueFull()) {
                synchronized (this.queueFullEnqueueLock) {
                    this.disruptor.getRingBuffer().publishEvent((EventTranslatorVararg)translator, new Object[] { asyncLogger, location, fqcn, level, marker, msg, thrown });
                }
            }
            else {
                this.disruptor.getRingBuffer().publishEvent((EventTranslatorVararg)translator, new Object[] { asyncLogger, location, fqcn, level, marker, msg, thrown });
            }
        }
        catch (NullPointerException npe) {
            this.logWarningOnNpeFromDisruptorPublish(level, fqcn, msg, thrown);
        }
    }
    
    private boolean synchronizeEnqueueWhenQueueFull() {
        return DisruptorUtil.ASYNC_LOGGER_SYNCHRONIZE_ENQUEUE_WHEN_QUEUE_FULL && this.backgroundThreadId != Thread.currentThread().getId() && !(Thread.currentThread() instanceof Log4jThread);
    }
    
    private void logWarningOnNpeFromDisruptorPublish(final RingBufferLogEventTranslator translator) {
        this.logWarningOnNpeFromDisruptorPublish(translator.level, translator.loggerName, translator.message, translator.thrown);
    }
    
    private void logWarningOnNpeFromDisruptorPublish(final Level level, final String fqcn, final Message msg, final Throwable thrown) {
        AsyncLoggerDisruptor.LOGGER.warn("[{}] Ignoring log event after log4j was shut down: {} [{}] {}{}", this.contextName, level, fqcn, msg.getFormattedMessage(), (thrown == null) ? "" : Throwables.toStringList(thrown));
    }
    
    public boolean isUseThreadLocals() {
        return this.useThreadLocalTranslator;
    }
    
    public void setUseThreadLocals(final boolean allow) {
        this.useThreadLocalTranslator = allow;
        AsyncLoggerDisruptor.LOGGER.trace("[{}] AsyncLoggers have been modified to use a {} translator", this.contextName, this.useThreadLocalTranslator ? "threadlocal" : "vararg");
    }
}
