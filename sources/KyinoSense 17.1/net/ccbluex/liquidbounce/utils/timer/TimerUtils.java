/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.MathHelper
 */
package net.ccbluex.liquidbounce.utils.timer;

import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.minecraft.util.MathHelper;

public final class TimerUtils {
    private long lastMS = 0L;
    private long previousTime = -1L;

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

    public boolean delay(float milliSec) {
        return (float)(this.getTime() - this.lastMS) >= milliSec;
    }

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasTimeElapsed(long time) {
        return System.currentTimeMillis() - this.lastMS > time;
    }

    public boolean check(float milliseconds) {
        return (float)(System.currentTimeMillis() - this.previousTime) >= milliseconds;
    }

    public boolean delay(double milliseconds) {
        return (double)MathHelper.func_76131_a((float)(this.getCurrentMS() - this.lastMS), (float)0.0f, (float)((float)milliseconds)) >= milliseconds;
    }

    public void reset() {
        this.previousTime = System.currentTimeMillis();
        this.lastMS = this.getCurrentMS();
    }

    public long time() {
        return System.nanoTime() / 1000000L - this.lastMS;
    }

    public boolean delay(long nextDelay) {
        return System.currentTimeMillis() - this.lastMS >= nextDelay;
    }

    public boolean delay(float nextDelay, boolean reset) {
        if ((float)(System.currentTimeMillis() - this.lastMS) >= nextDelay) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }

    public void setTime(long time) {
        this.lastMS = time;
    }
}

