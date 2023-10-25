/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class BlockFilter
extends AbstractBufferedImageOp {
    private int blockSize = 2;

    public BlockFilter() {
    }

    public BlockFilter(int blockSize) {
        this.blockSize = blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int getBlockSize() {
        return this.blockSize;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        int type = src.getType();
        WritableRaster srcRaster = src.getRaster();
        if (dst == null) {
            dst = this.createCompatibleDestImage(src, null);
        }
        int[] pixels = new int[this.blockSize * this.blockSize];
        for (int y = 0; y < height; y += this.blockSize) {
            for (int x = 0; x < width; x += this.blockSize) {
                int argb;
                int bx;
                int by;
                int w = Math.min(this.blockSize, width - x);
                int h = Math.min(this.blockSize, height - y);
                int t = w * h;
                this.getRGB(src, x, y, w, h, pixels);
                int r = 0;
                int g = 0;
                int b = 0;
                int i = 0;
                for (by = 0; by < h; ++by) {
                    for (bx = 0; bx < w; ++bx) {
                        argb = pixels[i];
                        r += argb >> 16 & 0xFF;
                        g += argb >> 8 & 0xFF;
                        b += argb & 0xFF;
                        ++i;
                    }
                }
                argb = r / t << 16 | g / t << 8 | b / t;
                i = 0;
                for (by = 0; by < h; ++by) {
                    for (bx = 0; bx < w; ++bx) {
                        pixels[i] = pixels[i] & 0xFF000000 | argb;
                        ++i;
                    }
                }
                this.setRGB(dst, x, y, w, h, pixels);
            }
        }
        return dst;
    }

    public String toString() {
        return "Pixellate/Mosaic...";
    }
}

