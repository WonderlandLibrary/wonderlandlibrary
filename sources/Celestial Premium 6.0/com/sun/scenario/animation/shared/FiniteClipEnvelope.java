/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.Animation
 *  javafx.animation.Animation$Status
 *  javafx.util.Duration
 */
package com.sun.scenario.animation.shared;

import com.sun.javafx.util.Utils;
import com.sun.scenario.animation.shared.AnimationAccessor;
import com.sun.scenario.animation.shared.ClipEnvelope;
import com.sun.scenario.animation.shared.MultiLoopClipEnvelope;
import javafx.animation.Animation;
import javafx.util.Duration;

public class FiniteClipEnvelope
extends MultiLoopClipEnvelope {
    private int cycleCount;
    private long totalTicks;

    protected FiniteClipEnvelope(Animation animation) {
        super(animation);
        if (animation != null) {
            this.autoReverse = animation.isAutoReverse();
            this.cycleCount = animation.getCycleCount();
        }
        this.updateTotalTicks();
    }

    @Override
    public ClipEnvelope setCycleDuration(Duration duration) {
        if (duration.isIndefinite()) {
            return FiniteClipEnvelope.create(this.animation);
        }
        this.updateCycleTicks(duration);
        this.updateTotalTicks();
        return this;
    }

    @Override
    public ClipEnvelope setCycleCount(int n) {
        if (n == 1 || n == -1) {
            return FiniteClipEnvelope.create(this.animation);
        }
        this.cycleCount = n;
        this.updateTotalTicks();
        return this;
    }

    @Override
    public void setRate(double d) {
        boolean bl = this.isDirectionChanged(d);
        long l = bl ? this.totalTicks - this.ticks : this.ticks;
        Animation.Status status = this.animation.getStatus();
        if (status != Animation.Status.STOPPED) {
            this.setInternalCurrentRate(Math.abs(this.currentRate - this.rate) < 1.0E-12 ? d : -d);
            this.deltaTicks = l - this.ticksRateChange(d);
            this.abortCurrentPulse();
        }
        this.ticks = l;
        this.rate = d;
    }

    @Override
    protected double calculateCurrentRate() {
        return !this.autoReverse ? this.rate : (this.isDuringEvenCycle() == this.rate > 0.0 ? this.rate : -this.rate);
    }

    private void updateTotalTicks() {
        this.totalTicks = (long)this.cycleCount * this.cycleTicks;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void timePulse(long l) {
        if (this.cycleTicks == 0L) {
            return;
        }
        this.aborted = false;
        this.inTimePulse = true;
        try {
            long l2;
            long l3 = this.ticks;
            long l4 = Math.round((double)l * Math.abs(this.rate));
            this.ticks = Utils.clamp(0L, this.deltaTicks + l4, this.totalTicks);
            boolean bl = this.ticks >= this.totalTicks;
            long l5 = this.ticks - l3;
            if (l5 == 0L) {
                return;
            }
            long l6 = l2 = this.currentRate > 0.0 ? this.cycleTicks - this.cyclePos : this.cyclePos;
            while (l5 >= l2) {
                if (l2 > 0L) {
                    this.cyclePos = this.currentRate > 0.0 ? this.cycleTicks : 0L;
                    l5 -= l2;
                    AnimationAccessor.getDefault().playTo(this.animation, this.cyclePos, this.cycleTicks);
                    if (this.aborted) {
                        return;
                    }
                }
                if (!bl || l5 > 0L) {
                    if (this.autoReverse) {
                        this.setCurrentRate(-this.currentRate);
                    } else {
                        this.cyclePos = this.currentRate > 0.0 ? 0L : this.cycleTicks;
                        AnimationAccessor.getDefault().jumpTo(this.animation, this.cyclePos, this.cycleTicks, false);
                    }
                }
                l2 = this.cycleTicks;
            }
            if (l5 > 0L && !bl) {
                this.cyclePos += this.currentRate > 0.0 ? l5 : -l5;
                AnimationAccessor.getDefault().playTo(this.animation, this.cyclePos, this.cycleTicks);
            }
            if (bl && !this.aborted) {
                AnimationAccessor.getDefault().finished(this.animation);
            }
        }
        finally {
            this.inTimePulse = false;
        }
    }

    @Override
    public void jumpTo(long l) {
        if (this.cycleTicks == 0L) {
            return;
        }
        long l2 = this.ticks;
        if (this.rate < 0.0) {
            l = this.totalTicks - l;
        }
        this.ticks = Utils.clamp(0L, l, this.totalTicks);
        long l3 = this.ticks - l2;
        if (l3 != 0L) {
            this.deltaTicks += l3;
            if (this.autoReverse) {
                boolean bl = this.ticks % (2L * this.cycleTicks) < this.cycleTicks;
                if (bl == this.rate > 0.0) {
                    this.cyclePos = this.ticks % this.cycleTicks;
                    if (this.animation.getStatus() == Animation.Status.RUNNING) {
                        this.setCurrentRate(Math.abs(this.rate));
                    }
                } else {
                    this.cyclePos = this.cycleTicks - this.ticks % this.cycleTicks;
                    if (this.animation.getStatus() == Animation.Status.RUNNING) {
                        this.setCurrentRate(-Math.abs(this.rate));
                    }
                }
            } else {
                this.cyclePos = this.ticks % this.cycleTicks;
                if (this.rate < 0.0) {
                    this.cyclePos = this.cycleTicks - this.cyclePos;
                }
                if (this.cyclePos == 0L && this.ticks > 0L) {
                    this.cyclePos = this.cycleTicks;
                }
            }
            AnimationAccessor.getDefault().jumpTo(this.animation, this.cyclePos, this.cycleTicks, false);
            this.abortCurrentPulse();
        }
    }
}

