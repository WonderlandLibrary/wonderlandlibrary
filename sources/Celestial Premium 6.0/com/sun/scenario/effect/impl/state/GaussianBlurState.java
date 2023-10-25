/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.impl.state.GaussianRenderState;
import com.sun.scenario.effect.impl.state.GaussianShadowState;
import com.sun.scenario.effect.impl.state.HVSeparableKernel;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;

public class GaussianBlurState
extends HVSeparableKernel {
    private float hradius;
    private float vradius;

    void checkRadius(float f) {
        if (f < 0.0f || f > 63.0f) {
            throw new IllegalArgumentException("Radius must be in the range [1,63]");
        }
    }

    public float getRadius() {
        return (this.hradius + this.vradius) / 2.0f;
    }

    public void setRadius(float f) {
        this.checkRadius(f);
        this.hradius = f;
        this.vradius = f;
    }

    public float getHRadius() {
        return this.hradius;
    }

    public void setHRadius(float f) {
        this.checkRadius(f);
        this.hradius = f;
    }

    public float getVRadius() {
        return this.vradius;
    }

    public void setVRadius(float f) {
        this.checkRadius(f);
        this.vradius = f;
    }

    float getRadius(int n) {
        return n == 0 ? this.hradius : this.vradius;
    }

    @Override
    public boolean isNop() {
        return this.hradius == 0.0f && this.vradius == 0.0f;
    }

    public int getPad(int n) {
        return (int)Math.ceil(this.getRadius(n));
    }

    @Override
    public int getKernelSize(int n) {
        return this.getPad(n) * 2 + 1;
    }

    public float getSpread() {
        return 0.0f;
    }

    public Color4f getShadowColor() {
        return null;
    }

    @Override
    public LinearConvolveRenderState getRenderState(BaseTransform baseTransform) {
        return new GaussianRenderState(this.hradius, this.vradius, this.getSpread(), this instanceof GaussianShadowState, this.getShadowColor(), baseTransform);
    }
}

