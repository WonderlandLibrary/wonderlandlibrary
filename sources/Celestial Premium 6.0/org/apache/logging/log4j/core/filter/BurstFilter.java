/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.filter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

@Plugin(name="BurstFilter", category="Core", elementType="filter", printObject=true)
public final class BurstFilter
extends AbstractFilter {
    private static final long NANOS_IN_SECONDS = 1000000000L;
    private static final int DEFAULT_RATE = 10;
    private static final int DEFAULT_RATE_MULTIPLE = 100;
    private static final int HASH_SHIFT = 32;
    private final Level level;
    private final long burstInterval;
    private final DelayQueue<LogDelay> history = new DelayQueue();
    private final Queue<LogDelay> available = new ConcurrentLinkedQueue<LogDelay>();

    private BurstFilter(Level level, float rate, long maxBurst, Filter.Result onMatch, Filter.Result onMismatch) {
        super(onMatch, onMismatch);
        this.level = level;
        this.burstInterval = (long)(1.0E9f * ((float)maxBurst / rate));
        int i = 0;
        while ((long)i < maxBurst) {
            this.available.add(new LogDelay());
            ++i;
        }
    }

    @Override
    public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object ... params) {
        return this.filter(level);
    }

    @Override
    public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        return this.filter(level);
    }

    @Override
    public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return this.filter(level);
    }

    @Override
    public Filter.Result filter(LogEvent event) {
        return this.filter(event.getLevel());
    }

    private Filter.Result filter(Level level) {
        if (this.level.isAtLeastAsSpecificAs(level)) {
            LogDelay delay = (LogDelay)this.history.poll();
            while (delay != null) {
                this.available.add(delay);
                delay = (LogDelay)this.history.poll();
            }
            delay = this.available.poll();
            if (delay != null) {
                delay.setDelay(this.burstInterval);
                this.history.add(delay);
                return this.onMatch;
            }
            return this.onMismatch;
        }
        return this.onMatch;
    }

    public int getAvailable() {
        return this.available.size();
    }

    public void clear() {
        for (LogDelay delay : this.history) {
            this.history.remove(delay);
            this.available.add(delay);
        }
    }

    @Override
    public String toString() {
        return "level=" + this.level.toString() + ", interval=" + this.burstInterval + ", max=" + this.history.size();
    }

    @PluginFactory
    public static BurstFilter createFilter(@PluginAttribute(value="level") String levelName, @PluginAttribute(value="rate") String rate, @PluginAttribute(value="maxBurst") String maxBurst, @PluginAttribute(value="onMatch") String match, @PluginAttribute(value="onMismatch") String mismatch) {
        float eventRate;
        Filter.Result onMatch = Filter.Result.toResult(match, Filter.Result.NEUTRAL);
        Filter.Result onMismatch = Filter.Result.toResult(mismatch, Filter.Result.DENY);
        Level level = Level.toLevel(levelName, Level.WARN);
        float f = eventRate = rate == null ? 10.0f : Float.parseFloat(rate);
        if (eventRate <= 0.0f) {
            eventRate = 10.0f;
        }
        long max = maxBurst == null ? (long)(eventRate * 100.0f) : Long.parseLong(maxBurst);
        return new BurstFilter(level, eventRate, max, onMatch, onMismatch);
    }

    private class LogDelay
    implements Delayed {
        private long expireTime;

        public void setDelay(long delay) {
            this.expireTime = delay + System.nanoTime();
        }

        @Override
        public long getDelay(TimeUnit timeUnit) {
            return timeUnit.convert(this.expireTime - System.nanoTime(), TimeUnit.NANOSECONDS);
        }

        @Override
        public int compareTo(Delayed delayed) {
            if (this.expireTime < ((LogDelay)delayed).expireTime) {
                return -1;
            }
            if (this.expireTime > ((LogDelay)delayed).expireTime) {
                return 1;
            }
            return 0;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            LogDelay logDelay = (LogDelay)o;
            return this.expireTime == logDelay.expireTime;
        }

        public int hashCode() {
            return (int)(this.expireTime ^ this.expireTime >>> 32);
        }
    }
}

