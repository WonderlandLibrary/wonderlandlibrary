/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.util.Duration
 */
package com.sun.javafx.animation;

import javafx.util.Duration;

public class TickCalculation {
    public static final int TICKS_PER_SECOND = 6000;
    private static final double TICKS_PER_MILLI = 6.0;
    private static final double TICKS_PER_NANO = 6.0E-6;

    private TickCalculation() {
    }

    public static long add(long l, long l2) {
        assert (l >= 0L);
        if (l == Long.MAX_VALUE || l2 == Long.MAX_VALUE) {
            return Long.MAX_VALUE;
        }
        if (l2 == Long.MIN_VALUE) {
            return 0L;
        }
        if (l2 >= 0L) {
            long l3 = l + l2;
            return l3 < 0L ? Long.MAX_VALUE : l3;
        }
        return Math.max(0L, l + l2);
    }

    public static long sub(long l, long l2) {
        assert (l >= 0L);
        if (l == Long.MAX_VALUE || l2 == Long.MIN_VALUE) {
            return Long.MAX_VALUE;
        }
        if (l2 == Long.MAX_VALUE) {
            return 0L;
        }
        if (l2 >= 0L) {
            return Math.max(0L, l - l2);
        }
        long l3 = l - l2;
        return l3 < 0L ? Long.MAX_VALUE : l3;
    }

    public static long fromMillis(double d) {
        return Math.round(6.0 * d);
    }

    public static long fromNano(long l) {
        return Math.round(6.0E-6 * (double)l);
    }

    public static long fromDuration(Duration duration) {
        return TickCalculation.fromMillis(duration.toMillis());
    }

    public static long fromDuration(Duration duration, double d) {
        return Math.round(6.0 * duration.toMillis() / Math.abs(d));
    }

    public static Duration toDuration(long l) {
        return Duration.millis((double)TickCalculation.toMillis(l));
    }

    public static double toMillis(long l) {
        return (double)l / 6.0;
    }
}

