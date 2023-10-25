/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.PointFilter;

public class OpacityFilter
extends PointFilter {
    private int opacity;
    private int opacity24;

    public OpacityFilter() {
        this(136);
    }

    public OpacityFilter(int opacity) {
        this.setOpacity(opacity);
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
        this.opacity24 = opacity << 24;
    }

    public int getOpacity() {
        return this.opacity;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        if ((rgb & 0xFF000000) != 0) {
            return rgb & 0xFFFFFF | this.opacity24;
        }
        return rgb;
    }

    public String toString() {
        return "Colors/Transparency...";
    }
}

