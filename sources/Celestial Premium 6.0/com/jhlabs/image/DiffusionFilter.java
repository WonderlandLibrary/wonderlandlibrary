/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.PixelUtils;
import com.jhlabs.image.WholeImageFilter;
import java.awt.Rectangle;

public class DiffusionFilter
extends WholeImageFilter {
    private static final int[] diffusionMatrix;
    private int[] matrix;
    private int sum = 16;
    private boolean serpentine = true;
    private boolean colorDither = true;
    private int levels = 6;

    static {
        int[] arrn = new int[9];
        arrn[5] = 7;
        arrn[6] = 3;
        arrn[7] = 5;
        arrn[8] = 1;
        diffusionMatrix = arrn;
    }

    public DiffusionFilter() {
        this.setMatrix(diffusionMatrix);
    }

    public void setSerpentine(boolean serpentine) {
        this.serpentine = serpentine;
    }

    public boolean getSerpentine() {
        return this.serpentine;
    }

    public void setColorDither(boolean colorDither) {
        this.colorDither = colorDither;
    }

    public boolean getColorDither() {
        return this.colorDither;
    }

    public void setMatrix(int[] matrix) {
        this.matrix = matrix;
        this.sum = 0;
        for (int i = 0; i < matrix.length; ++i) {
            this.sum += matrix[i];
        }
    }

    public int[] getMatrix() {
        return this.matrix;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public int getLevels() {
        return this.levels;
    }

    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        int[] outPixels = new int[width * height];
        int index = 0;
        int[] map = new int[this.levels];
        for (int i = 0; i < this.levels; ++i) {
            int v;
            map[i] = v = 255 * i / (this.levels - 1);
        }
        int[] div = new int[256];
        for (int i = 0; i < 256; ++i) {
            div[i] = this.levels * i / 256;
        }
        for (int y = 0; y < height; ++y) {
            int direction;
            boolean reverse;
            boolean bl = reverse = this.serpentine && (y & 1) == 1;
            if (reverse) {
                index = y * width + width - 1;
                direction = -1;
            } else {
                index = y * width;
                direction = 1;
            }
            for (int x = 0; x < width; ++x) {
                int rgb1 = inPixels[index];
                int r1 = rgb1 >> 16 & 0xFF;
                int g1 = rgb1 >> 8 & 0xFF;
                int b1 = rgb1 & 0xFF;
                if (!this.colorDither) {
                    g1 = b1 = (r1 + g1 + b1) / 3;
                    r1 = b1;
                }
                int r2 = map[div[r1]];
                int g2 = map[div[g1]];
                int b2 = map[div[b1]];
                outPixels[index] = rgb1 & 0xFF000000 | r2 << 16 | g2 << 8 | b2;
                int er = r1 - r2;
                int eg = g1 - g2;
                int eb = b1 - b2;
                for (int i = -1; i <= 1; ++i) {
                    int iy = i + y;
                    if (iy < 0 || iy >= height) continue;
                    for (int j = -1; j <= 1; ++j) {
                        int w;
                        int jx = j + x;
                        if (jx < 0 || jx >= width || (w = reverse ? this.matrix[(i + 1) * 3 - j + 1] : this.matrix[(i + 1) * 3 + j + 1]) == 0) continue;
                        int k = reverse ? index - j : index + j;
                        rgb1 = inPixels[k];
                        r1 = rgb1 >> 16 & 0xFF;
                        g1 = rgb1 >> 8 & 0xFF;
                        b1 = rgb1 & 0xFF;
                        inPixels[k] = inPixels[k] & 0xFF000000 | PixelUtils.clamp(r1 += er * w / this.sum) << 16 | PixelUtils.clamp(g1 += eg * w / this.sum) << 8 | PixelUtils.clamp(b1 += eb * w / this.sum);
                    }
                }
                index += direction;
            }
        }
        return outPixels;
    }

    public String toString() {
        return "Colors/Diffusion Dither...";
    }
}

