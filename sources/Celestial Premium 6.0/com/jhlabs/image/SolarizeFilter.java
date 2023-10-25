/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.TransferFilter;

public class SolarizeFilter
extends TransferFilter {
    @Override
    protected float transferFunction(float v) {
        return v > 0.5f ? 2.0f * (v - 0.5f) : 2.0f * (0.5f - v);
    }

    public String toString() {
        return "Colors/Solarize";
    }
}

