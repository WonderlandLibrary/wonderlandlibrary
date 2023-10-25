/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

public class Vec2f {
    public float x;
    public float y;

    public Vec2f() {
    }

    public Vec2f(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    public Vec2f(Vec2f vec2f) {
        this.x = vec2f.x;
        this.y = vec2f.y;
    }

    public void set(Vec2f vec2f) {
        this.x = vec2f.x;
        this.y = vec2f.y;
    }

    public void set(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    public static float distanceSq(float f, float f2, float f3, float f4) {
        return (f -= f3) * f + (f2 -= f4) * f2;
    }

    public static float distance(float f, float f2, float f3, float f4) {
        return (float)Math.sqrt((f -= f3) * f + (f2 -= f4) * f2);
    }

    public float distanceSq(float f, float f2) {
        return (f -= this.x) * f + (f2 -= this.y) * f2;
    }

    public float distanceSq(Vec2f vec2f) {
        float f = vec2f.x - this.x;
        float f2 = vec2f.y - this.y;
        return f * f + f2 * f2;
    }

    public float distance(float f, float f2) {
        return (float)Math.sqrt((f -= this.x) * f + (f2 -= this.y) * f2);
    }

    public float distance(Vec2f vec2f) {
        float f = vec2f.x - this.x;
        float f2 = vec2f.y - this.y;
        return (float)Math.sqrt(f * f + f2 * f2);
    }

    public int hashCode() {
        int n = 7;
        n = 31 * n + Float.floatToIntBits(this.x);
        n = 31 * n + Float.floatToIntBits(this.y);
        return n;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Vec2f) {
            Vec2f vec2f = (Vec2f)object;
            return this.x == vec2f.x && this.y == vec2f.y;
        }
        return false;
    }

    public String toString() {
        return "Vec2f[" + this.x + ", " + this.y + "]";
    }
}

