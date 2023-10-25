/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.Timer;

final class WinTimer
extends Timer {
    private static final int minPeriod = WinTimer._getMinPeriod();
    private static final int maxPeriod = WinTimer._getMaxPeriod();

    protected WinTimer(Runnable runnable) {
        super(runnable);
    }

    private static native int _getMinPeriod();

    private static native int _getMaxPeriod();

    static int getMinPeriod_impl() {
        return minPeriod;
    }

    static int getMaxPeriod_impl() {
        return maxPeriod;
    }

    @Override
    protected long _start(Runnable runnable) {
        throw new RuntimeException("vsync timer not supported");
    }

    @Override
    protected native long _start(Runnable var1, int var2);

    @Override
    protected native void _stop(long var1);

    @Override
    protected void _pause(long l) {
    }

    @Override
    protected void _resume(long l) {
    }
}

