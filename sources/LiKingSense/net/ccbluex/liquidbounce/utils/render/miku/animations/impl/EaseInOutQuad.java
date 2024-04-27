/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render.miku.animations.impl;

import net.ccbluex.liquidbounce.utils.render.miku.animations.Animation;
import net.ccbluex.liquidbounce.utils.render.miku.animations.Direction;

public class EaseInOutQuad
extends Animation {
    public EaseInOutQuad(int ms, double endPoint) {
        super(ms, endPoint);
    }

    public EaseInOutQuad(int ms, double endPoint, Direction direction) {
        super(ms, endPoint, direction);
    }

    @Override
    protected double getEquation(double x1) {
        double x = x1 / (double)this.duration;
        return x < 0.5 ? 2.0 * Math.pow(x, 2.0) : 1.0 - Math.pow(-2.0 * x + 2.0, 2.0) / 2.0;
    }
}

