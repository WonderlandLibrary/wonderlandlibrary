/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.TransferFilter;

public class RescaleFilter
extends TransferFilter {
    private float scale = 1.0f;

    public RescaleFilter() {
    }

    public RescaleFilter(float scale) {
        this.scale = scale;
    }

    @Override
    protected float transferFunction(float v) {
        return v * this.scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
        this.initialized = false;
    }

    public float getScale() {
        return this.scale;
    }

    public String toString() {
        return "Colors/Rescale...";
    }
}

