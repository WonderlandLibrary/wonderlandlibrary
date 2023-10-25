/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.math;

import com.jhlabs.math.CompoundFunction2D;
import com.jhlabs.math.Function2D;

public class FractalSumFunction
extends CompoundFunction2D {
    private float octaves = 1.0f;

    public FractalSumFunction(Function2D basis) {
        super(basis);
    }

    @Override
    public float evaluate(float x, float y) {
        float t = 0.0f;
        for (float f = 1.0f; f <= this.octaves; f *= 2.0f) {
            t += this.basis.evaluate(f * x, f * y) / f;
        }
        return t;
    }
}

