/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.Histogram;
import com.jhlabs.image.WholeImageFilter;
import java.awt.Rectangle;

public class EqualizeFilter
extends WholeImageFilter {
    private int[][] lut;

    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        int i;
        Histogram histogram = new Histogram(inPixels, width, height, 0, width);
        if (histogram.getNumSamples() > 0) {
            float scale = 255.0f / (float)histogram.getNumSamples();
            this.lut = new int[3][256];
            for (i = 0; i < 3; ++i) {
                int j;
                this.lut[i][0] = histogram.getFrequency(i, 0);
                for (j = 1; j < 256; ++j) {
                    this.lut[i][j] = this.lut[i][j - 1] + histogram.getFrequency(i, j);
                }
                for (j = 0; j < 256; ++j) {
                    this.lut[i][j] = Math.round((float)this.lut[i][j] * scale);
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

    private int filterRGB(int x, int y, int rgb) {
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
        return "Colors/Equalize";
    }
}

