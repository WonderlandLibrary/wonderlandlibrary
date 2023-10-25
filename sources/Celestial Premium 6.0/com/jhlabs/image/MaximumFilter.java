/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.PixelUtils;
import com.jhlabs.image.WholeImageFilter;
import java.awt.Rectangle;

public class MaximumFilter
extends WholeImageFilter {
    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        int index = 0;
        int[] outPixels = new int[width * height];
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int pixel = -16777216;
                for (int dy = -1; dy <= 1; ++dy) {
                    int iy = y + dy;
                    if (iy < 0 || iy >= height) continue;
                    int ioffset = iy * width;
                    for (int dx = -1; dx <= 1; ++dx) {
                        int ix = x + dx;
                        if (ix < 0 || ix >= width) continue;
                        pixel = PixelUtils.combinePixels(pixel, inPixels[ioffset + ix], 3);
                    }
                }
                outPixels[index++] = pixel;
            }
        }
        return outPixels;
    }

    public String toString() {
        return "Blur/Maximum";
    }
}

