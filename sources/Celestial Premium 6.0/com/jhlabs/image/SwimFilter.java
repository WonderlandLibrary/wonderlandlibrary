/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.TransformFilter;
import com.jhlabs.math.Noise;

public class SwimFilter
extends TransformFilter {
    private float scale = 32.0f;
    private float stretch = 1.0f;
    private float angle = 0.0f;
    private float amount = 1.0f;
    private float turbulence = 1.0f;
    private float time = 0.0f;
    private float m00 = 1.0f;
    private float m01 = 0.0f;
    private float m10 = 0.0f;
    private float m11 = 1.0f;

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return this.scale;
    }

    public void setStretch(float stretch) {
        this.stretch = stretch;
    }

    public float getStretch() {
        return this.stretch;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);
        this.m00 = cos;
        this.m01 = sin;
        this.m10 = -sin;
        this.m11 = cos;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setTurbulence(float turbulence) {
        this.turbulence = turbulence;
    }

    public float getTurbulence() {
        return this.turbulence;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public float getTime() {
        return this.time;
    }

    @Override
    protected void transformInverse(int x, int y, float[] out) {
        float nx = this.m00 * (float)x + this.m01 * (float)y;
        float ny = this.m10 * (float)x + this.m11 * (float)y;
        nx /= this.scale;
        ny /= this.scale * this.stretch;
        if (this.turbulence == 1.0f) {
            out[0] = (float)x + this.amount * Noise.noise3(nx + 0.5f, ny, this.time);
            out[1] = (float)y + this.amount * Noise.noise3(nx, ny + 0.5f, this.time);
        } else {
            out[0] = (float)x + this.amount * Noise.turbulence3(nx + 0.5f, ny, this.turbulence, this.time);
            out[1] = (float)y + this.amount * Noise.turbulence3(nx, ny + 0.5f, this.turbulence, this.time);
        }
    }

    public String toString() {
        return "Distort/Swim...";
    }
}

