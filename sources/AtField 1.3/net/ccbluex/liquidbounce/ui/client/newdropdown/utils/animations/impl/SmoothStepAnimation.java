/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.impl;

import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Animation;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Direction;

public class SmoothStepAnimation
extends Animation {
    public SmoothStepAnimation(int n, double d) {
        super(n, d);
    }

    @Override
    protected double getEquation(double d) {
        double d2 = d / (double)this.duration;
        return -2.0 * Math.pow(d2, 3.0) + 3.0 * Math.pow(d2, 2.0);
    }

    public SmoothStepAnimation(int n, double d, Direction direction) {
        super(n, d, direction);
    }
}

