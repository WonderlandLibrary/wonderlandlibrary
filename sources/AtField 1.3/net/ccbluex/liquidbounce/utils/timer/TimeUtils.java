/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.timer;

import net.ccbluex.liquidbounce.utils.misc.RandomUtils;

public final class TimeUtils {
    private final long currentMS = System.currentTimeMillis();
    private long lastMS;

    public boolean hasReached(double d) {
        return (double)(this.getCurrentMS() - this.lastMS) >= d;
    }

    public boolean hasreached(long l) {
        return (double)(this.getCurrentMS() - this.lastMS) >= (double)l;
    }

    public boolean sleep(double d) {
        if ((double)TimeUtils.getTime() >= d) {
            this.reset();
            return true;
        }
        return false;
    }

    public void reset() {
        this.lastMS = this.getCurrentMS();
    }

    public static long randomClickDelay(int n, int n2) {
        return (long)(Math.random() * (double)(1000 / n - 1000 / n2 + 1) + (double)(1000 / n2));
    }

    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean delay(float f) {
        return (float)(TimeUtils.getTime() - this.lastMS) >= f;
    }

    public long elapsed() {
        return System.currentTimeMillis() - this.currentMS;
    }

    public static long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasElapsed(long l) {
        return this.elapsed() > l;
    }

    public boolean sleep(long l) {
        if (TimeUtils.getTime() >= l) {
            this.reset();
            return true;
        }
        return false;
    }

    public static long randomDelay(int n, int n2) {
        return RandomUtils.nextInt(n, n2);
    }
}

