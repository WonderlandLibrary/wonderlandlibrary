/*
 * Decompiled with CFR 0.152.
 */
package me.report.liquidware.utils;

public class TimerUtils {
    private static long prevMS;

    public static boolean hasReached(long milliseconds) {
        int lastMS = 0;
        return TimerUtils.getCurrentMS() - (long)lastMS >= milliseconds;
    }

    public static long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public static void reset() {
        prevMS = TimerUtils.getTime();
    }

    public static long getTime() {
        return System.nanoTime() / 1000000L;
    }
}

