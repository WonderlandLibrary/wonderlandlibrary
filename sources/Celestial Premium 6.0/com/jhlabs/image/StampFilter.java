/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.GaussianFilter;
import com.jhlabs.image.ImageMath;
import com.jhlabs.image.PointFilter;
import java.awt.image.BufferedImage;

public class StampFilter
extends PointFilter {
    private float threshold;
    private float softness = 0.0f;
    private float radius = 5.0f;
    private float lowerThreshold3;
    private float upperThreshold3;
    private int white = -1;
    private int black = -16777216;

    public StampFilter() {
        this(0.5f);
    }

    public StampFilter(float threshold) {
        this.setThreshold(threshold);
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return this.radius;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    public float getThreshold() {
        return this.threshold;
    }

    public void setSoftness(float softness) {
        this.softness = softness;
    }

    public float getSoftness() {
        return this.softness;
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
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        dst = new GaussianFilter((int)this.radius).filter(src, null);
        this.lowerThreshold3 = 765.0f * (this.threshold - this.softness * 0.5f);
        this.upperThreshold3 = 765.0f * (this.threshold + this.softness * 0.5f);
        return super.filter(dst, dst);
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        int a = rgb & 0xFF000000;
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        int l = r + g + b;
        float f = ImageMath.smoothStep(this.lowerThreshold3, this.upperThreshold3, l);
        return ImageMath.mixColors(f, this.black, this.white);
    }

    public String toString() {
        return "Stylize/Stamp...";
    }
}

