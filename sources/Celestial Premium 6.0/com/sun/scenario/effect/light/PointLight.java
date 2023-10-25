/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.light;

import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.light.Light;

public class PointLight
extends Light {
    private float x;
    private float y;
    private float z;

    public PointLight() {
        this(0.0f, 0.0f, 0.0f, Color4f.WHITE);
    }

    public PointLight(float f, float f2, float f3, Color4f color4f) {
        this(Light.Type.POINT, f, f2, f3, color4f);
    }

    PointLight(Light.Type type, float f, float f2, float f3, Color4f color4f) {
        super(type, color4f);
        this.x = f;
        this.y = f2;
        this.z = f3;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float f) {
        this.x = f;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float f) {
        this.y = f;
    }

    public float getZ() {
        return this.z;
    }

    public void setZ(float f) {
        this.z = f;
    }

    @Override
    public float[] getNormalizedLightPosition() {
        float f = (float)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        if (f == 0.0f) {
            f = 1.0f;
        }
        float[] arrf = new float[]{this.x / f, this.y / f, this.z / f};
        return arrf;
    }
}

