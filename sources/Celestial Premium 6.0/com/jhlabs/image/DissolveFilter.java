/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ImageMath;
import com.jhlabs.image.PointFilter;
import java.awt.image.BufferedImage;
import java.util.Random;

public class DissolveFilter
extends PointFilter {
    private float density = 1.0f;
    private float softness = 0.0f;
    private float minDensity;
    private float maxDensity;
    private Random randomNumbers;

    public void setDensity(float density) {
        this.density = density;
    }

    public float getDensity() {
        return this.density;
    }

    public void setSoftness(float softness) {
        this.softness = softness;
    }

    public float getSoftness() {
        return this.softness;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        float d = (1.0f - this.density) * (1.0f + this.softness);
        this.minDensity = d - this.softness;
        this.maxDensity = d;
        this.randomNumbers = new Random(0L);
        return super.filter(src, dst);
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        int a = rgb >> 24 & 0xFF;
        float v = this.randomNumbers.nextFloat();
        float f = ImageMath.smoothStep(this.minDensity, this.maxDensity, v);
        return (int)((float)a * f) << 24 | rgb & 0xFFFFFF;
    }

    public String toString() {
        return "Stylize/Dissolve...";
    }
}

