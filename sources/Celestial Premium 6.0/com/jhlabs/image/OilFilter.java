/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.WholeImageFilter;
import java.awt.Rectangle;

public class OilFilter
extends WholeImageFilter {
    private int range = 3;
    private int levels = 256;

    public void setRange(int range) {
        this.range = range;
    }

    public int getRange() {
        return this.range;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public int getLevels() {
        return this.levels;
    }

    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        int index = 0;
        int[] rHistogram = new int[this.levels];
        int[] gHistogram = new int[this.levels];
        int[] bHistogram = new int[this.levels];
        int[] rTotal = new int[this.levels];
        int[] gTotal = new int[this.levels];
        int[] bTotal = new int[this.levels];
        int[] outPixels = new int[width * height];
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                for (int i = 0; i < this.levels; ++i) {
                    bTotal[i] = 0;
                    gTotal[i] = 0;
                    rTotal[i] = 0;
                    bHistogram[i] = 0;
                    gHistogram[i] = 0;
                    rHistogram[i] = 0;
                }
                for (int row = -this.range; row <= this.range; ++row) {
                    int iy = y + row;
                    if (iy < 0 || iy >= height) continue;
                    int ioffset = iy * width;
                    for (int col = -this.range; col <= this.range; ++col) {
                        int ix = x + col;
                        if (ix < 0 || ix >= width) continue;
                        int rgb = inPixels[ioffset + ix];
                        int r = rgb >> 16 & 0xFF;
                        int g = rgb >> 8 & 0xFF;
                        int b = rgb & 0xFF;
                        int ri = r * this.levels / 256;
                        int gi = g * this.levels / 256;
                        int bi = b * this.levels / 256;
                        int n = ri;
                        rTotal[n] = rTotal[n] + r;
                        int n2 = gi;
                        gTotal[n2] = gTotal[n2] + g;
                        int n3 = bi;
                        bTotal[n3] = bTotal[n3] + b;
                        int n4 = ri;
                        rHistogram[n4] = rHistogram[n4] + 1;
                        int n5 = gi;
                        gHistogram[n5] = gHistogram[n5] + 1;
                        int n6 = bi;
                        bHistogram[n6] = bHistogram[n6] + 1;
                    }
                }
                int r = 0;
                int g = 0;
                int b = 0;
                for (int i = 1; i < this.levels; ++i) {
                    if (rHistogram[i] > rHistogram[r]) {
                        r = i;
                    }
                    if (gHistogram[i] > gHistogram[g]) {
                        g = i;
                    }
                    if (bHistogram[i] <= bHistogram[b]) continue;
                    b = i;
                }
                r = rTotal[r] / rHistogram[r];
                g = gTotal[g] / gHistogram[g];
                b = bTotal[b] / bHistogram[b];
                outPixels[index] = inPixels[index] & 0xFF000000 | r << 16 | g << 8 | b;
                ++index;
            }
        }
        return outPixels;
    }

    public String toString() {
        return "Stylize/Oil...";
    }
}

