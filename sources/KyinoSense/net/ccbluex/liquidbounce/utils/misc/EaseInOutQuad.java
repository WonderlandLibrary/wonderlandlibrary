/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.misc;

import net.ccbluex.liquidbounce.utils.misc.Direction;
import net.ccbluex.liquidbounce.utils.render.Animation;

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
        double x2 = x1 / (double)this.duration;
        return x2 < 0.5 ? 2.0 * Math.pow(x2, 2.0) : 1.0 - Math.pow(-2.0 * x2 + 2.0, 2.0) / 2.0;
    }
}

