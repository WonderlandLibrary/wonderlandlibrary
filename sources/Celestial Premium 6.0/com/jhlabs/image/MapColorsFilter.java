/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.PointFilter;

public class MapColorsFilter
extends PointFilter {
    private int oldColor;
    private int newColor;

    public MapColorsFilter() {
        this(-1, -16777216);
    }

    public MapColorsFilter(int oldColor, int newColor) {
        this.canFilterIndexColorModel = true;
        this.oldColor = oldColor;
        this.newColor = newColor;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        if (rgb == this.oldColor) {
            return this.newColor;
        }
        return rgb;
    }
}

