/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.TransferFilter;

public class GammaFilter
extends TransferFilter {
    private float rGamma;
    private float gGamma;
    private float bGamma;

    public GammaFilter() {
        this(1.0f);
    }

    public GammaFilter(float gamma) {
        this(gamma, gamma, gamma);
    }

    public GammaFilter(float rGamma, float gGamma, float bGamma) {
        this.setGamma(rGamma, gGamma, bGamma);
    }

    public void setGamma(float rGamma, float gGamma, float bGamma) {
        this.rGamma = rGamma;
        this.gGamma = gGamma;
        this.bGamma = bGamma;
        this.initialized = false;
    }

    public void setGamma(float gamma) {
        this.setGamma(gamma, gamma, gamma);
    }

    public float getGamma() {
        return this.rGamma;
    }

    @Override
    protected void initialize() {
        this.rTable = this.makeTable(this.rGamma);
        this.gTable = this.gGamma == this.rGamma ? this.rTable : this.makeTable(this.gGamma);
        this.bTable = this.bGamma == this.rGamma ? this.rTable : (this.bGamma == this.gGamma ? this.gTable : this.makeTable(this.bGamma));
    }

    private int[] makeTable(float gamma) {
        int[] table = new int[256];
        for (int i = 0; i < 256; ++i) {
            int v = (int)(255.0 * Math.pow((double)i / 255.0, 1.0 / (double)gamma) + 0.5);
            if (v > 255) {
                v = 255;
            }
            table[i] = v;
        }
        return table;
    }

    public String toString() {
        return "Colors/Gamma...";
    }
}

