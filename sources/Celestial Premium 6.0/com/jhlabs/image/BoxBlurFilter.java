/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import com.jhlabs.image.ImageMath;
import java.awt.image.BufferedImage;

public class BoxBlurFilter
extends AbstractBufferedImageOp {
    private float hRadius;
    private float vRadius;
    private int iterations = 1;
    private boolean premultiplyAlpha = true;

    public BoxBlurFilter() {
    }

    public BoxBlurFilter(float hRadius, float vRadius, int iterations) {
        this.hRadius = hRadius;
        this.vRadius = vRadius;
        this.iterations = iterations;
    }

    public void setPremultiplyAlpha(boolean premultiplyAlpha) {
        this.premultiplyAlpha = premultiplyAlpha;
    }

    public boolean getPremultiplyAlpha() {
        return this.premultiplyAlpha;
    }

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
        if (this.premultiplyAlpha) {
            ImageMath.premultiply(inPixels, 0, inPixels.length);
        }
        for (int i = 0; i < this.iterations; ++i) {
            BoxBlurFilter.blur(inPixels, outPixels, width, height, this.hRadius);
            BoxBlurFilter.blur(outPixels, inPixels, height, width, this.vRadius);
        }
        BoxBlurFilter.blurFractional(inPixels, outPixels, width, height, this.hRadius);
        BoxBlurFilter.blurFractional(outPixels, inPixels, height, width, this.vRadius);
        if (this.premultiplyAlpha) {
            ImageMath.unpremultiply(inPixels, 0, inPixels.length);
        }
        this.setRGB(dst, 0, 0, width, height, inPixels);
        return dst;
    }

    public static void blur(int[] in, int[] out, int width, int height, float radius) {
        int widthMinus1 = width - 1;
        int r = (int)radius;
        int tableSize = 2 * r + 1;
        int[] divide = new int[256 * tableSize];
        for (int i = 0; i < 256 * tableSize; ++i) {
            divide[i] = i / tableSize;
        }
        int inIndex = 0;
        for (int y = 0; y < height; ++y) {
            int outIndex = y;
            int ta = 0;
            int tr = 0;
            int tg = 0;
            int tb = 0;
            for (int i = -r; i <= r; ++i) {
                int rgb = in[inIndex + ImageMath.clamp(i, 0, width - 1)];
                ta += rgb >> 24 & 0xFF;
                tr += rgb >> 16 & 0xFF;
                tg += rgb >> 8 & 0xFF;
                tb += rgb & 0xFF;
            }
            for (int x = 0; x < width; ++x) {
                int i2;
                out[outIndex] = divide[ta] << 24 | divide[tr] << 16 | divide[tg] << 8 | divide[tb];
                int i1 = x + r + 1;
                if (i1 > widthMinus1) {
                    i1 = widthMinus1;
                }
                if ((i2 = x - r) < 0) {
                    i2 = 0;
                }
                int rgb1 = in[inIndex + i1];
                int rgb2 = in[inIndex + i2];
                ta += (rgb1 >> 24 & 0xFF) - (rgb2 >> 24 & 0xFF);
                tr += (rgb1 & 0xFF0000) - (rgb2 & 0xFF0000) >> 16;
                tg += (rgb1 & 0xFF00) - (rgb2 & 0xFF00) >> 8;
                tb += (rgb1 & 0xFF) - (rgb2 & 0xFF);
                outIndex += height;
            }
            inIndex += width;
        }
    }

    public static void blurFractional(int[] in, int[] out, int width, int height, float radius) {
        radius -= (float)((int)radius);
        float f = 1.0f / (1.0f + 2.0f * radius);
        int inIndex = 0;
        for (int y = 0; y < height; ++y) {
            int outIndex = y;
            out[outIndex] = in[0];
            outIndex += height;
            for (int x = 1; x < width - 1; ++x) {
                int i = inIndex + x;
                int rgb1 = in[i - 1];
                int rgb2 = in[i];
                int rgb3 = in[i + 1];
                int a1 = rgb1 >> 24 & 0xFF;
                int r1 = rgb1 >> 16 & 0xFF;
                int g1 = rgb1 >> 8 & 0xFF;
                int b1 = rgb1 & 0xFF;
                int a2 = rgb2 >> 24 & 0xFF;
                int r2 = rgb2 >> 16 & 0xFF;
                int g2 = rgb2 >> 8 & 0xFF;
                int b2 = rgb2 & 0xFF;
                int a3 = rgb3 >> 24 & 0xFF;
                int r3 = rgb3 >> 16 & 0xFF;
                int g3 = rgb3 >> 8 & 0xFF;
                int b3 = rgb3 & 0xFF;
                a1 = a2 + (int)((float)(a1 + a3) * radius);
                r1 = r2 + (int)((float)(r1 + r3) * radius);
                g1 = g2 + (int)((float)(g1 + g3) * radius);
                b1 = b2 + (int)((float)(b1 + b3) * radius);
                a1 = (int)((float)a1 * f);
                r1 = (int)((float)r1 * f);
                g1 = (int)((float)g1 * f);
                b1 = (int)((float)b1 * f);
                out[outIndex] = a1 << 24 | r1 << 16 | g1 << 8 | b1;
                outIndex += height;
            }
            out[outIndex] = in[width - 1];
            inIndex += width;
        }
    }

    public void setHRadius(float hRadius) {
        this.hRadius = hRadius;
    }

    public float getHRadius() {
        return this.hRadius;
    }

    public void setVRadius(float vRadius) {
        this.vRadius = vRadius;
    }

    public float getVRadius() {
        return this.vRadius;
    }

    public void setRadius(float radius) {
        this.hRadius = this.vRadius = radius;
    }

    public float getRadius() {
        return this.hRadius;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getIterations() {
        return this.iterations;
    }

    public String toString() {
        return "Blur/Box Blur...";
    }
}

