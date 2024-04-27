/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.impl;

import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Animation;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Direction;

public class EaseBackIn
extends Animation {
    private final float easeAmount;

    @Override
    protected double getEquation(double d) {
        double d2 = d / (double)this.duration;
        float f = this.easeAmount + 1.0f;
        return Math.max(0.0, 1.0 + (double)f * Math.pow(d2 - 1.0, 3.0) + (double)this.easeAmount * Math.pow(d2 - 1.0, 2.0));
    }

    public EaseBackIn(int n, double d, float f, Direction direction) {
        super(n, d, direction);
        this.easeAmount = f;
    }

    public EaseBackIn(int n, double d, float f) {
        super(n, d);
        this.easeAmount = f;
    }

    @Override
    protected boolean correctOutput() {
        return true;
    }
}

