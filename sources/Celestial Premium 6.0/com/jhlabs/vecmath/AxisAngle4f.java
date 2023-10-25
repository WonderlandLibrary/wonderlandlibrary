/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.vecmath;

import com.jhlabs.vecmath.Vector3f;

public class AxisAngle4f {
    public float x;
    public float y;
    public float z;
    public float angle;

    public AxisAngle4f() {
        this(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public AxisAngle4f(float[] x) {
        this.x = x[0];
        this.y = x[1];
        this.z = x[2];
        this.angle = x[2];
    }

    public AxisAngle4f(float x, float y, float z, float angle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = angle;
    }

    public AxisAngle4f(AxisAngle4f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
        this.angle = t.angle;
    }

    public AxisAngle4f(Vector3f v, float angle) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.angle = angle;
    }

    public void set(float x, float y, float z, float angle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = angle;
    }

    public void set(AxisAngle4f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
        this.angle = t.angle;
    }

    public void get(AxisAngle4f t) {
        t.x = this.x;
        t.y = this.y;
        t.z = this.z;
        t.angle = this.angle;
    }

    public void get(float[] t) {
        t[0] = this.x;
        t[1] = this.y;
        t[2] = this.z;
        t[3] = this.angle;
    }

    public String toString() {
        return "[" + this.x + ", " + this.y + ", " + this.z + ", " + this.angle + "]";
    }
}

