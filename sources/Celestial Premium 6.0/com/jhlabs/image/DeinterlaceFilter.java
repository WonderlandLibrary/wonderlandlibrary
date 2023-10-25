/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import java.awt.image.BufferedImage;

public class DeinterlaceFilter
extends AbstractBufferedImageOp {
    public static final int EVEN = 0;
    public static final int ODD = 1;
    public static final int AVERAGE = 2;
    private int mode = 0;

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return this.mode;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        block8: {
            int[] pixels;
            int height;
            int width;
            block9: {
                block7: {
                    width = src.getWidth();
                    height = src.getHeight();
                    if (dst == null) {
                        dst = this.createCompatibleDestImage(src, null);
                    }
                    pixels = null;
                    if (this.mode != 0) break block7;
                    for (int y = 0; y < height - 1; y += 2) {
                        pixels = this.getRGB(src, 0, y, width, 1, pixels);
                        if (src != dst) {
                            this.setRGB(dst, 0, y, width, 1, pixels);
                        }
                        this.setRGB(dst, 0, y + 1, width, 1, pixels);
                    }
                    break block8;
                }
                if (this.mode != 1) break block9;
                for (int y = 1; y < height; y += 2) {
                    pixels = this.getRGB(src, 0, y, width, 1, pixels);
                    if (src != dst) {
                        this.setRGB(dst, 0, y, width, 1, pixels);
                    }
                    this.setRGB(dst, 0, y - 1, width, 1, pixels);
                }
                break block8;
            }
            if (this.mode != 2) break block8;
            int[] pixels2 = null;
            for (int y = 0; y < height - 1; y += 2) {
                pixels = this.getRGB(src, 0, y, width, 1, pixels);
                pixels2 = this.getRGB(src, 0, y + 1, width, 1, pixels2);
                for (int x = 0; x < width; ++x) {
                    int rgb1 = pixels[x];
                    int rgb2 = pixels2[x];
                    int a1 = rgb1 >> 24 & 0xFF;
                    int r1 = rgb1 >> 16 & 0xFF;
                    int g1 = rgb1 >> 8 & 0xFF;
                    int b1 = rgb1 & 0xFF;
                    int a2 = rgb2 >> 24 & 0xFF;
                    int r2 = rgb2 >> 16 & 0xFF;
                    int g2 = rgb2 >> 8 & 0xFF;
                    int b2 = rgb2 & 0xFF;
                    a1 = (a1 + a2) / 2;
                    r1 = (r1 + r2) / 2;
                    g1 = (g1 + g2) / 2;
                    b1 = (b1 + b2) / 2;
                    pixels[x] = a1 << 24 | r1 << 16 | g1 << 8 | b1;
                }
                this.setRGB(dst, 0, y, width, 1, pixels);
                this.setRGB(dst, 0, y + 1, width, 1, pixels);
            }
        }
        return dst;
    }

    public String toString() {
        return "Video/De-Interlace";
    }
}

