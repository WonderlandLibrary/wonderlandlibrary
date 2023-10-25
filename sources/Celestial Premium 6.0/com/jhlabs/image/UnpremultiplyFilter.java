/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.PointFilter;

public class UnpremultiplyFilter
extends PointFilter {
    @Override
    public int filterRGB(int x, int y, int rgb) {
        int a = rgb >> 24 & 0xFF;
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        if (a == 0 || a == 255) {
            return rgb;
        }
        float f = 255.0f / (float)a;
        r = (int)((float)r * f);
        g = (int)((float)g * f);
        b = (int)((float)b * f);
        if (r > 255) {
            r = 255;
        }
        if (g > 255) {
            g = 255;
        }
        if (b > 255) {
            b = 255;
        }
        return a << 24 | r << 16 | g << 8 | b;
    }

    public String toString() {
        return "Alpha/Unpremultiply";
    }
}

