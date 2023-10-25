/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import com.jhlabs.image.GaussianFilter;
import com.jhlabs.image.PixelUtils;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;

public class SmartBlurFilter
extends AbstractBufferedImageOp {
    private int hRadius = 5;
    private int vRadius = 5;
    private int threshold = 10;

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        if (dst == null) {
            dst = this.createCompatibleDestImage(src, null);
        }
        int[] inPixels = new int[width * height];
        int[] outPixels = new int[width * height];
        this.getRGB(src, 0, 0, width, height, inPixels);
        Kernel kernel = GaussianFilter.makeKernel(this.hRadius);
        this.thresholdBlur(kernel, inPixels, outPixels, width, height, true);
        this.thresholdBlur(kernel, outPixels, inPixels, height, width, true);
        this.setRGB(dst, 0, 0, width, height, inPixels);
        return dst;
    }

    private void thresholdBlur(Kernel kernel, int[] inPixels, int[] outPixels, int width, int height, boolean alpha) {
        boolean index = false;
        float[] matrix = kernel.getKernelData(null);
        int cols = kernel.getWidth();
        int cols2 = cols / 2;
        for (int y = 0; y < height; ++y) {
            int ioffset = y * width;
            int outIndex = y;
            for (int x = 0; x < width; ++x) {
                float r = 0.0f;
                float g = 0.0f;
                float b = 0.0f;
                float a = 0.0f;
                int moffset = cols2;
                int rgb1 = inPixels[ioffset + x];
                int a1 = rgb1 >> 24 & 0xFF;
                int r1 = rgb1 >> 16 & 0xFF;
                int g1 = rgb1 >> 8 & 0xFF;
                int b1 = rgb1 & 0xFF;
                float af = 0.0f;
                float rf = 0.0f;
                float gf = 0.0f;
                float bf = 0.0f;
                for (int col = -cols2; col <= cols2; ++col) {
                    float f = matrix[moffset + col];
                    if (f == 0.0f) continue;
                    int ix = x + col;
                    if (ix < 0 || ix >= width) {
                        ix = x;
                    }
                    int rgb2 = inPixels[ioffset + ix];
                    int a2 = rgb2 >> 24 & 0xFF;
                    int r2 = rgb2 >> 16 & 0xFF;
                    int g2 = rgb2 >> 8 & 0xFF;
                    int b2 = rgb2 & 0xFF;
                    int d = a1 - a2;
                    if (d >= -this.threshold && d <= this.threshold) {
                        a += f * (float)a2;
                        af += f;
                    }
                    if ((d = r1 - r2) >= -this.threshold && d <= this.threshold) {
                        r += f * (float)r2;
                        rf += f;
                    }
                    if ((d = g1 - g2) >= -this.threshold && d <= this.threshold) {
                        g += f * (float)g2;
                        gf += f;
                    }
                    if ((d = b1 - b2) < -this.threshold || d > this.threshold) continue;
                    b += f * (float)b2;
                    bf += f;
                }
                a = af == 0.0f ? (float)a1 : a / af;
                r = rf == 0.0f ? (float)r1 : r / rf;
                g = gf == 0.0f ? (float)g1 : g / gf;
                b = bf == 0.0f ? (float)b1 : b / bf;
                int ia = alpha ? PixelUtils.clamp((int)((double)a + 0.5)) : 255;
                int ir = PixelUtils.clamp((int)((double)r + 0.5));
                int ig = PixelUtils.clamp((int)((double)g + 0.5));
                int ib = PixelUtils.clamp((int)((double)b + 0.5));
                outPixels[outIndex] = ia << 24 | ir << 16 | ig << 8 | ib;
                outIndex += height;
            }
        }
    }

    public void setHRadius(int hRadius) {
        this.hRadius = hRadius;
    }

    public int getHRadius() {
        return this.hRadius;
    }

    public void setVRadius(int vRadius) {
        this.vRadius = vRadius;
    }

    public int getVRadius() {
        return this.vRadius;
    }

    public void setRadius(int radius) {
        this.hRadius = this.vRadius = radius;
    }

    public int getRadius() {
        return this.hRadius;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getThreshold() {
        return this.threshold;
    }

    public String toString() {
        return "Blur/Smart Blur...";
    }
}

