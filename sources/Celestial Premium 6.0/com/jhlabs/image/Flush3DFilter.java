/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.WholeImageFilter;
import java.awt.Rectangle;

public class Flush3DFilter
extends WholeImageFilter {
    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        int index = 0;
        int[] outPixels = new int[width * height];
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int pixel = inPixels[y * width + x];
                if (pixel != -16777216 && y > 0 && x > 0) {
                    int count = 0;
                    if (inPixels[y * width + x - 1] == -16777216) {
                        ++count;
                    }
                    if (inPixels[(y - 1) * width + x] == -16777216) {
                        ++count;
                    }
                    if (inPixels[(y - 1) * width + x - 1] == -16777216) {
                        ++count;
                    }
                    if (count >= 2) {
                        pixel = -1;
                    }
                }
                outPixels[index++] = pixel;
            }
        }
        return outPixels;
    }

    public String toString() {
        return "Stylize/Flush 3D...";
    }
}

