// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import org.apache.logging.log4j.core.Appender;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.BlockingQueue;
import java.util.List;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.util.Log4jThread;

class AsyncAppenderEventDispatcher extends Log4jThread
{
    private static final LogEvent STOP_EVENT;
    private static final AtomicLong THREAD_COUNTER;
    private static final Logger LOGGER;
    private final AppenderControl errorAppender;
    private final List<AppenderControl> appenders;
    private final BlockingQueue<LogEvent> queue;
    private final AtomicBoolean stoppedRef;
    
    AsyncAppenderEventDispatcher(final String name, final AppenderControl errorAppender, final List<AppenderControl> appenders, final BlockingQueue<LogEvent> queue) {
        super("AsyncAppenderEventDispatcher-" + AsyncAppenderEventDispatcher.THREAD_COUNTER.incrementAndGet() + "-" + name);
        this.setDaemon(true);
        this.errorAppender = errorAppender;
        this.appenders = appenders;
        this.queue = queue;
        this.stoppedRef = new AtomicBoolean();
    }
    
    List<Appender> getAppenders() {
        return this.appenders.stream().map((Function<? super Object, ?>)AppenderControl::getAppender).collect((Collector<? super Object, ?, List<Appender>>)Collectors.toList());
    }
    
    @Override
    public void run() {
        AsyncAppenderEventDispatcher.LOGGER.trace("{} has started.", this.getName());
        this.dispatchAll();
        this.dispatchRemaining();
    }
    
    private void dispatchAll() {
        while (!this.stoppedRef.get()) {
            LogEvent event;
            try {
                event = this.queue.take();
            }
            catch (final InterruptedException ignored) {
                this.interrupt();
                break;
            }
            if (event == AsyncAppenderEventDispatcher.STOP_EVENT) {
                break;
            }
            event.setEndOfBatch(this.queue.isEmpty());
            this.dispatch(event);
        }
        AsyncAppenderEventDispatcher.LOGGER.trace("{} has stopped.", this.getName());
    }
    
    private void dispatchRemaining() {
        int eventCount = 0;
        while (true) {
            final LogEvent event = this.queue.poll();
            if (event == null) {
                break;
            }
            if (event == AsyncAppenderEventDispatcher.STOP_EVENT) {
                continue;
            }
            event.setEndOfBatch(this.queue.isEmpty());
            this.dispatch(event);
            ++eventCount;
        }
        AsyncAppenderEventDispatcher.LOGGER.trace("{} has processed the last {} remaining event(s).", this.getName(), eventCount);
    }
    
    void dispatch(final LogEvent event) {
        boolean succeeded = false;
        for (int appenderIndex = 0; appenderIndex < this.appenders.size(); ++appenderIndex) {
            final AppenderControl control = this.appenders.get(appenderIndex);
            try {
                control.callAppender(event);
                succeeded = true;
            }
            catch (final Throwable error) {
                AsyncAppenderEventDispatcher.LOGGER.trace("{} has failed to call appender {}", this.getName(), control.getAppenderName(), error);
            }
        }
        if (!succeeded && this.errorAppender != null) {
            try {
                this.errorAppender.callAppender(event);
            }
            catch (final Throwable error2) {
                AsyncAppenderEventDispatcher.LOGGER.trace("{} has failed to call the error appender {}", this.getName(), this.errorAppender.getAppenderName(), error2);
            }
        }
    }
    
    void stop(final long timeoutMillis) throws InterruptedException {
        final boolean stopped = this.stoppedRef.compareAndSet(false, true);
        if (stopped) {
            AsyncAppenderEventDispatcher.LOGGER.trace("{} is signaled to stop.", this.getName());
        }
        while (State.NEW.equals(this.getState())) {}
        final boolean added = this.queue.offer(AsyncAppenderEventDispatcher.STOP_EVENT);
        if (!added) {
            this.interrupt();
        }
        this.join(timeoutMillis);
    }
    
    static {
        STOP_EVENT = new Log4jLogEvent();
        THREAD_COUNTER = new AtomicLong(0L);
        LOGGER = StatusLogger.getLogger();
    }
}
