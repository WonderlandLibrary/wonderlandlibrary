/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Application;

public abstract class Timer {
    private static final double UNSET_PERIOD = -1.0;
    private static final double SET_PERIOD = -2.0;
    private final Runnable runnable;
    private long ptr;
    private double period = -1.0;

    protected abstract long _start(Runnable var1);

    protected abstract long _start(Runnable var1, int var2);

    protected abstract void _stop(long var1);

    protected abstract void _pause(long var1);

    protected abstract void _resume(long var1);

    protected Timer(Runnable runnable) {
        if (runnable == null) {
            throw new IllegalArgumentException("runnable shouldn't be null");
        }
        this.runnable = runnable;
    }

    public static int getMinPeriod() {
        return Application.GetApplication().staticTimer_getMinPeriod();
    }

    public static int getMaxPeriod() {
        return Application.GetApplication().staticTimer_getMaxPeriod();
    }

    public synchronized void start(int n) {
        if (n < Timer.getMinPeriod() || n > Timer.getMaxPeriod()) {
            throw new IllegalArgumentException("period is out of range");
        }
        if (this.ptr != 0L) {
            this.stop();
        }
        this.ptr = this._start(this.runnable, n);
        if (this.ptr == 0L) {
            this.period = -1.0;
            throw new RuntimeException("Failed to start the timer");
        }
        this.period = n;
    }

    public synchronized void start() {
        if (this.ptr != 0L) {
            this.stop();
        }
        this.ptr = this._start(this.runnable);
        if (this.ptr == 0L) {
            this.period = -1.0;
            throw new RuntimeException("Failed to start the timer");
        }
        this.period = -2.0;
    }

    public synchronized void stop() {
        if (this.ptr != 0L) {
            this._stop(this.ptr);
            this.ptr = 0L;
            this.period = -1.0;
        }
    }

    public synchronized void pause() {
        if (this.ptr != 0L) {
            this._pause(this.ptr);
        }
    }

    public synchronized void resume() {
        if (this.ptr != 0L) {
            this._resume(this.ptr);
        }
    }

    public synchronized boolean isRunning() {
        return this.period != -1.0;
    }
}

