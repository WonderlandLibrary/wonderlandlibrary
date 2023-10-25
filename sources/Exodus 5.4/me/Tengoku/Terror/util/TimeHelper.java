/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

public class TimeHelper {
    private long lastMS = 0L;

    public boolean hasReachedDouble(double d) {
        return (double)(this.getCurrentMS() - this.lastMS) >= d;
    }

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasTimeElapsed(long l, boolean bl) {
        if (System.currentTimeMillis() - this.lastMS > l) {
            if (bl) {
                this.reset();
            }
            return true;
        }
        return false;
    }

    public boolean hasReached(float f) {
        return (float)(this.getCurrentMS() - this.lastMS) >= f;
    }

    public void setLastMS(long l) {
        this.lastMS = l;
    }

    public long getDelay() {
        return System.currentTimeMillis() - this.lastMS;
    }

    public void setLastMS() {
        this.lastMS = System.currentTimeMillis();
    }

    public boolean hasTimeReached(long l) {
        return System.currentTimeMillis() - this.lastMS >= l;
    }

    public void reset() {
        this.lastMS = this.getCurrentMS();
    }

    public int convertToMS(int n) {
        return 1000 / n;
    }
}

