/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ConvolveFilter;
import com.jhlabs.image.PixelUtils;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;

public class GaussianFilter
extends ConvolveFilter {
    protected float radius;
    protected Kernel kernel;

    public GaussianFilter() {
        this(2.0f);
    }

    public GaussianFilter(float radius) {
        this.setRadius(radius);
    }

    public void setRadius(float radius) {
        this.radius = radius;
        this.kernel = GaussianFilter.makeKernel(radius);
    }

    public float getRadius() {
        return this.radius;
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
        src.getRGB(0, 0, width, height, inPixels, 0, width);
        if (this.radius > 0.0f) {
            GaussianFilter.convolveAndTranspose(this.kernel, inPixels, outPixels, width, height, this.alpha, this.alpha && this.premultiplyAlpha, false, CLAMP_EDGES);
            GaussianFilter.convolveAndTranspose(this.kernel, outPixels, inPixels, height, width, this.alpha, false, this.alpha && this.premultiplyAlpha, CLAMP_EDGES);
        }
        dst.setRGB(0, 0, width, height, inPixels, 0, width);
        return dst;
    }

    public static void convolveAndTranspose(Kernel kernel, int[] inPixels, int[] outPixels, int width, int height, boolean alpha, boolean premultiply, boolean unpremultiply, int edgeAction) {
        float[] matrix = kernel.getKernelData(null);
        int cols = kernel.getWidth();
        int cols2 = cols / 2;
        for (int y = 0; y < height; ++y) {
            int index = y;
            int ioffset = y * width;
            for (int x = 0; x < width; ++x) {
                float r = 0.0f;
                float g = 0.0f;
                float b = 0.0f;
                float a = 0.0f;
                int moffset = cols2;
                for (int col = -cols2; col <= cols2; ++col) {
                    float f = matrix[moffset + col];
                    if (f == 0.0f) continue;
                    int ix = x + col;
                    if (ix < 0) {
                        if (edgeAction == CLAMP_EDGES) {
                            ix = 0;
                        } else if (edgeAction == WRAP_EDGES) {
                            ix = (x + width) % width;
                        }
                    } else if (ix >= width) {
                        if (edgeAction == CLAMP_EDGES) {
                            ix = width - 1;
                        } else if (edgeAction == WRAP_EDGES) {
                            ix = (x + width) % width;
                        }
                    }
                    int rgb = inPixels[ioffset + ix];
                    int pa = rgb >> 24 & 0xFF;
                    int pr = rgb >> 16 & 0xFF;
                    int pg = rgb >> 8 & 0xFF;
                    int pb = rgb & 0xFF;
                    if (premultiply) {
                        float a255 = (float)pa * 0.003921569f;
                        pr = (int)((float)pr * a255);
                        pg = (int)((float)pg * a255);
                        pb = (int)((float)pb * a255);
                    }
                    a += f * (float)pa;
                    r += f * (float)pr;
                    g += f * (float)pg;
                    b += f * (float)pb;
                }
                if (unpremultiply && a != 0.0f && a != 255.0f) {
                    float f = 255.0f / a;
                    r *= f;
                    g *= f;
                    b *= f;
                }
                int ia = alpha ? PixelUtils.clamp((int)((double)a + 0.5)) : 255;
                int ir = PixelUtils.clamp((int)((double)r + 0.5));
                int ig = PixelUtils.clamp((int)((double)g + 0.5));
                int ib = PixelUtils.clamp((int)((double)b + 0.5));
                outPixels[index] = ia << 24 | ir << 16 | ig << 8 | ib;
                index += height;
            }
        }
    }

    public static Kernel makeKernel(float radius) {
        int r = (int)Math.ceil(radius);
        int rows = r * 2 + 1;
        float[] matrix = new float[rows];
        float sigma = radius / 3.0f;
        float sigma22 = 2.0f * sigma * sigma;
        float sigmaPi2 = (float)Math.PI * 2 * sigma;
        float sqrtSigmaPi2 = (float)Math.sqrt(sigmaPi2);
        float radius2 = radius * radius;
        float total = 0.0f;
        int index = 0;
        for (int row = -r; row <= r; ++row) {
            float distance = row * row;
            matrix[index] = distance > radius2 ? 0.0f : (float)Math.exp(-distance / sigma22) / sqrtSigmaPi2;
            total += matrix[index];
            ++index;
        }
        int i = 0;
        while (i < rows) {
            int n = i++;
            matrix[n] = matrix[n] / total;
        }
        return new Kernel(rows, 1, matrix);
    }

    @Override
    public String toString() {
        return "Blur/Gaussian Blur...";
    }
}

