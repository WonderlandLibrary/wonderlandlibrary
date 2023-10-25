/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

public class Point2D {
    public float x;
    public float y;

    public Point2D() {
    }

    public Point2D(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    public void setLocation(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    public void setLocation(Point2D point2D) {
        this.setLocation(point2D.x, point2D.y);
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

    public float distanceSq(Point2D point2D) {
        float f = point2D.x - this.x;
        float f2 = point2D.y - this.y;
        return f * f + f2 * f2;
    }

    public float distance(float f, float f2) {
        return (float)Math.sqrt((f -= this.x) * f + (f2 -= this.y) * f2);
    }

    public float distance(Point2D point2D) {
        float f = point2D.x - this.x;
        float f2 = point2D.y - this.y;
        return (float)Math.sqrt(f * f + f2 * f2);
    }

    public int hashCode() {
        int n = Float.floatToIntBits(this.x);
        return n ^= Float.floatToIntBits(this.y) * 31;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Point2D) {
            Point2D point2D = (Point2D)object;
            return this.x == point2D.x && this.y == point2D.y;
        }
        return false;
    }

    public String toString() {
        return "Point2D[" + this.x + ", " + this.y + "]";
    }
}

