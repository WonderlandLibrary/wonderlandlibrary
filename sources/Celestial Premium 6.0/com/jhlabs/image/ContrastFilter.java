/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.TransferFilter;

public class ContrastFilter
extends TransferFilter {
    private float brightness = 1.0f;
    private float contrast = 1.0f;

    @Override
    protected float transferFunction(float f) {
        f *= this.brightness;
        f = (f - 0.5f) * this.contrast + 0.5f;
        return f;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
        this.initialized = false;
    }

    public float getBrightness() {
        return this.brightness;
    }

    public void setContrast(float contrast) {
        this.contrast = contrast;
        this.initialized = false;
    }

    public float getContrast() {
        return this.contrast;
    }

    public String toString() {
        return "Colors/Contrast...";
    }
}

