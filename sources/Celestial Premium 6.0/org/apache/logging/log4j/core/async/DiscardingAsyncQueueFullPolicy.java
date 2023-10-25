/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.async;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.async.AsyncQueueFullPolicy;
import org.apache.logging.log4j.core.async.DefaultAsyncQueueFullPolicy;
import org.apache.logging.log4j.core.async.EventRoute;
import org.apache.logging.log4j.status.StatusLogger;

public class DiscardingAsyncQueueFullPolicy
extends DefaultAsyncQueueFullPolicy {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private final Level thresholdLevel;
    private final AtomicLong discardCount = new AtomicLong();

    public DiscardingAsyncQueueFullPolicy(Level thresholdLevel) {
        this.thresholdLevel = Objects.requireNonNull(thresholdLevel, "thresholdLevel");
    }

    @Override
    public EventRoute getRoute(long backgroundThreadId, Level level) {
        if (level.isLessSpecificThan(this.thresholdLevel)) {
            if (this.discardCount.getAndIncrement() == 0L) {
                LOGGER.warn("Async queue is full, discarding event with level {}. This message will only appear once; future events from {} are silently discarded until queue capacity becomes available.", (Object)level, (Object)this.thresholdLevel);
            }
            return EventRoute.DISCARD;
        }
        return super.getRoute(backgroundThreadId, level);
    }

    public static long getDiscardCount(AsyncQueueFullPolicy router) {
        if (router instanceof DiscardingAsyncQueueFullPolicy) {
            return ((DiscardingAsyncQueueFullPolicy)router).discardCount.get();
        }
        return 0L;
    }

    public Level getThresholdLevel() {
        return this.thresholdLevel;
    }
}

