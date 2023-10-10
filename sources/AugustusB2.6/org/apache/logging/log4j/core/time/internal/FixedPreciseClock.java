// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.time.internal;

import org.apache.logging.log4j.core.time.MutableInstant;
import org.apache.logging.log4j.core.time.PreciseClock;

public class FixedPreciseClock implements PreciseClock
{
    private final long currentTimeMillis;
    private final int nanosOfMillisecond;
    
    public FixedPreciseClock() {
        this(0L);
    }
    
    public FixedPreciseClock(final long currentTimeMillis) {
        this(currentTimeMillis, 0);
    }
    
    public FixedPreciseClock(final long currentTimeMillis, final int nanosOfMillisecond) {
        this.currentTimeMillis = currentTimeMillis;
        this.nanosOfMillisecond = nanosOfMillisecond;
    }
    
    @Override
    public void init(final MutableInstant instant) {
        instant.initFromEpochMilli(this.currentTimeMillis, this.nanosOfMillisecond);
    }
    
    @Override
    public long currentTimeMillis() {
        return this.currentTimeMillis;
    }
}
