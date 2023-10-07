/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.misc;

import net.ccbluex.liquidbounce.utils.misc.Direction;
import net.ccbluex.liquidbounce.utils.render.Animation2323;

public class SmoothStepAnimation
extends Animation2323 {
    public SmoothStepAnimation(int ms, double endPoint) {
        super(ms, endPoint);
    }

    public SmoothStepAnimation(int ms, double endPoint, Direction direction) {
        super(ms, endPoint, direction);
    }

    @Override
    protected double getEquation(double x) {
        double x2 = x / (double)this.duration;
        return -2.0 * Math.pow(x2, 3.0) + 3.0 * Math.pow(x2, 2.0);
    }
}

