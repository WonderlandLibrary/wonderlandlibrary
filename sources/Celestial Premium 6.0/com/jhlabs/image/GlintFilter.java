/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import com.jhlabs.image.Colormap;
import com.jhlabs.image.GaussianFilter;
import com.jhlabs.image.LinearColormap;
import com.jhlabs.image.PixelUtils;
import java.awt.image.BufferedImage;

public class GlintFilter
extends AbstractBufferedImageOp {
    private float threshold = 1.0f;
    private int length = 5;
    private float blur = 0.0f;
    private float amount = 0.1f;
    private boolean glintOnly = false;
    private Colormap colormap = new LinearColormap(-1, -16777216);

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    public float getThreshold() {
        return this.threshold;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return this.length;
    }

    public void setBlur(float blur) {
        this.blur = blur;
    }

    public float getBlur() {
        return this.blur;
    }

    public void setGlintOnly(boolean glintOnly) {
        this.glintOnly = glintOnly;
    }

    public boolean getGlintOnly() {
        return this.glintOnly;
    }

    public void setColormap(Colormap colormap) {
        this.colormap = colormap;
    }

    public Colormap getColormap() {
        return this.colormap;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width];
        int length2 = (int)((float)this.length / 1.414f);
        int[] colors = new int[this.length + 1];
        int[] colors2 = new int[length2 + 1];
        if (this.colormap != null) {
            int b;
            int g;
            int r;
            int argb;
            int i;
            for (i = 0; i <= this.length; ++i) {
                argb = this.colormap.getColor((float)i / (float)this.length);
                r = argb >> 16 & 0xFF;
                g = argb >> 8 & 0xFF;
                b = argb & 0xFF;
                colors[i] = argb = argb & 0xFF000000 | (int)(this.amount * (float)r) << 16 | (int)(this.amount * (float)g) << 8 | (int)(this.amount * (float)b);
            }
            for (i = 0; i <= length2; ++i) {
                argb = this.colormap.getColor((float)i / (float)length2);
                r = argb >> 16 & 0xFF;
                g = argb >> 8 & 0xFF;
                b = argb & 0xFF;
                colors2[i] = argb = argb & 0xFF000000 | (int)(this.amount * (float)r) << 16 | (int)(this.amount * (float)g) << 8 | (int)(this.amount * (float)b);
            }
        }
        BufferedImage mask = new BufferedImage(width, height, 2);
        int threshold3 = (int)(this.threshold * 3.0f * 255.0f);
        for (int y = 0; y < height; ++y) {
            this.getRGB(src, 0, y, width, 1, pixels);
            for (int x = 0; x < width; ++x) {
                int rgb = pixels[x];
                int a = rgb & 0xFF000000;
                int r = rgb >> 16 & 0xFF;
                int g = rgb >> 8 & 0xFF;
                int b = rgb & 0xFF;
                int l = r + g + b;
                pixels[x] = l < threshold3 ? -16777216 : a | (l /= 3) << 16 | l << 8 | l;
            }
            this.setRGB(mask, 0, y, width, 1, pixels);
        }
        if (this.blur != 0.0f) {
            mask = new GaussianFilter(this.blur).filter(mask, null);
        }
        if (dst == null) {
            dst = this.createCompatibleDestImage(src, null);
        }
        int[] dstPixels = this.glintOnly ? new int[width * height] : this.getRGB(src, 0, 0, width, height, null);
        for (int y = 0; y < height; ++y) {
            int index = y * width;
            this.getRGB(mask, 0, y, width, 1, pixels);
            int ymin = Math.max(y - this.length, 0) - y;
            int ymax = Math.min(y + this.length, height - 1) - y;
            int ymin2 = Math.max(y - length2, 0) - y;
            int ymax2 = Math.min(y + length2, height - 1) - y;
            for (int x = 0; x < width; ++x) {
                if ((float)(pixels[x] & 0xFF) > this.threshold * 255.0f) {
                    int xmin = Math.max(x - this.length, 0) - x;
                    int xmax = Math.min(x + this.length, width - 1) - x;
                    int xmin2 = Math.max(x - length2, 0) - x;
                    int xmax2 = Math.min(x + length2, width - 1) - x;
                    int i = 0;
                    int k = 0;
                    while (i <= xmax) {
                        dstPixels[index + i] = PixelUtils.combinePixels(dstPixels[index + i], colors[k], 4);
                        ++i;
                        ++k;
                    }
                    i = -1;
                    k = 1;
                    while (i >= xmin) {
                        dstPixels[index + i] = PixelUtils.combinePixels(dstPixels[index + i], colors[k], 4);
                        --i;
                        ++k;
                    }
                    i = 1;
                    int j = index + width;
                    int k2 = 0;
                    while (i <= ymax) {
                        dstPixels[j] = PixelUtils.combinePixels(dstPixels[j], colors[k2], 4);
                        ++i;
                        j += width;
                        ++k2;
                    }
                    i = -1;
                    j = index - width;
                    k2 = 0;
                    while (i >= ymin) {
                        dstPixels[j] = PixelUtils.combinePixels(dstPixels[j], colors[k2], 4);
                        --i;
                        j -= width;
                        ++k2;
                    }
                    int xymin = Math.max(xmin2, ymin2);
                    int xymax = Math.min(xmax2, ymax2);
                    int count = Math.min(xmax2, ymax2);
                    int i2 = 1;
                    int j2 = index + width + 1;
                    int k3 = 0;
                    while (i2 <= count) {
                        dstPixels[j2] = PixelUtils.combinePixels(dstPixels[j2], colors2[k3], 4);
                        ++i2;
                        j2 += width + 1;
                        ++k3;
                    }
                    count = Math.min(-xmin2, -ymin2);
                    i2 = 1;
                    j2 = index - width - 1;
                    k3 = 0;
                    while (i2 <= count) {
                        dstPixels[j2] = PixelUtils.combinePixels(dstPixels[j2], colors2[k3], 4);
                        ++i2;
                        j2 -= width + 1;
                        ++k3;
                    }
                    count = Math.min(xmax2, -ymin2);
                    i2 = 1;
                    j2 = index - width + 1;
                    k3 = 0;
                    while (i2 <= count) {
                        dstPixels[j2] = PixelUtils.combinePixels(dstPixels[j2], colors2[k3], 4);
                        ++i2;
                        j2 += -width + 1;
                        ++k3;
                    }
                    count = Math.min(-xmin2, ymax2);
                    i2 = 1;
                    j2 = index + width - 1;
                    k3 = 0;
                    while (i2 <= count) {
                        dstPixels[j2] = PixelUtils.combinePixels(dstPixels[j2], colors2[k3], 4);
                        ++i2;
                        j2 += width - 1;
                        ++k3;
                    }
                }
                ++index;
            }
        }
        this.setRGB(dst, 0, 0, width, height, dstPixels);
        return dst;
    }

    public String toString() {
        return "Effects/Glint...";
    }
}

