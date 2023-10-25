/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ImageMath;
import com.jhlabs.image.PixelUtils;
import com.jhlabs.image.PointFilter;

public class ThresholdFilter
extends PointFilter {
    private int lowerThreshold;
    private int upperThreshold;
    private int white = 0xFFFFFF;
    private int black = 0;

    public ThresholdFilter() {
        this(127);
    }

    public ThresholdFilter(int t) {
        this.setLowerThreshold(t);
        this.setUpperThreshold(t);
    }

    public void setLowerThreshold(int lowerThreshold) {
        this.lowerThreshold = lowerThreshold;
    }

    public int getLowerThreshold() {
        return this.lowerThreshold;
    }

    public void setUpperThreshold(int upperThreshold) {
        this.upperThreshold = upperThreshold;
    }

    public int getUpperThreshold() {
        return this.upperThreshold;
    }

    public void setWhite(int white) {
        this.white = white;
    }

    public int getWhite() {
        return this.white;
    }

    public void setBlack(int black) {
        this.black = black;
    }

    public int getBlack() {
        return this.black;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        int v = PixelUtils.brightness(rgb);
        float f = ImageMath.smoothStep(this.lowerThreshold, this.upperThreshold, v);
        return rgb & 0xFF000000 | ImageMath.mixColors(f, this.black, this.white) & 0xFFFFFF;
    }

    public String toString() {
        return "Stylize/Threshold...";
    }
}

