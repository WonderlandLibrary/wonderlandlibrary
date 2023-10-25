/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.vecmath;

public class Tuple3f {
    public float x;
    public float y;
    public float z;

    public Tuple3f() {
        this(0.0f, 0.0f, 0.0f);
    }

    public Tuple3f(float[] x) {
        this.x = x[0];
        this.y = x[1];
        this.z = x[2];
    }

    public Tuple3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Tuple3f(Tuple3f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
    }

    public void absolute() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        this.z = Math.abs(this.z);
    }

    public void absolute(Tuple3f t) {
        this.x = Math.abs(t.x);
        this.y = Math.abs(t.y);
        this.z = Math.abs(t.z);
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
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(float[] x) {
        this.x = x[0];
        this.y = x[1];
        this.z = x[2];
    }

    public void set(Tuple3f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
    }

    public void get(Tuple3f t) {
        t.x = this.x;
        t.y = this.y;
        t.z = this.z;
    }

    public void get(float[] t) {
        t[0] = this.x;
        t[1] = this.y;
        t[2] = this.z;
    }

    public void negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }

    public void negate(Tuple3f t) {
        this.x = -t.x;
        this.y = -t.y;
        this.z = -t.z;
    }

    public void interpolate(Tuple3f t, float alpha) {
        float a = 1.0f - alpha;
        this.x = a * this.x + alpha * t.x;
        this.y = a * this.y + alpha * t.y;
        this.z = a * this.z + alpha * t.z;
    }

    public void scale(float s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
    }

    public void add(Tuple3f t) {
        this.x += t.x;
        this.y += t.y;
        this.z += t.z;
    }

    public void add(Tuple3f t1, Tuple3f t2) {
        this.x = t1.x + t2.x;
        this.y = t1.y + t2.y;
        this.z = t1.z + t2.z;
    }

    public void sub(Tuple3f t) {
        this.x -= t.x;
        this.y -= t.y;
        this.z -= t.z;
    }

    public void sub(Tuple3f t1, Tuple3f t2) {
        this.x = t1.x - t2.x;
        this.y = t1.y - t2.y;
        this.z = t1.z - t2.z;
    }

    public void scaleAdd(float s, Tuple3f t) {
        this.x += s * t.x;
        this.y += s * t.y;
        this.z += s * t.z;
    }

    public void scaleAdd(float s, Tuple3f t1, Tuple3f t2) {
        this.x = s * t1.x + t2.x;
        this.y = s * t1.y + t2.y;
        this.z = s * t1.z + t2.z;
    }

    public String toString() {
        return "[" + this.x + ", " + this.y + ", " + this.z + "]";
    }
}

