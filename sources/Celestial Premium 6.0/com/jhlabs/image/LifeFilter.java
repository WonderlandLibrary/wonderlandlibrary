/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.BinaryFilter;
import java.awt.Rectangle;

public class LifeFilter
extends BinaryFilter {
    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        int index = 0;
        int[] outPixels = new int[width * height];
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                boolean r = false;
                boolean g = false;
                boolean b = false;
                int pixel = inPixels[y * width + x];
                int a = pixel & 0xFF000000;
                int neighbours = 0;
                for (int row = -1; row <= 1; ++row) {
                    int iy = y + row;
                    if (iy < 0 || iy >= height) continue;
                    int ioffset = iy * width;
                    for (int col = -1; col <= 1; ++col) {
                        int rgb;
                        int ix = x + col;
                        if (row == 0 && col == 0 || ix < 0 || ix >= width || !this.blackFunction.isBlack(rgb = inPixels[ioffset + ix])) continue;
                        ++neighbours;
                    }
                }
                if (this.blackFunction.isBlack(pixel)) {
                    outPixels[index++] = neighbours == 2 || neighbours == 3 ? pixel : -1;
                    continue;
                }
                outPixels[index++] = neighbours == 3 ? -16777216 : pixel;
            }
        }
        return outPixels;
    }

    public String toString() {
        return "Binary/Life";
    }
}

