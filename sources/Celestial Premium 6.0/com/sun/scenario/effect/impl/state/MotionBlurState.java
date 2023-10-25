/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.state.GaussianRenderState;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;

public class MotionBlurState
extends LinearConvolveKernel {
    private float radius;
    private float angle;

    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float f) {
        if (f < 0.0f || f > 63.0f) {
            throw new IllegalArgumentException("Radius must be in the range [0,63]");
        }
        this.radius = f;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setAngle(float f) {
        this.angle = f;
    }

    public int getHPad() {
        return (int)Math.ceil(Math.abs(Math.cos(this.angle)) * (double)this.radius);
    }

    public int getVPad() {
        return (int)Math.ceil(Math.abs(Math.sin(this.angle)) * (double)this.radius);
    }

    @Override
    public LinearConvolveRenderState getRenderState(BaseTransform baseTransform) {
        float f = (float)Math.cos(this.angle);
        float f2 = (float)Math.sin(this.angle);
        return new GaussianRenderState(this.radius, f, f2, baseTransform);
    }

    @Override
    public boolean isNop() {
        return this.radius == 0.0f;
    }

    @Override
    public int getKernelSize(int n) {
        return (int)Math.ceil(this.radius) * 2 + 1;
    }

    @Override
    public final Rectangle getResultBounds(Rectangle rectangle, int n) {
        Rectangle rectangle2 = new Rectangle(rectangle);
        rectangle2.grow(this.getHPad(), this.getVPad());
        return rectangle2;
    }
}

