/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.state;

import com.sun.scenario.effect.ZoomRadialBlur;

public class ZoomRadialBlurState {
    private float dx = -1.0f;
    private float dy = -1.0f;
    private final ZoomRadialBlur effect;

    public ZoomRadialBlurState(ZoomRadialBlur zoomRadialBlur) {
        this.effect = zoomRadialBlur;
    }

    public int getRadius() {
        return this.effect.getRadius();
    }

    public void updateDeltas(float f, float f2) {
        this.dx = f;
        this.dy = f2;
    }

    public void invalidateDeltas() {
        this.dx = -1.0f;
        this.dy = -1.0f;
    }

    public float getDx() {
        return this.dx;
    }

    public float getDy() {
        return this.dy;
    }

    public int getNumSteps() {
        int n = this.getRadius();
        return n * 2 + 1;
    }

    public float getAlpha() {
        float f = this.getRadius();
        return 1.0f / (2.0f * f + 1.0f);
    }
}

