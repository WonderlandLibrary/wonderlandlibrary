/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.util.Log4jThread;
import org.apache.logging.log4j.status.StatusLogger;

class AsyncAppenderEventDispatcher
extends Log4jThread {
    private static final LogEvent STOP_EVENT = new Log4jLogEvent();
    private static final AtomicLong THREAD_COUNTER = new AtomicLong(0L);
    private static final Logger LOGGER = StatusLogger.getLogger();
    private final AppenderControl errorAppender;
    private final List<AppenderControl> appenders;
    private final BlockingQueue<LogEvent> queue;
    private final AtomicBoolean stoppedRef;

    AsyncAppenderEventDispatcher(String name, AppenderControl errorAppender, List<AppenderControl> appenders, BlockingQueue<LogEvent> queue) {
        super("AsyncAppenderEventDispatcher-" + THREAD_COUNTER.incrementAndGet() + "-" + name);
        this.errorAppender = errorAppender;
        this.appenders = appenders;
        this.queue = queue;
        this.stoppedRef = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        LOGGER.trace("{} has started.", this.getName());
        this.dispatchAll();
        this.dispatchRemaining();
    }

    private void dispatchAll() {
        while (!this.stoppedRef.get()) {
            LogEvent event;
            try {
                event = this.queue.take();
            }
            catch (InterruptedException ignored) {
                this.interrupt();
                break;
            }
            if (event == STOP_EVENT) break;
            event.setEndOfBatch(this.queue.isEmpty());
            this.dispatch(event);
        }
        LOGGER.trace("{} has stopped.", this.getName());
    }

    private void dispatchRemaining() {
        LogEvent event;
        int eventCount = 0;
        while ((event = (LogEvent)this.queue.poll()) != null) {
            if (event == STOP_EVENT) continue;
            event.setEndOfBatch(this.queue.isEmpty());
            this.dispatch(event);
            ++eventCount;
        }
        LOGGER.trace("{} has processed the last {} remaining event(s).", this.getName(), eventCount);
    }

    void dispatch(LogEvent event) {
        boolean succeeded = false;
        for (int appenderIndex = 0; appenderIndex < this.appenders.size(); ++appenderIndex) {
            AppenderControl control = this.appenders.get(appenderIndex);
            try {
                control.callAppender(event);
                succeeded = true;
                continue;
            }
            catch (Throwable error) {
                LOGGER.trace("{} has failed to call appender {}", this.getName(), control.getAppenderName(), error);
            }
        }
        if (!succeeded && this.errorAppender != null) {
            try {
                this.errorAppender.callAppender(event);
            }
            catch (Throwable error) {
                LOGGER.trace("{} has failed to call the error appender {}", this.getName(), this.errorAppender.getAppenderName(), error);
            }
        }
    }

    void stop(long timeoutMillis) throws InterruptedException {
        boolean stopped = this.stoppedRef.compareAndSet(false, true);
        if (stopped) {
            LOGGER.trace("{} is signaled to stop.", this.getName());
        }
        while (Thread.State.NEW.equals((Object)this.getState())) {
        }
        boolean added = this.queue.offer(STOP_EVENT);
        if (!added) {
            this.interrupt();
        }
        this.join(timeoutMillis);
    }
}

