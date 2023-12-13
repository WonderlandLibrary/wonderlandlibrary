// 
// Decompiled by Procyon v0.6.0
// 

package com.sun.javafx.geom;

public class Vec2d
{
    public double x;
    public double y;
    
    public Vec2d() {
    }
    
    public Vec2d(final double x, final double y) {
        this.x = x;
        this.y = y;
    }
    
    public Vec2d(final Vec2d v) {
        this.set(v);
    }
    
    public Vec2d(final Vec2f v) {
        this.set(v);
    }
    
    public void set(final Vec2d v) {
        this.x = v.x;
        this.y = v.y;
    }
    
    public void set(final Vec2f v) {
        this.x = v.x;
        this.y = v.y;
    }
    
    public void set(final double x, final double y) {
        this.x = x;
        this.y = y;
    }
    
    public static double distanceSq(double x1, double y1, final double x2, final double y2) {
        x1 -= x2;
        y1 -= y2;
        return x1 * x1 + y1 * y1;
    }
    
    public static double distance(double x1, double y1, final double x2, final double y2) {
        x1 -= x2;
        y1 -= y2;
        return Math.sqrt(x1 * x1 + y1 * y1);
    }
    
    public double distanceSq(double vx, double vy) {
        vx -= this.x;
        vy -= this.y;
        return vx * vx + vy * vy;
    }
    
    public double distanceSq(final Vec2d v) {
        final double vx = v.x - this.x;
        final double vy = v.y - this.y;
        return vx * vx + vy * vy;
    }
    
    public double distance(double vx, double vy) {
        vx -= this.x;
        vy -= this.y;
        return Math.sqrt(vx * vx + vy * vy);
    }
    
    public double distance(final Vec2d v) {
        final double vx = v.x - this.x;
        final double vy = v.y - this.y;
        return Math.sqrt(vx * vx + vy * vy);
    }
    
    @Override
    public int hashCode() {
        long bits = 7L;
        bits = 31L * bits + Double.doubleToLongBits(this.x);
        bits = 31L * bits + Double.doubleToLongBits(this.y);
        return (int)(bits ^ bits >> 32);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Vec2d) {
            final Vec2d v = (Vec2d)obj;
            return this.x == v.x && this.y == v.y;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Vec2d[" + this.x + ", " + this.y + "]";
    }
}
