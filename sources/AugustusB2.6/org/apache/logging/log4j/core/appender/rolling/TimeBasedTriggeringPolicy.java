// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling;

import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.util.Integers;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.LogEvent;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "TimeBasedTriggeringPolicy", category = "Core", printObject = true)
public final class TimeBasedTriggeringPolicy extends AbstractTriggeringPolicy
{
    private long nextRolloverMillis;
    private final int interval;
    private final boolean modulate;
    private final long maxRandomDelayMillis;
    private RollingFileManager manager;
    
    private TimeBasedTriggeringPolicy(final int interval, final boolean modulate, final long maxRandomDelayMillis) {
        this.interval = interval;
        this.modulate = modulate;
        this.maxRandomDelayMillis = maxRandomDelayMillis;
    }
    
    public int getInterval() {
        return this.interval;
    }
    
    public long getNextRolloverMillis() {
        return this.nextRolloverMillis;
    }
    
    @Override
    public void initialize(final RollingFileManager aManager) {
        this.manager = aManager;
        long current = aManager.getFileTime();
        if (current == 0L) {
            current = System.currentTimeMillis();
        }
        aManager.getPatternProcessor().getNextTime(current, this.interval, this.modulate);
        aManager.getPatternProcessor().setTimeBased(true);
        this.nextRolloverMillis = ThreadLocalRandom.current().nextLong(0L, 1L + this.maxRandomDelayMillis) + aManager.getPatternProcessor().getNextTime(current, this.interval, this.modulate);
    }
    
    @Override
    public boolean isTriggeringEvent(final LogEvent event) {
        final long nowMillis = event.getTimeMillis();
        if (nowMillis >= this.nextRolloverMillis) {
            this.nextRolloverMillis = ThreadLocalRandom.current().nextLong(0L, 1L + this.maxRandomDelayMillis) + this.manager.getPatternProcessor().getNextTime(nowMillis, this.interval, this.modulate);
            this.manager.getPatternProcessor().setCurrentFileTime(System.currentTimeMillis());
            return true;
        }
        return false;
    }
    
    @Deprecated
    public static TimeBasedTriggeringPolicy createPolicy(@PluginAttribute("interval") final String interval, @PluginAttribute("modulate") final String modulate) {
        return newBuilder().withInterval(Integers.parseInt(interval, 1)).withModulate(Boolean.parseBoolean(modulate)).build();
    }
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    @Override
    public String toString() {
        return "TimeBasedTriggeringPolicy(nextRolloverMillis=" + this.nextRolloverMillis + ", interval=" + this.interval + ", modulate=" + this.modulate + ")";
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<TimeBasedTriggeringPolicy>
    {
        @PluginBuilderAttribute
        private int interval;
        @PluginBuilderAttribute
        private boolean modulate;
        @PluginBuilderAttribute
        private int maxRandomDelay;
        
        public Builder() {
            this.interval = 1;
            this.modulate = false;
            this.maxRandomDelay = 0;
        }
        
        @Override
        public TimeBasedTriggeringPolicy build() {
            final long maxRandomDelayMillis = TimeUnit.SECONDS.toMillis(this.maxRandomDelay);
            return new TimeBasedTriggeringPolicy(this.interval, this.modulate, maxRandomDelayMillis, null);
        }
        
        public int getInterval() {
            return this.interval;
        }
        
        public boolean isModulate() {
            return this.modulate;
        }
        
        public int getMaxRandomDelay() {
            return this.maxRandomDelay;
        }
        
        public Builder withInterval(final int interval) {
            this.interval = interval;
            return this;
        }
        
        public Builder withModulate(final boolean modulate) {
            this.modulate = modulate;
            return this;
        }
        
        public Builder withMaxRandomDelay(final int maxRandomDelay) {
            this.maxRandomDelay = maxRandomDelay;
            return this;
        }
    }
}
