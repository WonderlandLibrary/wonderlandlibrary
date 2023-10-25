/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.TransformFilter;
import java.awt.image.BufferedImage;

public class DiffuseFilter
extends TransformFilter {
    private float[] sinTable;
    private float[] cosTable;
    private float scale = 4.0f;

    public DiffuseFilter() {
        this.setEdgeAction(1);
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return this.scale;
    }

    @Override
    protected void transformInverse(int x, int y, float[] out) {
        int angle = (int)(Math.random() * 255.0);
        float distance = (float)Math.random();
        out[0] = (float)x + distance * this.sinTable[angle];
        out[1] = (float)y + distance * this.cosTable[angle];
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        this.sinTable = new float[256];
        this.cosTable = new float[256];
        for (int i = 0; i < 256; ++i) {
            float angle = (float)Math.PI * 2 * (float)i / 256.0f;
            this.sinTable[i] = (float)((double)this.scale * Math.sin(angle));
            this.cosTable[i] = (float)((double)this.scale * Math.cos(angle));
        }
        return super.filter(src, dst);
    }

    public String toString() {
        return "Distort/Diffuse...";
    }
}

