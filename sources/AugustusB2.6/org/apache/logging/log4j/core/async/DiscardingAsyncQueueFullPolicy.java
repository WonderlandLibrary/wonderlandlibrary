// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class DiscardingAsyncQueueFullPolicy extends DefaultAsyncQueueFullPolicy
{
    private static final Logger LOGGER;
    private final Level thresholdLevel;
    private final AtomicLong discardCount;
    
    public DiscardingAsyncQueueFullPolicy(final Level thresholdLevel) {
        this.discardCount = new AtomicLong();
        this.thresholdLevel = Objects.requireNonNull(thresholdLevel, "thresholdLevel");
    }
    
    @Override
    public EventRoute getRoute(final long backgroundThreadId, final Level level) {
        if (level.isLessSpecificThan(this.thresholdLevel)) {
            if (this.discardCount.getAndIncrement() == 0L) {
                DiscardingAsyncQueueFullPolicy.LOGGER.warn("Async queue is full, discarding event with level {}. This message will only appear once; future events from {} are silently discarded until queue capacity becomes available.", level, this.thresholdLevel);
            }
            return EventRoute.DISCARD;
        }
        return super.getRoute(backgroundThreadId, level);
    }
    
    public static long getDiscardCount(final AsyncQueueFullPolicy router) {
        if (router instanceof DiscardingAsyncQueueFullPolicy) {
            return ((DiscardingAsyncQueueFullPolicy)router).discardCount.get();
        }
        return 0L;
    }
    
    public Level getThresholdLevel() {
        return this.thresholdLevel;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
