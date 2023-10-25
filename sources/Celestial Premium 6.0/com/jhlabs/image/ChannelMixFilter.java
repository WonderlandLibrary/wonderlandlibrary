/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.PixelUtils;
import com.jhlabs.image.PointFilter;

public class ChannelMixFilter
extends PointFilter {
    private int blueGreen;
    private int redBlue;
    private int greenRed;
    private int intoR;
    private int intoG;
    private int intoB;

    public ChannelMixFilter() {
        this.canFilterIndexColorModel = true;
    }

    public void setBlueGreen(int blueGreen) {
        this.blueGreen = blueGreen;
    }

    public int getBlueGreen() {
        return this.blueGreen;
    }

    public void setRedBlue(int redBlue) {
        this.redBlue = redBlue;
    }

    public int getRedBlue() {
        return this.redBlue;
    }

    public void setGreenRed(int greenRed) {
        this.greenRed = greenRed;
    }

    public int getGreenRed() {
        return this.greenRed;
    }

    public void setIntoR(int intoR) {
        this.intoR = intoR;
    }

    public int getIntoR() {
        return this.intoR;
    }

    public void setIntoG(int intoG) {
        this.intoG = intoG;
    }

    public int getIntoG() {
        return this.intoG;
    }

    public void setIntoB(int intoB) {
        this.intoB = intoB;
    }

    public int getIntoB() {
        return this.intoB;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        int a = rgb & 0xFF000000;
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        int nr = PixelUtils.clamp((this.intoR * (this.blueGreen * g + (255 - this.blueGreen) * b) / 255 + (255 - this.intoR) * r) / 255);
        int ng = PixelUtils.clamp((this.intoG * (this.redBlue * b + (255 - this.redBlue) * r) / 255 + (255 - this.intoG) * g) / 255);
        int nb = PixelUtils.clamp((this.intoB * (this.greenRed * r + (255 - this.greenRed) * g) / 255 + (255 - this.intoB) * b) / 255);
        return a | nr << 16 | ng << 8 | nb;
    }

    public String toString() {
        return "Colors/Mix Channels...";
    }
}

