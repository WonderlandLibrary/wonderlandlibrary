/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils;

public class Timer {
    private long time = -1L;

    public boolean passedS(double s) {
        return this.passedMs((long)s * 1000L);
    }

    public boolean passedDms(double dms) {
        return this.passedMs((long)dms * 10L);
    }

    public boolean passedDs(double ds) {
        return this.passedMs((long)ds * 100L);
    }

    public boolean passedMs(long ms) {
        return this.passedNS(this.convertToNS(ms));
    }

    public void setMs(long ms) {
        this.time = System.nanoTime() - this.convertToNS(ms);
    }

    public boolean passedNS(long ns) {
        if (System.nanoTime() - this.time < ns) return false;
        return true;
    }

    public long getPassedTimeMs() {
        return this.getMs(System.nanoTime() - this.time);
    }

    public long getPassedTimeS() {
        return this.getMs(System.nanoTime() - this.time) / 1000L;
    }

    public Timer reset() {
        this.time = System.nanoTime();
        return this;
    }

    public Timer delayRandom(int delay, int randomOffsetMs) {
        return this.delay((int)((double)delay + (double)randomOffsetMs * Math.random()));
    }

    public Timer delay(int delay) {
        this.time = System.nanoTime() + this.convertToNS(delay);
        return this;
    }

    public long getMs(long time) {
        return time / 1000000L;
    }

    public long convertToNS(long time) {
        return time * 1000000L;
    }
}

