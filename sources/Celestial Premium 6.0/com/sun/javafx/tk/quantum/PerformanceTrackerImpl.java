/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.perf.PerformanceTracker;
import com.sun.javafx.tk.quantum.PerformanceTrackerHelper;

final class PerformanceTrackerImpl
extends PerformanceTracker {
    final PerformanceTrackerHelper helper = PerformanceTrackerHelper.getInstance();

    public PerformanceTrackerImpl() {
        this.setPerfLoggingEnabled(this.helper.isPerfLoggingEnabled());
    }

    @Override
    public void doLogEvent(String string) {
        this.helper.logEvent(string);
    }

    @Override
    public void doOutputLog() {
        this.helper.outputLog();
    }

    @Override
    public long nanoTime() {
        return this.helper.nanoTime();
    }
}

