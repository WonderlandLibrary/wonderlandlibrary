/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

public class TimerUtils {
    private long lastMS;
    private long ms;

    public boolean delay(float f) {
        return (float)(this.getTime() - this.lastMS) >= f;
    }

    public long getTime() {
        return this.getCurrentMS() - this.lastMS;
    }

    public void reset() {
        this.lastMS = this.getCurrentMS();
        this.ms = 0L;
    }

    public boolean waitUntil(double d) {
        ++this.ms;
        return (double)this.ms >= d;
    }

    public boolean hasReached(double d) {
        return (double)(System.currentTimeMillis() - this.lastMS) >= d;
    }

    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
}

