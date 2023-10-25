/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.Colormap;
import com.jhlabs.image.PixelUtils;
import com.jhlabs.image.PointFilter;
import com.jhlabs.math.Noise;

public class MarbleTexFilter
extends PointFilter {
    private float scale = 32.0f;
    private float stretch = 1.0f;
    private float angle = 0.0f;
    private float turbulence = 1.0f;
    private float turbulenceFactor = 0.5f;
    private Colormap colormap;
    private float m00 = 1.0f;
    private float m01 = 0.0f;
    private float m10 = 0.0f;
    private float m11 = 1.0f;

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

    public void setTurbulenceFactor(float turbulenceFactor) {
        this.turbulenceFactor = turbulenceFactor;
    }

    public float getTurbulenceFactor() {
        return this.turbulenceFactor;
    }

    public void setColormap(Colormap colormap) {
        this.colormap = colormap;
    }

    public Colormap getColormap() {
        return this.colormap;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        float brownLayer;
        float nx = this.m00 * (float)x + this.m01 * (float)y;
        float ny = this.m10 * (float)x + this.m11 * (float)y;
        nx /= this.scale * this.stretch;
        ny /= this.scale;
        int a = rgb & 0xFF000000;
        if (this.colormap != null) {
            float chaos = this.turbulenceFactor * Noise.turbulence2(nx, ny, this.turbulence);
            float f = 3.0f * this.turbulenceFactor * chaos + ny;
            f = (float)Math.sin((double)f * Math.PI);
            float perturb = (float)Math.sin(40.0 * (double)chaos);
            f = (float)((double)f + 0.2 * (double)perturb);
            return this.colormap.getColor(f);
        }
        float chaos = this.turbulenceFactor * Noise.turbulence2(nx, ny, this.turbulence);
        float t = (float)Math.sin(Math.sin(8.0 * (double)chaos + (double)(7.0f * nx) + 3.0 * (double)ny));
        float greenLayer = brownLayer = Math.abs(t);
        float perturb = (float)Math.sin(40.0 * (double)chaos);
        perturb = Math.abs(perturb);
        float brownPerturb = 0.6f * perturb + 0.3f;
        float greenPerturb = 0.2f * perturb + 0.8f;
        float grnPerturb = 0.15f * perturb + 0.85f;
        float grn = 0.5f * (float)Math.pow(Math.abs(brownLayer), 0.3);
        brownLayer = (float)Math.pow(0.5 * ((double)brownLayer + 1.0), 0.6) * brownPerturb;
        greenLayer = (float)Math.pow(0.5 * ((double)greenLayer + 1.0), 0.6) * greenPerturb;
        float red = (0.5f * brownLayer + 0.35f * greenLayer) * 2.0f * grn;
        float blu = (0.25f * brownLayer + 0.35f * greenLayer) * 2.0f * grn;
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        r = PixelUtils.clamp((int)((float)r * red));
        g = PixelUtils.clamp((int)((float)g * (grn *= Math.max(brownLayer, greenLayer) * grnPerturb)));
        b = PixelUtils.clamp((int)((float)b * blu));
        return rgb & 0xFF000000 | r << 16 | g << 8 | b;
    }

    public String toString() {
        return "Texture/Marble Texture...";
    }
}

