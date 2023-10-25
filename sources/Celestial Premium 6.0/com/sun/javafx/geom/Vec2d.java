/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Vec2f;

public class Vec2d {
    public double x;
    public double y;

    public Vec2d() {
    }

    public Vec2d(double d, double d2) {
        this.x = d;
        this.y = d2;
    }

    public Vec2d(Vec2d vec2d) {
        this.set(vec2d);
    }

    public Vec2d(Vec2f vec2f) {
        this.set(vec2f);
    }

    public void set(Vec2d vec2d) {
        this.x = vec2d.x;
        this.y = vec2d.y;
    }

    public void set(Vec2f vec2f) {
        this.x = vec2f.x;
        this.y = vec2f.y;
    }

    public void set(double d, double d2) {
        this.x = d;
        this.y = d2;
    }

    public static double distanceSq(double d, double d2, double d3, double d4) {
        return (d -= d3) * d + (d2 -= d4) * d2;
    }

    public static double distance(double d, double d2, double d3, double d4) {
        return Math.sqrt((d -= d3) * d + (d2 -= d4) * d2);
    }

    public double distanceSq(double d, double d2) {
        return (d -= this.x) * d + (d2 -= this.y) * d2;
    }

    public double distanceSq(Vec2d vec2d) {
        double d = vec2d.x - this.x;
        double d2 = vec2d.y - this.y;
        return d * d + d2 * d2;
    }

    public double distance(double d, double d2) {
        return Math.sqrt((d -= this.x) * d + (d2 -= this.y) * d2);
    }

    public double distance(Vec2d vec2d) {
        double d = vec2d.x - this.x;
        double d2 = vec2d.y - this.y;
        return Math.sqrt(d * d + d2 * d2);
    }

    public int hashCode() {
        long l = 7L;
        l = 31L * l + Double.doubleToLongBits(this.x);
        l = 31L * l + Double.doubleToLongBits(this.y);
        return (int)(l ^ l >> 32);
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Vec2d) {
            Vec2d vec2d = (Vec2d)object;
            return this.x == vec2d.x && this.y == vec2d.y;
        }
        return false;
    }

    public String toString() {
        return "Vec2d[" + this.x + ", " + this.y + "]";
    }
}

