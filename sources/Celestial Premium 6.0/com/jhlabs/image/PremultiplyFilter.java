/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.PointFilter;

public class PremultiplyFilter
extends PointFilter {
    @Override
    public int filterRGB(int x, int y, int rgb) {
        int a = rgb >> 24 & 0xFF;
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        float f = (float)a * 0.003921569f;
        r = (int)((float)r * f);
        g = (int)((float)g * f);
        b = (int)((float)b * f);
        return a << 24 | r << 16 | g << 8 | b;
    }

    public String toString() {
        return "Alpha/Premultiply";
    }
}

