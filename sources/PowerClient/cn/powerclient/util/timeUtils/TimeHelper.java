/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.util.timeUtils;

public class TimeHelper {
    public long lastMs = 0L;

    public boolean isDelayComplete(long delay) {
        if (System.currentTimeMillis() - this.lastMs > delay) {
            return true;
        }
        return false;
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

    public void setLastMs(int i2) {
        this.lastMs = System.currentTimeMillis() + (long)i2;
    }

    public boolean hasReached(long milliseconds) {
        if (this.getCurrentMS() - this.lastMs >= milliseconds) {
            return true;
        }
        return false;
    }

    public boolean hasReached(float timeLeft) {
        if ((float)(this.getCurrentMS() - this.lastMs) >= timeLeft) {
            return true;
        }
        return false;
    }

    public boolean delay(double nextDelay) {
        if ((double)(System.currentTimeMillis() - this.lastMs) >= nextDelay) {
            return true;
        }
        return false;
    }
}

