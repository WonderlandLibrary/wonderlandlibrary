/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.vecmath;

import com.jhlabs.vecmath.Tuple4f;

public class Vector4f
extends Tuple4f {
    public Vector4f() {
        this(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public Vector4f(float[] x) {
        this.x = x[0];
        this.y = x[1];
        this.z = x[2];
        this.w = x[2];
    }

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4f(Vector4f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
        this.w = t.w;
    }

    public Vector4f(Tuple4f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
        this.w = t.w;
    }

    public float dot(Vector4f v) {
        return v.x * this.x + v.y * this.y + v.z * this.z + v.w * this.w;
    }

    public float length() {
        return (float)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
    }

    public void normalize() {
        float d = 1.0f / (this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
        this.x *= d;
        this.y *= d;
        this.z *= d;
        this.w *= d;
    }
}

