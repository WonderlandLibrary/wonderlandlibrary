/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.light;

import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.light.Light;
import com.sun.scenario.effect.light.PointLight;

public class SpotLight
extends PointLight {
    private float pointsAtX = 0.0f;
    private float pointsAtY = 0.0f;
    private float pointsAtZ = 0.0f;
    private float specularExponent = 1.0f;

    public SpotLight() {
        this(0.0f, 0.0f, 0.0f, Color4f.WHITE);
    }

    public SpotLight(float f, float f2, float f3, Color4f color4f) {
        super(Light.Type.SPOT, f, f2, f3, color4f);
    }

    public float getPointsAtX() {
        return this.pointsAtX;
    }

    public void setPointsAtX(float f) {
        this.pointsAtX = f;
    }

    public float getPointsAtY() {
        return this.pointsAtY;
    }

    public void setPointsAtY(float f) {
        float f2 = this.pointsAtY;
        this.pointsAtY = f;
    }

    public float getPointsAtZ() {
        return this.pointsAtZ;
    }

    public void setPointsAtZ(float f) {
        this.pointsAtZ = f;
    }

    public float getSpecularExponent() {
        return this.specularExponent;
    }

    public void setSpecularExponent(float f) {
        if (f < 0.0f || f > 4.0f) {
            throw new IllegalArgumentException("Specular exponent must be in the range [0,4]");
        }
        this.specularExponent = f;
    }

    @Override
    public float[] getNormalizedLightPosition() {
        float f;
        float f2;
        float f3 = this.getX();
        float f4 = (float)Math.sqrt(f3 * f3 + (f2 = this.getY()) * f2 + (f = this.getZ()) * f);
        if (f4 == 0.0f) {
            f4 = 1.0f;
        }
        float[] arrf = new float[]{f3 / f4, f2 / f4, f / f4};
        return arrf;
    }

    public float[] getNormalizedLightDirection() {
        float f;
        float f2;
        float f3 = this.pointsAtX - this.getX();
        float f4 = (float)Math.sqrt(f3 * f3 + (f2 = this.pointsAtY - this.getY()) * f2 + (f = this.pointsAtZ - this.getZ()) * f);
        if (f4 == 0.0f) {
            f4 = 1.0f;
        }
        float[] arrf = new float[]{f3 / f4, f2 / f4, f / f4};
        return arrf;
    }
}

