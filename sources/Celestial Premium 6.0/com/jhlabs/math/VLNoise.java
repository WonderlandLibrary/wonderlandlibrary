/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.math;

import com.jhlabs.math.Function2D;
import com.jhlabs.math.Noise;

public class VLNoise
implements Function2D {
    private float distortion = 10.0f;

    public void setDistortion(float distortion) {
        this.distortion = distortion;
    }

    public float getDistortion() {
        return this.distortion;
    }

    @Override
    public float evaluate(float x, float y) {
        float ox = Noise.noise2(x + 0.5f, y) * this.distortion;
        float oy = Noise.noise2(x, y + 0.5f) * this.distortion;
        return Noise.noise2(x + ox, y + oy);
    }
}

