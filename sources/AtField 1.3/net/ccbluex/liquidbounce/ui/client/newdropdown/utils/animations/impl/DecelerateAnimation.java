/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.impl;

import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Animation;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Direction;

public class DecelerateAnimation
extends Animation {
    public DecelerateAnimation(int n, double d) {
        super(n, d);
    }

    @Override
    protected double getEquation(double d) {
        double d2 = d / (double)this.duration;
        return 1.0 - (d2 - 1.0) * (d2 - 1.0);
    }

    public DecelerateAnimation(int n, double d, Direction direction) {
        super(n, d, direction);
    }
}

