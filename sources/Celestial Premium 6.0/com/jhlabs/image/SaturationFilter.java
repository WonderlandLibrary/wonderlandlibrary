/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.PixelUtils;
import com.jhlabs.image.PointFilter;

public class SaturationFilter
extends PointFilter {
    public float amount = 1.0f;

    public SaturationFilter() {
    }

    public SaturationFilter(float amount) {
        this.amount = amount;
        this.canFilterIndexColorModel = true;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return this.amount;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        if (this.amount != 1.0f) {
            int a = rgb & 0xFF000000;
            int r = rgb >> 16 & 0xFF;
            int g = rgb >> 8 & 0xFF;
            int b = rgb & 0xFF;
            int v = (r + g + b) / 3;
            r = PixelUtils.clamp((int)((float)v + this.amount * (float)(r - v)));
            g = PixelUtils.clamp((int)((float)v + this.amount * (float)(g - v)));
            b = PixelUtils.clamp((int)((float)v + this.amount * (float)(b - v)));
            return a | r << 16 | g << 8 | b;
        }
        return rgb;
    }

    public String toString() {
        return "Colors/Saturation...";
    }
}

