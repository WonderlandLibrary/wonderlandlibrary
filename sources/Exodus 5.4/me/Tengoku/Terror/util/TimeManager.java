/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

import java.util.concurrent.TimeUnit;

public class TimeManager {
    private long last;

    public final synchronized void resetTime() {
        this.last = System.nanoTime();
    }

    public final synchronized boolean sleep(long l) {
        return this.sleep(l, TimeUnit.MILLISECONDS);
    }

    public synchronized boolean sleep(long l, TimeUnit timeUnit) {
        return timeUnit.convert(System.nanoTime() - this.last, TimeUnit.NANOSECONDS) >= l;
    }

    public final long convertToMillis(double d) {
        return (long)(1000.0 / d);
    }
}

