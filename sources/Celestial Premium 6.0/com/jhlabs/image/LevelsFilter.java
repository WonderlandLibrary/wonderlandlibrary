/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.Histogram;
import com.jhlabs.image.PixelUtils;
import com.jhlabs.image.WholeImageFilter;
import java.awt.Rectangle;

public class LevelsFilter
extends WholeImageFilter {
    private int[][] lut;
    private float lowLevel = 0.0f;
    private float highLevel = 1.0f;
    private float lowOutputLevel = 0.0f;
    private float highOutputLevel = 1.0f;

    public void setLowLevel(float lowLevel) {
        this.lowLevel = lowLevel;
    }

    public float getLowLevel() {
        return this.lowLevel;
    }

    public void setHighLevel(float highLevel) {
        this.highLevel = highLevel;
    }

    public float getHighLevel() {
        return this.highLevel;
    }

    public void setLowOutputLevel(float lowOutputLevel) {
        this.lowOutputLevel = lowOutputLevel;
    }

    public float getLowOutputLevel() {
        return this.lowOutputLevel;
    }

    public void setHighOutputLevel(float highOutputLevel) {
        this.highOutputLevel = highOutputLevel;
    }

    public float getHighOutputLevel() {
        return this.highOutputLevel;
    }

    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        int i;
        Histogram histogram = new Histogram(inPixels, width, height, 0, width);
        if (histogram.getNumSamples() > 0) {
            float scale = 255.0f / (float)histogram.getNumSamples();
            this.lut = new int[3][256];
            float low = this.lowLevel * 255.0f;
            float high = this.highLevel * 255.0f;
            if (low == high) {
                high += 1.0f;
            }
            for (i = 0; i < 3; ++i) {
                for (int j = 0; j < 256; ++j) {
                    this.lut[i][j] = PixelUtils.clamp((int)(255.0f * (this.lowOutputLevel + (this.highOutputLevel - this.lowOutputLevel) * ((float)j - low) / (high - low))));
                }
            }
        } else {
            this.lut = null;
        }
        i = 0;
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                inPixels[i] = this.filterRGB(x, y, inPixels[i]);
                ++i;
            }
        }
        this.lut = null;
        return inPixels;
    }

    public int filterRGB(int x, int y, int rgb) {
        if (this.lut != null) {
            int a = rgb & 0xFF000000;
            int r = this.lut[0][rgb >> 16 & 0xFF];
            int g = this.lut[1][rgb >> 8 & 0xFF];
            int b = this.lut[2][rgb & 0xFF];
            return a | r << 16 | g << 8 | b;
        }
        return rgb;
    }

    public String toString() {
        return "Colors/Levels...";
    }
}

