/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.PixelUtils;
import com.jhlabs.image.PointFilter;

public class SwizzleFilter
extends PointFilter {
    private int[] matrix;

    public SwizzleFilter() {
        int[] arrn = new int[20];
        arrn[0] = 1;
        arrn[6] = 1;
        arrn[12] = 1;
        arrn[18] = 1;
        this.matrix = arrn;
    }

    public void setMatrix(int[] matrix) {
        this.matrix = matrix;
    }

    public int[] getMatrix() {
        return this.matrix;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        int a = rgb >> 24 & 0xFF;
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        a = this.matrix[0] * a + this.matrix[1] * r + this.matrix[2] * g + this.matrix[3] * b + this.matrix[4] * 255;
        r = this.matrix[5] * a + this.matrix[6] * r + this.matrix[7] * g + this.matrix[8] * b + this.matrix[9] * 255;
        g = this.matrix[10] * a + this.matrix[11] * r + this.matrix[12] * g + this.matrix[13] * b + this.matrix[14] * 255;
        b = this.matrix[15] * a + this.matrix[16] * r + this.matrix[17] * g + this.matrix[18] * b + this.matrix[19] * 255;
        a = PixelUtils.clamp(a);
        r = PixelUtils.clamp(r);
        g = PixelUtils.clamp(g);
        b = PixelUtils.clamp(b);
        return a << 24 | r << 16 | g << 8 | b;
    }

    public String toString() {
        return "Channels/Swizzle...";
    }
}

