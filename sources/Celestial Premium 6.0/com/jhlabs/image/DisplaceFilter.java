/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.TransformFilter;
import java.awt.image.BufferedImage;

public class DisplaceFilter
extends TransformFilter {
    private float amount = 1.0f;
    private BufferedImage displacementMap = null;
    private int[] xmap;
    private int[] ymap;
    private int dw;
    private int dh;

    public void setDisplacementMap(BufferedImage displacementMap) {
        this.displacementMap = displacementMap;
    }

    public BufferedImage getDisplacementMap() {
        return this.displacementMap;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return this.amount;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int y;
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage dm = this.displacementMap != null ? this.displacementMap : src;
        this.dw = dm.getWidth();
        this.dh = dm.getHeight();
        int[] mapPixels = new int[this.dw * this.dh];
        this.getRGB(dm, 0, 0, this.dw, this.dh, mapPixels);
        this.xmap = new int[this.dw * this.dh];
        this.ymap = new int[this.dw * this.dh];
        int i = 0;
        for (y = 0; y < this.dh; ++y) {
            for (int x = 0; x < this.dw; ++x) {
                int rgb = mapPixels[i];
                int r = rgb >> 16 & 0xFF;
                int g = rgb >> 8 & 0xFF;
                int b = rgb & 0xFF;
                mapPixels[i] = (r + g + b) / 8;
                ++i;
            }
        }
        i = 0;
        for (y = 0; y < this.dh; ++y) {
            int j1 = (y + this.dh - 1) % this.dh * this.dw;
            int j2 = y * this.dw;
            int j3 = (y + 1) % this.dh * this.dw;
            for (int x = 0; x < this.dw; ++x) {
                int k1 = (x + this.dw - 1) % this.dw;
                int k2 = x;
                int k3 = (x + 1) % this.dw;
                this.xmap[i] = mapPixels[k1 + j1] + mapPixels[k1 + j2] + mapPixels[k1 + j3] - mapPixels[k3 + j1] - mapPixels[k3 + j2] - mapPixels[k3 + j3];
                this.ymap[i] = mapPixels[k1 + j3] + mapPixels[k2 + j3] + mapPixels[k3 + j3] - mapPixels[k1 + j1] - mapPixels[k2 + j1] - mapPixels[k3 + j1];
                ++i;
            }
        }
        mapPixels = null;
        dst = super.filter(src, dst);
        this.ymap = null;
        this.xmap = null;
        return dst;
    }

    @Override
    protected void transformInverse(int x, int y, float[] out) {
        float nx = x;
        float ny = y;
        int i = y % this.dh * this.dw + x % this.dw;
        out[0] = (float)x + this.amount * (float)this.xmap[i];
        out[1] = (float)y + this.amount * (float)this.ymap[i];
    }

    public String toString() {
        return "Distort/Displace...";
    }
}

