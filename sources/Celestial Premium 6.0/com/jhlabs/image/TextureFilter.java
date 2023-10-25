/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.Colormap;
import com.jhlabs.image.Gradient;
import com.jhlabs.image.ImageMath;
import com.jhlabs.image.PixelUtils;
import com.jhlabs.image.PointFilter;
import com.jhlabs.math.Function2D;
import com.jhlabs.math.Noise;

public class TextureFilter
extends PointFilter {
    private float scale = 32.0f;
    private float stretch = 1.0f;
    private float angle = 0.0f;
    public float amount = 1.0f;
    public float turbulence = 1.0f;
    public float gain = 0.5f;
    public float bias = 0.5f;
    public int operation;
    private float m00 = 1.0f;
    private float m01 = 0.0f;
    private float m10 = 0.0f;
    private float m11 = 1.0f;
    private Colormap colormap = new Gradient();
    private Function2D function = new Noise();

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setFunction(Function2D function) {
        this.function = function;
    }

    public Function2D getFunction() {
        return this.function;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public int getOperation() {
        return this.operation;
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
        float f = (double)this.turbulence == 1.0 ? Noise.noise2(nx, ny) : Noise.turbulence2(nx /= this.scale, ny /= this.scale * this.stretch, this.turbulence);
        f = f * 0.5f + 0.5f;
        f = ImageMath.gain(f, this.gain);
        f = ImageMath.bias(f, this.bias);
        f *= this.amount;
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
        if (this.operation != 0) {
            v = PixelUtils.combinePixels(rgb, v, this.operation);
        }
        return v;
    }

    public String toString() {
        return "Texture/Noise...";
    }
}

