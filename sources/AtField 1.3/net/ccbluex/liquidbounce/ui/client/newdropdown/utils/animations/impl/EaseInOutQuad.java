/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.impl;

import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Animation;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Direction;

public class EaseInOutQuad
extends Animation {
    public EaseInOutQuad(int n, double d, Direction direction) {
        super(n, d, direction);
    }

    public EaseInOutQuad(int n, double d) {
        super(n, d);
    }

    @Override
    protected double getEquation(double d) {
        double d2 = d / (double)this.duration;
        return d2 < 0.5 ? 2.0 * Math.pow(d2, 2.0) : 1.0 - Math.pow(-2.0 * d2 + 2.0, 2.0) / 2.0;
    }
}

