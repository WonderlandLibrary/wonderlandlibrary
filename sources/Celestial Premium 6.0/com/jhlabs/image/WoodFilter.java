/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.Colormap;
import com.jhlabs.image.ImageMath;
import com.jhlabs.image.LinearColormap;
import com.jhlabs.image.PixelUtils;
import com.jhlabs.image.PointFilter;
import com.jhlabs.math.Noise;

public class WoodFilter
extends PointFilter {
    private float scale = 200.0f;
    private float stretch = 10.0f;
    private float angle = 1.5707964f;
    private float rings = 0.5f;
    private float turbulence = 0.0f;
    private float fibres = 0.5f;
    private float gain = 0.8f;
    private float m00 = 1.0f;
    private float m01 = 0.0f;
    private float m10 = 0.0f;
    private float m11 = 1.0f;
    private Colormap colormap = new LinearColormap(-1719148, -6784175);

    public void setRings(float rings) {
        this.rings = rings;
    }

    public float getRings() {
        return this.rings;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return this.scale;
    }

    public void setStretch(float stretch) {
        this.stretch = stretch;
    }

    public float getStretch() {
        return this.stretch;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);
        this.m00 = cos;
        this.m01 = sin;
        this.m10 = -sin;
        this.m11 = cos;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setTurbulence(float turbulence) {
        this.turbulence = turbulence;
    }

    public float getTurbulence() {
        return this.turbulence;
    }

    public void setFibres(float fibres) {
        this.fibres = fibres;
    }

    public float getFibres() {
        return this.fibres;
    }

    public void setGain(float gain) {
        this.gain = gain;
    }

    public float getGain() {
        return this.gain;
    }

    public void setColormap(Colormap colormap) {
        this.colormap = colormap;
    }

    public Colormap getColormap() {
        return this.colormap;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        int v;
        float nx = this.m00 * (float)x + this.m01 * (float)y;
        float ny = this.m10 * (float)x + this.m11 * (float)y;
        float f = Noise.noise2(nx /= this.scale, ny /= this.scale * this.stretch);
        f += 0.1f * this.turbulence * Noise.noise2(nx * 0.05f, ny * 20.0f);
        f = f * 0.5f + 0.5f;
        f *= this.rings * 50.0f;
        f -= (float)((int)f);
        f *= 1.0f - ImageMath.smoothStep(this.gain, 1.0f, f);
        f += this.fibres * Noise.noise2(nx * this.scale, ny * 50.0f);
        int a = rgb & 0xFF000000;
        if (this.colormap != null) {
            v = this.colormap.getColor(f);
        } else {
            v = PixelUtils.clamp((int)(f * 255.0f));
            int r = v << 16;
            int g = v << 8;
            int b = v;
            v = a | r | g | b;
        }
        return v;
    }

    public String toString() {
        return "Texture/Wood...";
    }
}

