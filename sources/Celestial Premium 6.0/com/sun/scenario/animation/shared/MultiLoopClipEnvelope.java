/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.Animation
 */
package com.sun.scenario.animation.shared;

import com.sun.scenario.animation.shared.ClipEnvelope;
import javafx.animation.Animation;

abstract class MultiLoopClipEnvelope
extends ClipEnvelope {
    protected boolean autoReverse;
    protected long cyclePos;

    protected MultiLoopClipEnvelope(Animation animation) {
        super(animation);
    }

    protected boolean isAutoReverse() {
        return this.autoReverse;
    }

    @Override
    public void setAutoReverse(boolean bl) {
        this.autoReverse = bl;
    }

    @Override
    protected long ticksRateChange(double d) {
        return Math.round((double)(this.ticks - this.deltaTicks) * Math.abs(d / this.rate));
    }

    protected boolean isDirectionChanged(double d) {
        return d * this.rate < 0.0;
    }

    protected boolean isDuringEvenCycle() {
        return this.ticks % (2L * this.cycleTicks) < this.cycleTicks;
    }
}

