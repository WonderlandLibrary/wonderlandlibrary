/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.vecmath;

import com.jhlabs.vecmath.Tuple3f;

public class Point3f
extends Tuple3f {
    public Point3f() {
        this(0.0f, 0.0f, 0.0f);
    }

    public Point3f(float[] x) {
        this.x = x[0];
        this.y = x[1];
        this.z = x[2];
    }

    public Point3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3f(Point3f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
    }

    public Point3f(Tuple3f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
    }

    public float distanceL1(Point3f p) {
        return Math.abs(this.x - p.x) + Math.abs(this.y - p.y) + Math.abs(this.z - p.z);
    }

    public float distanceSquared(Point3f p) {
        float dx = this.x - p.x;
        float dy = this.y - p.y;
        float dz = this.z - p.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public float distance(Point3f p) {
        float dx = this.x - p.x;
        float dy = this.y - p.y;
        float dz = this.z - p.z;
        return (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}

