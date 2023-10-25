/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.PointFilter;

public class FourColorFilter
extends PointFilter {
    private int width;
    private int height;
    private int colorNW;
    private int colorNE;
    private int colorSW;
    private int colorSE;
    private int rNW;
    private int gNW;
    private int bNW;
    private int rNE;
    private int gNE;
    private int bNE;
    private int rSW;
    private int gSW;
    private int bSW;
    private int rSE;
    private int gSE;
    private int bSE;

    public FourColorFilter() {
        this.setColorNW(-65536);
        this.setColorNE(-65281);
        this.setColorSW(-16776961);
        this.setColorSE(-16711681);
    }

    public void setColorNW(int color) {
        this.colorNW = color;
        this.rNW = color >> 16 & 0xFF;
        this.gNW = color >> 8 & 0xFF;
        this.bNW = color & 0xFF;
    }

    public int getColorNW() {
        return this.colorNW;
    }

    public void setColorNE(int color) {
        this.colorNE = color;
        this.rNE = color >> 16 & 0xFF;
        this.gNE = color >> 8 & 0xFF;
        this.bNE = color & 0xFF;
    }

    public int getColorNE() {
        return this.colorNE;
    }

    public void setColorSW(int color) {
        this.colorSW = color;
        this.rSW = color >> 16 & 0xFF;
        this.gSW = color >> 8 & 0xFF;
        this.bSW = color & 0xFF;
    }

    public int getColorSW() {
        return this.colorSW;
    }

    public void setColorSE(int color) {
        this.colorSE = color;
        this.rSE = color >> 16 & 0xFF;
        this.gSE = color >> 8 & 0xFF;
        this.bSE = color & 0xFF;
    }

    public int getColorSE() {
        return this.colorSE;
    }

    @Override
    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
        super.setDimensions(width, height);
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        float fx = (float)x / (float)this.width;
        float fy = (float)y / (float)this.height;
        float p = (float)this.rNW + (float)(this.rNE - this.rNW) * fx;
        float q = (float)this.rSW + (float)(this.rSE - this.rSW) * fx;
        int r = (int)(p + (q - p) * fy + 0.5f);
        p = (float)this.gNW + (float)(this.gNE - this.gNW) * fx;
        q = (float)this.gSW + (float)(this.gSE - this.gSW) * fx;
        int g = (int)(p + (q - p) * fy + 0.5f);
        p = (float)this.bNW + (float)(this.bNE - this.bNW) * fx;
        q = (float)this.bSW + (float)(this.bSE - this.bSW) * fx;
        int b = (int)(p + (q - p) * fy + 0.5f);
        return 0xFF000000 | r << 16 | g << 8 | b;
    }

    public String toString() {
        return "Texture/Four Color Fill...";
    }
}

