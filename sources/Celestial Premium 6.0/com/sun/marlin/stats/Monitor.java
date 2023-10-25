/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin.stats;

import com.sun.marlin.stats.StatLong;

public final class Monitor
extends StatLong {
    private static final long INVALID = -1L;
    private long start = -1L;

    public Monitor(String string) {
        super(string);
    }

    public void start() {
        this.start = System.nanoTime();
    }

    public void stop() {
        long l = System.nanoTime() - this.start;
        if (this.start != -1L && l > 0L) {
            this.add(l);
        }
        this.start = -1L;
    }
}

