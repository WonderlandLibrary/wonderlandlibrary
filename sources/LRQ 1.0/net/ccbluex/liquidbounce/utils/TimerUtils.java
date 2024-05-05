/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

public class TimerUtils {
    private static long prevMS;
    private static long lastMS;

    public TimerUtils() {
        prevMS = 0L;
        lastMS = -1L;
    }

    public static boolean delay(float milliSec) {
        return (float)(TimerUtils.getTime() - prevMS) >= milliSec;
    }

    public static void reset() {
        prevMS = TimerUtils.getTime();
    }

    public static long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public static long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public static boolean hasReached(long milliseconds) {
        return TimerUtils.getCurrentMS() - lastMS >= milliseconds;
    }

    public static boolean hasReached(double milliseconds) {
        return (double)(TimerUtils.getCurrentMS() - lastMS) >= milliseconds;
    }

    public boolean hasTimeElapsed(long time, boolean reset) {
        if (TimerUtils.getTime() >= time) {
            if (reset) {
                TimerUtils.reset();
            }
            return true;
        }
        return false;
    }
}

