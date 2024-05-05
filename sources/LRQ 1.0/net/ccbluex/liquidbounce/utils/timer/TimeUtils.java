/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.timer;

import net.ccbluex.liquidbounce.utils.misc.RandomUtils;

public final class TimeUtils {
    private long lastMS;

    public static long randomDelay(int minDelay, int maxDelay) {
        return RandomUtils.nextInt(minDelay, maxDelay);
    }

    public static long randomClickDelay(int minCPS2, int maxCPS2) {
        return (long)(Math.random() * (double)(1000 / minCPS2 - 1000 / maxCPS2 + 1) + (double)(1000 / maxCPS2));
    }

    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasReached(double milliseconds) {
        return (double)(this.getCurrentMS() - this.lastMS) >= milliseconds;
    }

    public boolean hasreached(long milliseconds) {
        return (double)(this.getCurrentMS() - this.lastMS) >= (double)milliseconds;
    }

    public void reset() {
        this.lastMS = this.getCurrentMS();
    }

    public boolean delay(float milliSec) {
        return (float)(this.getTime() - this.lastMS) >= milliSec;
    }

    public static long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public boolean sleep(long time) {
        if (this.getTime() >= time) {
            this.reset();
            return true;
        }
        return false;
    }

    public boolean sleep(double time) {
        if ((double)this.getTime() >= time) {
            this.reset();
            return true;
        }
        return false;
    }
}

