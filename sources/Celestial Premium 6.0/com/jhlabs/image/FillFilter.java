/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.PointFilter;

public class FillFilter
extends PointFilter {
    private int fillColor;

    public FillFilter() {
        this(-16777216);
    }

    public FillFilter(int color) {
        this.fillColor = color;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public int getFillColor() {
        return this.fillColor;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        return this.fillColor;
    }
}

