/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import com.jhlabs.image.ImageMath;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class ColorHalftoneFilter
extends AbstractBufferedImageOp {
    private float dotRadius = 2.0f;
    private float cyanScreenAngle = (float)Math.toRadians(108.0);
    private float magentaScreenAngle = (float)Math.toRadians(162.0);
    private float yellowScreenAngle = (float)Math.toRadians(90.0);

    public void setdotRadius(float dotRadius) {
        this.dotRadius = dotRadius;
    }

    public float getdotRadius() {
        return this.dotRadius;
    }

    public float getCyanScreenAngle() {
        return this.cyanScreenAngle;
    }

    public void setCyanScreenAngle(float cyanScreenAngle) {
        this.cyanScreenAngle = cyanScreenAngle;
    }

    public float getMagentaScreenAngle() {
        return this.magentaScreenAngle;
    }

    public void setMagentaScreenAngle(float magentaScreenAngle) {
        this.magentaScreenAngle = magentaScreenAngle;
    }

    public float getYellowScreenAngle() {
        return this.yellowScreenAngle;
    }

    public void setYellowScreenAngle(float yellowScreenAngle) {
        this.yellowScreenAngle = yellowScreenAngle;
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
        float gridSize = 2.0f * this.dotRadius * 1.414f;
        float[] angles = new float[]{this.cyanScreenAngle, this.magentaScreenAngle, this.yellowScreenAngle};
        float[] mx = new float[]{0.0f, -1.0f, 1.0f, 0.0f, 0.0f};
        float[] my = new float[]{0.0f, 0.0f, 0.0f, -1.0f, 1.0f};
        float halfGridSize = gridSize / 2.0f;
        int[] outPixels = new int[width];
        int[] inPixels = this.getRGB(src, 0, 0, width, height, null);
        for (int y = 0; y < height; ++y) {
            int x = 0;
            int ix = y * width;
            while (x < width) {
                outPixels[x] = inPixels[ix] & 0xFF000000 | 0xFFFFFF;
                ++x;
                ++ix;
            }
            for (int channel = 0; channel < 3; ++channel) {
                int shift = 16 - 8 * channel;
                int mask = 255 << shift;
                float angle = angles[channel];
                float sin = (float)Math.sin(angle);
                float cos = (float)Math.cos(angle);
                int x2 = 0;
                while (x2 < width) {
                    float tx = (float)x2 * cos + (float)y * sin;
                    float ty = (float)(-x2) * sin + (float)y * cos;
                    tx = tx - ImageMath.mod(tx - halfGridSize, gridSize) + halfGridSize;
                    ty = ty - ImageMath.mod(ty - halfGridSize, gridSize) + halfGridSize;
                    float f = 1.0f;
                    for (int i = 0; i < 5; ++i) {
                        float ttx = tx + mx[i] * gridSize;
                        float tty = ty + my[i] * gridSize;
                        float ntx = ttx * cos - tty * sin;
                        float nty = ttx * sin + tty * cos;
                        int nx = ImageMath.clamp((int)ntx, 0, width - 1);
                        int ny = ImageMath.clamp((int)nty, 0, height - 1);
                        int argb = inPixels[ny * width + nx];
                        int nr = argb >> shift & 0xFF;
                        float l = (float)nr / 255.0f;
                        l = 1.0f - l * l;
                        l = (float)((double)l * ((double)halfGridSize * 1.414));
                        float dx = (float)x2 - ntx;
                        float dy = (float)y - nty;
                        float dx2 = dx * dx;
                        float dy2 = dy * dy;
                        float R = (float)Math.sqrt(dx2 + dy2);
                        float f2 = 1.0f - ImageMath.smoothStep(R, R + 1.0f, l);
                        f = Math.min(f, f2);
                    }
                    int v = (int)(255.0f * f);
                    v <<= shift;
                    v ^= ~mask;
                    int n = x2++;
                    outPixels[n] = outPixels[n] & (v |= 0xFF000000);
                }
            }
            this.setRGB(dst, 0, y, width, 1, outPixels);
        }
        return dst;
    }

    public String toString() {
        return "Pixellate/Color Halftone...";
    }
}

