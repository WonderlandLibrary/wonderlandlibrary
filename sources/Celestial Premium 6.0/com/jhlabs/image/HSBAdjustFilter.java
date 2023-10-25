/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.PointFilter;
import java.awt.Color;

public class HSBAdjustFilter
extends PointFilter {
    public float hFactor;
    public float sFactor;
    public float bFactor;
    private float[] hsb = new float[3];

    public HSBAdjustFilter() {
        this(0.0f, 0.0f, 0.0f);
    }

    public HSBAdjustFilter(float r, float g, float b) {
        this.hFactor = r;
        this.sFactor = g;
        this.bFactor = b;
        this.canFilterIndexColorModel = true;
    }

    public void setHFactor(float hFactor) {
        this.hFactor = hFactor;
    }

    public float getHFactor() {
        return this.hFactor;
    }

    public void setSFactor(float sFactor) {
        this.sFactor = sFactor;
    }

    public float getSFactor() {
        return this.sFactor;
    }

    public void setBFactor(float bFactor) {
        this.bFactor = bFactor;
    }

    public float getBFactor() {
        return this.bFactor;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        int a = rgb & 0xFF000000;
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        Color.RGBtoHSB(r, g, b, this.hsb);
        this.hsb[0] = this.hsb[0] + this.hFactor;
        while (this.hsb[0] < 0.0f) {
            this.hsb[0] = (float)((double)this.hsb[0] + Math.PI * 2);
        }
        this.hsb[1] = this.hsb[1] + this.sFactor;
        if (this.hsb[1] < 0.0f) {
            this.hsb[1] = 0.0f;
        } else if ((double)this.hsb[1] > 1.0) {
            this.hsb[1] = 1.0f;
        }
        this.hsb[2] = this.hsb[2] + this.bFactor;
        if (this.hsb[2] < 0.0f) {
            this.hsb[2] = 0.0f;
        } else if ((double)this.hsb[2] > 1.0) {
            this.hsb[2] = 1.0f;
        }
        rgb = Color.HSBtoRGB(this.hsb[0], this.hsb[1], this.hsb[2]);
        return a | rgb & 0xFFFFFF;
    }

    public String toString() {
        return "Colors/Adjust HSB...";
    }
}

