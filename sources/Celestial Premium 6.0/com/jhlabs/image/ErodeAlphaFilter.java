/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.GaussianFilter;
import com.jhlabs.image.ImageMath;
import com.jhlabs.image.PointFilter;
import java.awt.image.BufferedImage;

public class ErodeAlphaFilter
extends PointFilter {
    private float threshold;
    private float softness = 0.0f;
    protected float radius = 5.0f;
    private float lowerThreshold;
    private float upperThreshold;

    public ErodeAlphaFilter() {
        this(3.0f, 0.75f, 0.0f);
    }

    public ErodeAlphaFilter(float radius, float threshold, float softness) {
        this.radius = radius;
        this.threshold = threshold;
        this.softness = softness;
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

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        dst = new GaussianFilter((int)this.radius).filter(src, null);
        this.lowerThreshold = 255.0f * (this.threshold - this.softness * 0.5f);
        this.upperThreshold = 255.0f * (this.threshold + this.softness * 0.5f);
        return super.filter(dst, dst);
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        int a = rgb >> 24 & 0xFF;
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        if (a == 255) {
            return -1;
        }
        float f = ImageMath.smoothStep(this.lowerThreshold, this.upperThreshold, a);
        if ((a = (int)(f * 255.0f)) < 0) {
            a = 0;
        } else if (a > 255) {
            a = 255;
        }
        return a << 24 | 0xFFFFFF;
    }

    public String toString() {
        return "Alpha/Erode...";
    }
}

