// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.util.Builder;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Delayed;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import java.util.Iterator;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Logger;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.logging.log4j.core.Filter;
import java.util.Queue;
import java.util.concurrent.DelayQueue;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "BurstFilter", category = "Core", elementType = "filter", printObject = true)
public final class BurstFilter extends AbstractFilter
{
    private static final long NANOS_IN_SECONDS = 1000000000L;
    private static final int DEFAULT_RATE = 10;
    private static final int DEFAULT_RATE_MULTIPLE = 100;
    private static final int HASH_SHIFT = 32;
    private final Level level;
    private final long burstInterval;
    private final DelayQueue<LogDelay> history;
    private final Queue<LogDelay> available;
    
    static LogDelay createLogDelay(final long expireTime) {
        return new LogDelay(expireTime);
    }
    
    private BurstFilter(final Level level, final float rate, final long maxBurst, final Filter.Result onMatch, final Filter.Result onMismatch) {
        super(onMatch, onMismatch);
        this.history = new DelayQueue<LogDelay>();
        this.available = new ConcurrentLinkedQueue<LogDelay>();
        this.level = level;
        this.burstInterval = (long)(1.0E9f * (maxBurst / rate));
        for (int i = 0; i < maxBurst; ++i) {
            this.available.add(createLogDelay(0L));
        }
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final LogEvent event) {
        return this.filter(event.getLevel());
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        return this.filter(level);
    }
    
    private Filter.Result filter(final Level level) {
        if (!this.level.isMoreSpecificThan(level)) {
            return this.onMatch;
        }
        for (LogDelay delay = this.history.poll(); delay != null; delay = this.history.poll()) {
            this.available.add(delay);
        }
        LogDelay delay = this.available.poll();
        if (delay != null) {
            delay.setDelay(this.burstInterval);
            this.history.add(delay);
            return this.onMatch;
        }
        return this.onMismatch;
    }
    
    public int getAvailable() {
        return this.available.size();
    }
    
    public void clear() {
        for (final LogDelay delay : this.history) {
            this.history.remove(delay);
            this.available.add(delay);
        }
    }
    
    @Override
    public String toString() {
        return "level=" + this.level.toString() + ", interval=" + this.burstInterval + ", max=" + this.history.size();
    }
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    private static class LogDelay implements Delayed
    {
        private long expireTime;
        
        LogDelay(final long expireTime) {
            this.expireTime = expireTime;
        }
        
        public void setDelay(final long delay) {
            this.expireTime = delay + System.nanoTime();
        }
        
        @Override
        public long getDelay(final TimeUnit timeUnit) {
            return timeUnit.convert(this.expireTime - System.nanoTime(), TimeUnit.NANOSECONDS);
        }
        
        @Override
        public int compareTo(final Delayed delayed) {
            final long diff = this.expireTime - ((LogDelay)delayed).expireTime;
            return Long.signum(diff);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final LogDelay logDelay = (LogDelay)o;
            return this.expireTime == logDelay.expireTime;
        }
        
        @Override
        public int hashCode() {
            return (int)(this.expireTime ^ this.expireTime >>> 32);
        }
    }
    
    public static class Builder extends AbstractFilterBuilder<Builder> implements org.apache.logging.log4j.core.util.Builder<BurstFilter>
    {
        @PluginBuilderAttribute
        private Level level;
        @PluginBuilderAttribute
        private float rate;
        @PluginBuilderAttribute
        private long maxBurst;
        
        public Builder() {
            this.level = Level.WARN;
            this.rate = 10.0f;
        }
        
        public Builder setLevel(final Level level) {
            this.level = level;
            return this;
        }
        
        public Builder setRate(final float rate) {
            this.rate = rate;
            return this;
        }
        
        public Builder setMaxBurst(final long maxBurst) {
            this.maxBurst = maxBurst;
            return this;
        }
        
        @Override
        public BurstFilter build() {
            if (this.rate <= 0.0f) {
                this.rate = 10.0f;
            }
            if (this.maxBurst <= 0L) {
                this.maxBurst = (long)(this.rate * 100.0f);
            }
            return new BurstFilter(this.level, this.rate, this.maxBurst, this.getOnMatch(), this.getOnMismatch(), null);
        }
    }
}
