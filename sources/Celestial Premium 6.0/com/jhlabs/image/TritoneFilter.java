/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ImageMath;
import com.jhlabs.image.PixelUtils;
import com.jhlabs.image.PointFilter;
import java.awt.image.BufferedImage;

public class TritoneFilter
extends PointFilter {
    private int shadowColor = -16777216;
    private int midColor = -7829368;
    private int highColor = -1;
    private int[] lut;

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        float t;
        int i;
        this.lut = new int[256];
        for (i = 0; i < 128; ++i) {
            t = (float)i / 127.0f;
            this.lut[i] = ImageMath.mixColors(t, this.shadowColor, this.midColor);
        }
        for (i = 128; i < 256; ++i) {
            t = (float)(i - 127) / 128.0f;
            this.lut[i] = ImageMath.mixColors(t, this.midColor, this.highColor);
        }
        dst = super.filter(src, dst);
        this.lut = null;
        return dst;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        return this.lut[PixelUtils.brightness(rgb)];
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public int getShadowColor() {
        return this.shadowColor;
    }

    public void setMidColor(int midColor) {
        this.midColor = midColor;
    }

    public int getMidColor() {
        return this.midColor;
    }

    public void setHighColor(int highColor) {
        this.highColor = highColor;
    }

    public int getHighColor() {
        return this.highColor;
    }

    public String toString() {
        return "Colors/Tritone...";
    }
}

