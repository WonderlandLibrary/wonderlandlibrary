/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.light;

import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.light.Light;

public class DistantLight
extends Light {
    private float azimuth;
    private float elevation;

    public DistantLight() {
        this(0.0f, 0.0f, Color4f.WHITE);
    }

    public DistantLight(float f, float f2, Color4f color4f) {
        super(Light.Type.DISTANT, color4f);
        this.azimuth = f;
        this.elevation = f2;
    }

    public float getAzimuth() {
        return this.azimuth;
    }

    public void setAzimuth(float f) {
        this.azimuth = f;
    }

    public float getElevation() {
        return this.elevation;
    }

    public void setElevation(float f) {
        this.elevation = f;
    }

    @Override
    public float[] getNormalizedLightPosition() {
        float f;
        float f2;
        double d = Math.toRadians(this.azimuth);
        double d2 = Math.toRadians(this.elevation);
        float f3 = (float)(Math.cos(d) * Math.cos(d2));
        float f4 = (float)Math.sqrt(f3 * f3 + (f2 = (float)(Math.sin(d) * Math.cos(d2))) * f2 + (f = (float)Math.sin(d2)) * f);
        if (f4 == 0.0f) {
            f4 = 1.0f;
        }
        float[] arrf = new float[]{f3 / f4, f2 / f4, f / f4};
        return arrf;
    }
}

