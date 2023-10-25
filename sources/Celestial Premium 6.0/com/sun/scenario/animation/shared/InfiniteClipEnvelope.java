/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.Animation
 *  javafx.animation.Animation$Status
 *  javafx.util.Duration
 */
package com.sun.scenario.animation.shared;

import com.sun.scenario.animation.shared.AnimationAccessor;
import com.sun.scenario.animation.shared.ClipEnvelope;
import com.sun.scenario.animation.shared.MultiLoopClipEnvelope;
import javafx.animation.Animation;
import javafx.util.Duration;

public class InfiniteClipEnvelope
extends MultiLoopClipEnvelope {
    protected InfiniteClipEnvelope(Animation animation) {
        super(animation);
        if (animation != null) {
            this.autoReverse = animation.isAutoReverse();
        }
    }

    @Override
    public ClipEnvelope setCycleDuration(Duration duration) {
        if (duration.isIndefinite()) {
            return InfiniteClipEnvelope.create(this.animation);
        }
        this.updateCycleTicks(duration);
        return this;
    }

    @Override
    public ClipEnvelope setCycleCount(int n) {
        return n != -1 ? InfiniteClipEnvelope.create(this.animation) : this;
    }

    @Override
    public void setRate(double d) {
        Animation.Status status = this.animation.getStatus();
        if (status != Animation.Status.STOPPED) {
            this.setInternalCurrentRate(Math.abs(this.currentRate - this.rate) < 1.0E-12 ? d : -d);
            this.deltaTicks = this.ticks - this.ticksRateChange(d);
            if (this.isDirectionChanged(d)) {
                long l = 2L * this.cycleTicks - this.cyclePos;
                this.deltaTicks += l;
                this.ticks += l;
            }
            this.abortCurrentPulse();
        }
        this.rate = d;
    }

    @Override
    protected double calculateCurrentRate() {
        return !this.autoReverse ? this.rate : (this.isDuringEvenCycle() ? this.rate : -this.rate);
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
            this.ticks = Math.max(0L, this.deltaTicks + l4);
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
                if (this.autoReverse) {
                    this.setCurrentRate(-this.currentRate);
                } else {
                    this.cyclePos = this.currentRate > 0.0 ? 0L : this.cycleTicks;
                    AnimationAccessor.getDefault().jumpTo(this.animation, this.cyclePos, this.cycleTicks, false);
                }
                l2 = this.cycleTicks;
            }
            if (l5 > 0L) {
                this.cyclePos += this.currentRate > 0.0 ? l5 : -l5;
                AnimationAccessor.getDefault().playTo(this.animation, this.cyclePos, this.cycleTicks);
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
        this.ticks = Math.max(0L, l) % (2L * this.cycleTicks);
        long l3 = this.ticks - l2;
        if (l3 != 0L) {
            this.deltaTicks += l3;
            if (this.autoReverse) {
                if (this.ticks > this.cycleTicks) {
                    this.cyclePos = 2L * this.cycleTicks - this.ticks;
                    if (this.animation.getStatus() == Animation.Status.RUNNING) {
                        this.setCurrentRate(-this.rate);
                    }
                } else {
                    this.cyclePos = this.ticks;
                    if (this.animation.getStatus() == Animation.Status.RUNNING) {
                        this.setCurrentRate(this.rate);
                    }
                }
            } else {
                this.cyclePos = this.ticks % this.cycleTicks;
                if (this.cyclePos == 0L) {
                    this.cyclePos = this.ticks;
                }
            }
            AnimationAccessor.getDefault().jumpTo(this.animation, this.cyclePos, this.cycleTicks, false);
            this.abortCurrentPulse();
        }
    }
}

