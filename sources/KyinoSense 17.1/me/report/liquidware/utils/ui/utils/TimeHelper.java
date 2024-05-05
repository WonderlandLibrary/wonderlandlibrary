/*
 * Decompiled with CFR 0.152.
 */
package me.report.liquidware.utils.ui.utils;

public class TimeHelper {
    public long lastMs = 0L;
    private long prevMS;

    public boolean isDelayComplete(long delay) {
        return System.currentTimeMillis() - this.lastMs > delay;
    }

    public boolean isDelayComplete(double delay) {
        return (double)(System.currentTimeMillis() - this.lastMs) > delay;
    }

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public void reset() {
        this.lastMs = System.currentTimeMillis();
    }

    public long getLastMs() {
        return this.lastMs;
    }

    public void setLastMs(int i) {
        this.lastMs = System.currentTimeMillis() + (long)i;
    }

    public boolean hasReached(long milliseconds) {
        return System.currentTimeMillis() - this.lastMs >= milliseconds;
    }

    public boolean hasReached(double milliseconds) {
        return (double)(System.currentTimeMillis() - this.lastMs) >= milliseconds;
    }

    public boolean delay(float milliSec) {
        return (float)(this.getTime() - this.prevMS) >= milliSec;
    }

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public long getDifference() {
        return this.getTime() - this.prevMS;
    }

    public boolean check(float milliseconds) {
        return (float)this.getTime() >= milliseconds;
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
}

