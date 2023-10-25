/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.Colormap;
import com.jhlabs.image.Gradient;
import com.jhlabs.image.PointFilter;

public class LookupFilter
extends PointFilter {
    private Colormap colormap = new Gradient();

    public LookupFilter() {
        this.canFilterIndexColorModel = true;
    }

    public LookupFilter(Colormap colormap) {
        this.canFilterIndexColorModel = true;
        this.colormap = colormap;
    }

    public void setColormap(Colormap colormap) {
        this.colormap = colormap;
    }

    public Colormap getColormap() {
        return this.colormap;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        rgb = (r + g + b) / 3;
        return this.colormap.getColor((float)rgb / 255.0f);
    }

    public String toString() {
        return "Colors/Lookup...";
    }
}

