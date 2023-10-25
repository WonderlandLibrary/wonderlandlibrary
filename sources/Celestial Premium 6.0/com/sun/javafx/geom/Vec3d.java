/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Vec3f;

public class Vec3d {
    public double x;
    public double y;
    public double z;

    public Vec3d() {
    }

    public Vec3d(double d, double d2, double d3) {
        this.x = d;
        this.y = d2;
        this.z = d3;
    }

    public Vec3d(Vec3d vec3d) {
        this.set(vec3d);
    }

    public Vec3d(Vec3f vec3f) {
        this.set(vec3f);
    }

    public void set(Vec3f vec3f) {
        this.x = vec3f.x;
        this.y = vec3f.y;
        this.z = vec3f.z;
    }

    public void set(Vec3d vec3d) {
        this.x = vec3d.x;
        this.y = vec3d.y;
        this.z = vec3d.z;
    }

    public void set(double d, double d2, double d3) {
        this.x = d;
        this.y = d2;
        this.z = d3;
    }

    public void mul(double d) {
        this.x *= d;
        this.y *= d;
        this.z *= d;
    }

    public void sub(Vec3f vec3f, Vec3f vec3f2) {
        this.x = vec3f.x - vec3f2.x;
        this.y = vec3f.y - vec3f2.y;
        this.z = vec3f.z - vec3f2.z;
    }

    public void sub(Vec3d vec3d, Vec3d vec3d2) {
        this.x = vec3d.x - vec3d2.x;
        this.y = vec3d.y - vec3d2.y;
        this.z = vec3d.z - vec3d2.z;
    }

    public void sub(Vec3d vec3d) {
        this.x -= vec3d.x;
        this.y -= vec3d.y;
        this.z -= vec3d.z;
    }

    public void add(Vec3d vec3d, Vec3d vec3d2) {
        this.x = vec3d.x + vec3d2.x;
        this.y = vec3d.y + vec3d2.y;
        this.z = vec3d.z + vec3d2.z;
    }

    public void add(Vec3d vec3d) {
        this.x += vec3d.x;
        this.y += vec3d.y;
        this.z += vec3d.z;
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public void normalize() {
        double d = 1.0 / this.length();
        this.x *= d;
        this.y *= d;
        this.z *= d;
    }

    public void cross(Vec3d vec3d, Vec3d vec3d2) {
        double d = vec3d.y * vec3d2.z - vec3d.z * vec3d2.y;
        double d2 = vec3d2.x * vec3d.z - vec3d2.z * vec3d.x;
        this.z = vec3d.x * vec3d2.y - vec3d.y * vec3d2.x;
        this.x = d;
        this.y = d2;
    }

    public double dot(Vec3d vec3d) {
        return this.x * vec3d.x + this.y * vec3d.y + this.z * vec3d.z;
    }

    public int hashCode() {
        long l = 7L;
        l = 31L * l + Double.doubleToLongBits(this.x);
        l = 31L * l + Double.doubleToLongBits(this.y);
        l = 31L * l + Double.doubleToLongBits(this.z);
        return (int)(l ^ l >> 32);
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Vec3d) {
            Vec3d vec3d = (Vec3d)object;
            return this.x == vec3d.x && this.y == vec3d.y && this.z == vec3d.z;
        }
        return false;
    }

    public String toString() {
        return "Vec3d[" + this.x + ", " + this.y + ", " + this.z + "]";
    }
}

