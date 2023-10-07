/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.animations.impl;

import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.animations.Animation;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.animations.Direction;

public class EaseBackIn
extends Animation {
    private final float easeAmount;

    public EaseBackIn(int ms, double endPoint, float easeAmount) {
        super(ms, endPoint);
        this.easeAmount = easeAmount;
    }

    public EaseBackIn(int ms, double endPoint, float easeAmount, Direction direction) {
        super(ms, endPoint, direction);
        this.easeAmount = easeAmount;
    }

    @Override
    protected boolean correctOutput() {
        return true;
    }

    @Override
    protected double getEquation(double x) {
        double x1 = x / (double)this.duration;
        float shrink = this.easeAmount + 1.0f;
        return Math.max(0.0, 1.0 + (double)shrink * Math.pow(x1 - 1.0, 3.0) + (double)this.easeAmount * Math.pow(x1 - 1.0, 2.0));
    }
}

