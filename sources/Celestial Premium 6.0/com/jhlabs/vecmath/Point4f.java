/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.vecmath;

import com.jhlabs.vecmath.Tuple4f;

public class Point4f
extends Tuple4f {
    public Point4f() {
        this(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public Point4f(float[] x) {
        this.x = x[0];
        this.y = x[1];
        this.z = x[2];
        this.w = x[3];
    }

    public Point4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Point4f(Point4f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
        this.w = t.w;
    }

    public Point4f(Tuple4f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
        this.w = t.w;
    }

    public float distanceL1(Point4f p) {
        return Math.abs(this.x - p.x) + Math.abs(this.y - p.y) + Math.abs(this.z - p.z) + Math.abs(this.w - p.w);
    }

    public float distanceSquared(Point4f p) {
        float dx = this.x - p.x;
        float dy = this.y - p.y;
        float dz = this.z - p.z;
        float dw = this.w - p.w;
        return dx * dx + dy * dy + dz * dz + dw * dw;
    }

    public float distance(Point4f p) {
        float dx = this.x - p.x;
        float dy = this.y - p.y;
        float dz = this.z - p.z;
        float dw = this.w - p.w;
        return (float)Math.sqrt(dx * dx + dy * dy + dz * dz + dw * dw);
    }
}

