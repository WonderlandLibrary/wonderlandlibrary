/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.PointFilter;

public class InvertFilter
extends PointFilter {
    public InvertFilter() {
        this.canFilterIndexColorModel = true;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        int a = rgb & 0xFF000000;
        return a | ~rgb & 0xFFFFFF;
    }

    public String toString() {
        return "Colors/Invert";
    }
}

