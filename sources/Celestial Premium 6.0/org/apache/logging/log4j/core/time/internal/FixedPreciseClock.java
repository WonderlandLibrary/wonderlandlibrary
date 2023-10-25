/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.time.internal;

import org.apache.logging.log4j.core.time.MutableInstant;
import org.apache.logging.log4j.core.time.PreciseClock;

public class FixedPreciseClock
implements PreciseClock {
    private final long currentTimeMillis;
    private final int nanosOfMillisecond;

    public FixedPreciseClock() {
        this(0L);
    }

    public FixedPreciseClock(long currentTimeMillis) {
        this(currentTimeMillis, 0);
    }

    public FixedPreciseClock(long currentTimeMillis, int nanosOfMillisecond) {
        this.currentTimeMillis = currentTimeMillis;
        this.nanosOfMillisecond = nanosOfMillisecond;
    }

    @Override
    public void init(MutableInstant instant) {
        instant.initFromEpochMilli(this.currentTimeMillis, this.nanosOfMillisecond);
    }

    @Override
    public long currentTimeMillis() {
        return this.currentTimeMillis;
    }
}

