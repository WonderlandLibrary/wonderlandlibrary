/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.lang3.concurrent;

import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.lang3.concurrent.AbstractCircuitBreaker;
import org.apache.commons.lang3.concurrent.CircuitBreakingException;

public class ThresholdCircuitBreaker
extends AbstractCircuitBreaker<Long> {
    private static final long INITIAL_COUNT = 0L;
    private final long threshold;
    private final AtomicLong used = new AtomicLong(0L);

    public ThresholdCircuitBreaker(long threshold) {
        this.threshold = threshold;
    }

    public long getThreshold() {
        return this.threshold;
    }

    @Override
    public boolean checkState() throws CircuitBreakingException {
        return this.isOpen();
    }

    @Override
    public void close() {
        super.close();
        this.used.set(0L);
    }

    @Override
    public boolean incrementAndCheckState(Long increment) throws CircuitBreakingException {
        long used;
        if (this.threshold == 0L) {
            this.open();
        }
        if ((used = this.used.addAndGet(increment)) > this.threshold) {
            this.open();
        }
        return this.checkState();
    }
}

