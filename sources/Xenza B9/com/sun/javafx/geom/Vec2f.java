// 
// Decompiled by Procyon v0.6.0
// 

package com.sun.javafx.geom;

public class Vec2f
{
    public float x;
    public float y;
    
    public Vec2f() {
    }
    
    public Vec2f(final float x, final float y) {
        this.x = x;
        this.y = y;
    }
    
    public Vec2f(final Vec2f v) {
        this.x = v.x;
        this.y = v.y;
    }
    
    public void set(final Vec2f v) {
        this.x = v.x;
        this.y = v.y;
    }
    
    public void set(final float x, final float y) {
        this.x = x;
        this.y = y;
    }
    
    public static float distanceSq(float x1, float y1, final float x2, final float y2) {
        x1 -= x2;
        y1 -= y2;
        return x1 * x1 + y1 * y1;
    }
    
    public static float distance(float x1, float y1, final float x2, final float y2) {
        x1 -= x2;
        y1 -= y2;
        return (float)Math.sqrt(x1 * x1 + y1 * y1);
    }
    
    public float distanceSq(float vx, float vy) {
        vx -= this.x;
        vy -= this.y;
        return vx * vx + vy * vy;
    }
    
    public float distanceSq(final Vec2f v) {
        final float vx = v.x - this.x;
        final float vy = v.y - this.y;
        return vx * vx + vy * vy;
    }
    
    public float distance(float vx, float vy) {
        vx -= this.x;
        vy -= this.y;
        return (float)Math.sqrt(vx * vx + vy * vy);
    }
    
    public float distance(final Vec2f v) {
        final float vx = v.x - this.x;
        final float vy = v.y - this.y;
        return (float)Math.sqrt(vx * vx + vy * vy);
    }
    
    @Override
    public int hashCode() {
        int bits = 7;
        bits = 31 * bits + Float.floatToIntBits(this.x);
        bits = 31 * bits + Float.floatToIntBits(this.y);
        return bits;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Vec2f) {
            final Vec2f v = (Vec2f)obj;
            return this.x == v.x && this.y == v.y;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Vec2f[" + this.x + ", " + this.y + "]";
    }
}
