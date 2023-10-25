/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.PixelUtils;
import com.jhlabs.image.TransformFilter;
import com.jhlabs.math.Noise;
import java.awt.image.BufferedImage;

public class MarbleFilter
extends TransformFilter {
    private float[] sinTable;
    private float[] cosTable;
    private float xScale = 4.0f;
    private float yScale = 4.0f;
    private float amount = 1.0f;
    private float turbulence = 1.0f;

    public MarbleFilter() {
        this.setEdgeAction(1);
    }

    public void setXScale(float xScale) {
        this.xScale = xScale;
    }

    public float getXScale() {
        return this.xScale;
    }

    public void setYScale(float yScale) {
        this.yScale = yScale;
    }

    public float getYScale() {
        return this.yScale;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setTurbulence(float turbulence) {
        this.turbulence = turbulence;
    }

    public float getTurbulence() {
        return this.turbulence;
    }

    private void initialize() {
        this.sinTable = new float[256];
        this.cosTable = new float[256];
        for (int i = 0; i < 256; ++i) {
            float angle = (float)Math.PI * 2 * (float)i / 256.0f * this.turbulence;
            this.sinTable[i] = (float)((double)(-this.yScale) * Math.sin(angle));
            this.cosTable[i] = (float)((double)this.yScale * Math.cos(angle));
        }
    }

    private int displacementMap(int x, int y) {
        return PixelUtils.clamp((int)(127.0f * (1.0f + Noise.noise2((float)x / this.xScale, (float)y / this.xScale))));
    }

    @Override
    protected void transformInverse(int x, int y, float[] out) {
        int displacement = this.displacementMap(x, y);
        out[0] = (float)x + this.sinTable[displacement];
        out[1] = (float)y + this.cosTable[displacement];
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        this.initialize();
        return super.filter(src, dst);
    }

    public String toString() {
        return "Distort/Marble...";
    }
}

