/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.util.Callback
 */
package com.sun.scenario.animation;

import com.sun.javafx.animation.TickCalculation;
import com.sun.scenario.DelayedRunnable;
import com.sun.scenario.Settings;
import com.sun.scenario.animation.AnimationPulse;
import com.sun.scenario.animation.shared.PulseReceiver;
import com.sun.scenario.animation.shared.TimerReceiver;
import java.util.Arrays;
import javafx.util.Callback;

public abstract class AbstractPrimaryTimer {
    protected static final String FULLSPEED_PROP = "javafx.animation.fullspeed";
    private static boolean fullspeed = Settings.getBoolean("javafx.animation.fullspeed");
    protected static final String ADAPTIVE_PULSE_PROP = "com.sun.scenario.animation.adaptivepulse";
    private static boolean useAdaptivePulse = Settings.getBoolean("com.sun.scenario.animation.adaptivepulse");
    protected static final String PULSE_PROP = "javafx.animation.pulse";
    protected static final String FRAMERATE_PROP = "javafx.animation.framerate";
    protected static final String FIXED_PULSE_LENGTH_PROP = "com.sun.scenario.animation.fixed.pulse.length";
    protected static final String ANIMATION_MBEAN_ENABLED = "com.sun.scenario.animation.AnimationMBean.enabled";
    protected static final boolean enableAnimationMBean = false;
    private final int PULSE_DURATION_NS = this.getPulseDuration(1000000000);
    private final int PULSE_DURATION_TICKS = this.getPulseDuration((int)TickCalculation.fromMillis(1000.0));
    private static Callback<String, Void> pcl = string -> {
        switch (string) {
            case "javafx.animation.fullspeed": {
                fullspeed = Settings.getBoolean("javafx.animation.fullspeed");
                break;
            }
            case "com.sun.scenario.animation.adaptivepulse": {
                useAdaptivePulse = Settings.getBoolean("com.sun.scenario.animation.adaptivepulse");
                break;
            }
            case "com.sun.scenario.animation.AnimationMBean.enabled": {
                AnimationPulse.getDefaultBean().setEnabled(Settings.getBoolean("com.sun.scenario.animation.AnimationMBean.enabled"));
            }
        }
        return null;
    };
    private boolean paused = false;
    private long totalPausedTime;
    private long startPauseTime;
    private PulseReceiver[] receivers = new PulseReceiver[2];
    private int receiversLength;
    private boolean receiversLocked;
    private TimerReceiver[] animationTimers = new TimerReceiver[2];
    private int animationTimersLength;
    private boolean animationTimersLocked;
    private final long fixedPulseLength = Boolean.getBoolean("com.sun.scenario.animation.fixed.pulse.length") ? (long)this.PULSE_DURATION_NS : 0L;
    private long debugNanos = 0L;
    private final MainLoop theMainLoop = new MainLoop();

    boolean isPaused() {
        return this.paused;
    }

    long getTotalPausedTime() {
        return this.totalPausedTime;
    }

    long getStartPauseTime() {
        return this.startPauseTime;
    }

    public int getDefaultResolution() {
        return this.PULSE_DURATION_TICKS;
    }

    public void pause() {
        if (!this.paused) {
            this.startPauseTime = this.nanos();
            this.paused = true;
        }
    }

    public void resume() {
        if (this.paused) {
            this.paused = false;
            this.totalPausedTime += this.nanos() - this.startPauseTime;
        }
    }

    public long nanos() {
        if (this.fixedPulseLength > 0L) {
            return this.debugNanos;
        }
        return this.paused ? this.startPauseTime : System.nanoTime() - this.totalPausedTime;
    }

    public boolean isFullspeed() {
        return fullspeed;
    }

    protected AbstractPrimaryTimer() {
    }

    public void addPulseReceiver(PulseReceiver pulseReceiver) {
        boolean bl;
        boolean bl2 = bl = this.receiversLength == this.receivers.length;
        if (this.receiversLocked || bl) {
            this.receivers = Arrays.copyOf(this.receivers, bl ? this.receivers.length * 3 / 2 + 1 : this.receivers.length);
            this.receiversLocked = false;
        }
        this.receivers[this.receiversLength++] = pulseReceiver;
        if (this.receiversLength == 1) {
            this.theMainLoop.updateAnimationRunnable();
        }
    }

    public void removePulseReceiver(PulseReceiver pulseReceiver) {
        if (this.receiversLocked) {
            this.receivers = (PulseReceiver[])this.receivers.clone();
            this.receiversLocked = false;
        }
        for (int i = 0; i < this.receiversLength; ++i) {
            if (pulseReceiver != this.receivers[i]) continue;
            if (i == this.receiversLength - 1) {
                this.receivers[i] = null;
            } else {
                System.arraycopy(this.receivers, i + 1, this.receivers, i, this.receiversLength - i - 1);
                this.receivers[this.receiversLength - 1] = null;
            }
            --this.receiversLength;
            break;
        }
        if (this.receiversLength == 0) {
            this.theMainLoop.updateAnimationRunnable();
        }
    }

