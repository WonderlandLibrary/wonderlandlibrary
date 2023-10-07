/*
 * Decompiled with CFR 0.152.
 */
package me.report.liquidware.utils.timer;

public class TimerUtil {
    private long lastMS;

    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasReached(double milliseconds) {
        return (double)(this.getCurrentMS() - this.lastMS) >= milliseconds;
    }

    public final long getElapsedTime() {
        return this.getCurrentMS() - this.lastMS;
    }

    public void reset() {
        this.lastMS = this.getCurrentMS();
    }

    public boolean delay(float milliSec) {
        return (float)(this.getTime() - this.lastMS) >= milliSec;
    }

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public boolean isDelayComplete(long delay) {
        return System.currentTimeMillis() - this.lastMS > delay;
    }
}

