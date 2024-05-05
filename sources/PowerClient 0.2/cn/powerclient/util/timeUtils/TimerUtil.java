/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.util.timeUtils;

public final class TimerUtil {
    public double time = System.nanoTime() / 1000000L;

    public boolean hasTimeElapsed(double time, boolean reset) {
        if (this.getTime() >= time) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }

    public double getTime() {
        return (double)(System.nanoTime() / 1000000L) - this.time;
    }

    public void reset() {
        this.time = System.nanoTime() / 1000000L;
    }
}

