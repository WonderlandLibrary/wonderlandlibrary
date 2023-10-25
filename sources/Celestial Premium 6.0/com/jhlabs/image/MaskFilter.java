/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.PointFilter;

public class MaskFilter
extends PointFilter {
    private int mask;

    public MaskFilter() {
        this(-16711681);
    }

    public MaskFilter(int mask) {
        this.canFilterIndexColorModel = true;
        this.setMask(mask);
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public int getMask() {
        return this.mask;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        return rgb & this.mask;
    }

    public String toString() {
        return "Mask";
    }
}

