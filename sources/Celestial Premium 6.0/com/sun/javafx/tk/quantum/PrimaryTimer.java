/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.tk.Toolkit;
import com.sun.scenario.DelayedRunnable;
import com.sun.scenario.Settings;
import com.sun.scenario.animation.AbstractPrimaryTimer;
import com.sun.scenario.animation.AnimationPulse;
import java.util.Map;

public final class PrimaryTimer
extends AbstractPrimaryTimer {
    private static final Object PRIMARY_TIMER_KEY = new StringBuilder("PrimaryTimerKey");

    private PrimaryTimer() {
    }

    public static synchronized PrimaryTimer getInstance() {
        Map<Object, Object> map = Toolkit.getToolkit().getContextMap();
        PrimaryTimer primaryTimer = (PrimaryTimer)map.get(PRIMARY_TIMER_KEY);
        if (primaryTimer == null) {
            primaryTimer = new PrimaryTimer();
            map.put(PRIMARY_TIMER_KEY, primaryTimer);
            if (Settings.getBoolean("com.sun.scenario.animation.AnimationMBean.enabled", false)) {
                AnimationPulse.getDefaultBean().setEnabled(true);
            }
        }
        return primaryTimer;
    }

    @Override
    protected int getPulseDuration(int n) {
        int n2 = n / 60;
        if (Settings.get("javafx.animation.framerate") != null) {
            int n3 = Settings.getInt("javafx.animation.framerate", 60);
            if (n3 > 0) {
                n2 = n / n3;
            }
        } else if (Settings.get("javafx.animation.pulse") != null) {
            int n4 = Settings.getInt("javafx.animation.pulse", 60);
            if (n4 > 0) {
                n2 = n / n4;
            }
        } else {
            int n5 = Toolkit.getToolkit().getRefreshRate();
            if (n5 > 0) {
                n2 = n / n5;
            }
        }
        return n2;
    }

    @Override
    protected void postUpdateAnimationRunnable(DelayedRunnable delayedRunnable) {
        Toolkit.getToolkit().setAnimationRunnable(delayedRunnable);
    }

    @Override
    protected void recordStart(long l) {
        AnimationPulse.getDefaultBean().recordStart(l);
    }

    @Override
    protected void recordEnd() {
        AnimationPulse.getDefaultBean().recordEnd();
    }

    @Override
    protected void recordAnimationEnd() {
        AnimationPulse.getDefaultBean().recordAnimationEnd();
    }
}

