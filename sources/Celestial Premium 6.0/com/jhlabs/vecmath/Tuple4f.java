/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.vecmath;

public class Tuple4f {
    public float x;
    public float y;
    public float z;
    public float w;

    public Tuple4f() {
        this(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public Tuple4f(float[] x) {
        this.x = x[0];
        this.y = x[1];
        this.z = x[2];
        this.w = x[2];
    }

    public Tuple4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Tuple4f(Tuple4f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
        this.w = t.w;
    }

    public void absolute() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        this.z = Math.abs(this.z);
        this.w = Math.abs(this.w);
    }

    public void absolute(Tuple4f t) {
        this.x = Math.abs(t.x);
        this.y = Math.abs(t.y);
        this.z = Math.abs(t.z);
        this.w = Math.abs(t.w);
    }

    public void clamp(float min, float max) {
        if (this.x < min) {
            this.x = min;
        } else if (this.x > max) {
            this.x = max;
        }
        if (this.y < min) {
            this.y = min;
        } else if (this.y > max) {
            this.y = max;
        }
        if (this.z < min) {
            this.z = min;
        } else if (this.z > max) {
            this.z = max;
        }
        if (this.w < min) {
            this.w = min;
        } else if (this.w > max) {
            this.w = max;
        }
    }

    public void set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void set(float[] x) {
        this.x = x[0];
        this.y = x[1];
        this.z = x[2];
        this.w = x[2];
    }

    public void set(Tuple4f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
        this.w = t.w;
    }

    public void get(Tuple4f t) {
        t.x = this.x;
        t.y = this.y;
        t.z = this.z;
        t.w = this.w;
    }

    public void get(float[] t) {
        t[0] = this.x;
        t[1] = this.y;
        t[2] = this.z;
        t[3] = this.w;
    }

    public void negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        this.w = -this.w;
    }

    public void negate(Tuple4f t) {
        this.x = -t.x;
        this.y = -t.y;
        this.z = -t.z;
        this.w = -t.w;
    }

    public void interpolate(Tuple4f t, float alpha) {
        float a = 1.0f - alpha;
        this.x = a * this.x + alpha * t.x;
        this.y = a * this.y + alpha * t.y;
        this.z = a * this.z + alpha * t.z;
        this.w = a * this.w + alpha * t.w;
    }

    public void scale(float s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
        this.w *= s;
    }

    public void add(Tuple4f t) {
        this.x += t.x;
        this.y += t.y;
        this.z += t.z;
        this.w += t.w;
    }

    public void add(Tuple4f t1, Tuple4f t2) {
        this.x = t1.x + t2.x;
        this.y = t1.y + t2.y;
        this.z = t1.z + t2.z;
        this.w = t1.w + t2.w;
    }

    public void sub(Tuple4f t) {
        this.x -= t.x;
        this.y -= t.y;
        this.z -= t.z;
        this.w -= t.w;
    }

    public void sub(Tuple4f t1, Tuple4f t2) {
        this.x = t1.x - t2.x;
        this.y = t1.y - t2.y;
        this.z = t1.z - t2.z;
        this.w = t1.w - t2.w;
    }

    public String toString() {
        return "[" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + "]";
    }
}