    public void addAnimationTimer(TimerReceiver timerReceiver) {
        boolean bl;
        boolean bl2 = bl = this.animationTimersLength == this.animationTimers.length;
        if (this.animationTimersLocked || bl) {
            this.animationTimers = Arrays.copyOf(this.animationTimers, bl ? this.animationTimers.length * 3 / 2 + 1 : this.animationTimers.length);
            this.animationTimersLocked = false;
        }
        this.animationTimers[this.animationTimersLength++] = timerReceiver;
        if (this.animationTimersLength == 1) {
            this.theMainLoop.updateAnimationRunnable();
        }
    }

    public void removeAnimationTimer(TimerReceiver timerReceiver) {
        if (this.animationTimersLocked) {
            this.animationTimers = (TimerReceiver[])this.animationTimers.clone();
            this.animationTimersLocked = false;
        }
        for (int i = 0; i < this.animationTimersLength; ++i) {
            if (timerReceiver != this.animationTimers[i]) continue;
            if (i == this.animationTimersLength - 1) {
                this.animationTimers[i] = null;
            } else {
                System.arraycopy(this.animationTimers, i + 1, this.animationTimers, i, this.animationTimersLength - i - 1);
                this.animationTimers[this.animationTimersLength - 1] = null;
            }
            --this.animationTimersLength;
            break;
        }
        if (this.animationTimersLength == 0) {
            this.theMainLoop.updateAnimationRunnable();
        }
    }

    protected void recordStart(long l) {
    }

    protected void recordEnd() {
    }

    protected void recordAnimationEnd() {
    }

    protected abstract void postUpdateAnimationRunnable(DelayedRunnable var1);

    protected abstract int getPulseDuration(int var1);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void timePulseImpl(long l) {
        if (this.fixedPulseLength > 0L) {
            this.debugNanos += this.fixedPulseLength;
            l = this.debugNanos;
        }
        PulseReceiver[] arrpulseReceiver = this.receivers;
        int n = this.receiversLength;
        try {
            this.receiversLocked = true;
            for (int i = 0; i < n; ++i) {
                arrpulseReceiver[i].timePulse(TickCalculation.fromNano(l));
            }
        }
        finally {
            this.receiversLocked = false;
        }
        this.recordAnimationEnd();
        TimerReceiver[] arrtimerReceiver = this.animationTimers;
        int n2 = this.animationTimersLength;
        try {
            this.animationTimersLocked = true;
            for (int i = 0; i < n2; ++i) {
                arrtimerReceiver[i].handle(l);
            }
        }
        finally {
            this.animationTimersLocked = false;
        }
    }

    static {
        Settings.addPropertyChangeListener(pcl);
        int n = Settings.getInt("javafx.animation.pulse", -1);
        if (n != -1) {
            System.err.println("Setting PULSE_DURATION to " + n + " hz");
        }
    }

    private final class MainLoop
    implements DelayedRunnable {
        private boolean inactive = true;
        private long nextPulseTime = AbstractPrimaryTimer.this.nanos();
        private long lastPulseDuration = Integer.MIN_VALUE;

        private MainLoop() {
        }

        @Override
        public void run() {
            if (AbstractPrimaryTimer.this.paused) {
                return;
            }
            long l = AbstractPrimaryTimer.this.nanos();
            AbstractPrimaryTimer.this.recordStart((this.nextPulseTime - l) / 1000000L);
            AbstractPrimaryTimer.this.timePulseImpl(l);
            AbstractPrimaryTimer.this.recordEnd();
            this.updateNextPulseTime(l);
            this.updateAnimationRunnable();
        }

        @Override
        public long getDelay() {
            long l = AbstractPrimaryTimer.this.nanos();
            long l2 = (this.nextPulseTime - l) / 1000000L;
            return Math.max(0L, l2);
        }

        private void updateNextPulseTime(long l) {
            long l2 = AbstractPrimaryTimer.this.nanos();
            if (fullspeed) {
                this.nextPulseTime = l2;
            } else if (useAdaptivePulse) {
                this.nextPulseTime += (long)AbstractPrimaryTimer.this.PULSE_DURATION_NS;
                long l3 = l2 - l;
                if (l3 - this.lastPulseDuration > 500000L) {
                    l3 /= 2L;
                }
                if (l3 < 2000000L) {
                    l3 = 2000000L;
                }
                if (l3 >= (long)AbstractPrimaryTimer.this.PULSE_DURATION_NS) {
                    l3 = 3 * AbstractPrimaryTimer.this.PULSE_DURATION_NS / 4;
                }
                this.lastPulseDuration = l3;
                this.nextPulseTime -= l3;
            } else {
                this.nextPulseTime = (this.nextPulseTime + (long)AbstractPrimaryTimer.this.PULSE_DURATION_NS) / (long)AbstractPrimaryTimer.this.PULSE_DURATION_NS * (long)AbstractPrimaryTimer.this.PULSE_DURATION_NS;
            }
        }

        private void updateAnimationRunnable() {
            boolean bl;
            boolean bl2 = bl = AbstractPrimaryTimer.this.animationTimersLength == 0 && AbstractPrimaryTimer.this.receiversLength == 0;
            if (this.inactive != bl) {
                this.inactive = bl;
                MainLoop mainLoop = this.inactive ? null : this;
                AbstractPrimaryTimer.this.postUpdateAnimationRunnable(mainLoop);
            }
        }
    }
}

