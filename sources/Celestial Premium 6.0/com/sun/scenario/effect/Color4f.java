/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

public final class Color4f {
    public static final Color4f BLACK = new Color4f(0.0f, 0.0f, 0.0f, 1.0f);
    public static final Color4f WHITE = new Color4f(1.0f, 1.0f, 1.0f, 1.0f);
    private final float r;
    private final float g;
    private final float b;
    private final float a;

    public Color4f(float f, float f2, float f3, float f4) {
        this.r = f;
        this.g = f2;
        this.b = f3;
        this.a = f4;
    }

    public float getRed() {
        return this.r;
    }

    public float getGreen() {
        return this.g;
    }

    public float getBlue() {
        return this.b;
    }

    public float getAlpha() {
        return this.a;
    }

    public float[] getPremultipliedRGBComponents() {
        float[] arrf = new float[]{this.r * this.a, this.g * this.a, this.b * this.a, this.a};
        return arrf;
    }
}

