/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.PointFilter;

public class GrayscaleFilter
extends PointFilter {
    public GrayscaleFilter() {
        this.canFilterIndexColorModel = true;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        int a = rgb & 0xFF000000;
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        rgb = r * 77 + g * 151 + b * 28 >> 8;
        return a | rgb << 16 | rgb << 8 | rgb;
    }

    public String toString() {
        return "Colors/Grayscale";
    }
}

