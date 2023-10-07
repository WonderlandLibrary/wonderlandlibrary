// 
// Decompiled by Procyon v0.6.0
// 

package com.sun.javafx.geom;

public class Vec3f
{
    public float x;
    public float y;
    public float z;
    
    public Vec3f() {
    }
    
    public Vec3f(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vec3f(final Vec3f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    
    public void set(final Vec3f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    
    public void set(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public final void mul(final float s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
    }
    
    public void sub(final Vec3f t1, final Vec3f t2) {
        this.x = t1.x - t2.x;
        this.y = t1.y - t2.y;
        this.z = t1.z - t2.z;
    }
    
    public void sub(final Vec3f t1) {
        this.x -= t1.x;
        this.y -= t1.y;
        this.z -= t1.z;
    }
    
    public void add(final Vec3f t1, final Vec3f t2) {
        this.x = t1.x + t2.x;
        this.y = t1.y + t2.y;
        this.z = t1.z + t2.z;
    }
    
    public void add(final Vec3f t1) {
        this.x += t1.x;
        this.y += t1.y;
        this.z += t1.z;
    }
    
    public float length() {
        return (float)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }
    
    public void normalize() {
        final float norm = 1.0f / this.length();
        this.x *= norm;
        this.y *= norm;
        this.z *= norm;
    }
    
    public void cross(final Vec3f v1, final Vec3f v2) {
        final float tmpX = v1.y * v2.z - v1.z * v2.y;
        final float tmpY = v2.x * v1.z - v2.z * v1.x;
        this.z = v1.x * v2.y - v1.y * v2.x;
        this.x = tmpX;
        this.y = tmpY;
    }
    
    public float dot(final Vec3f v1) {
        return this.x * v1.x + this.y * v1.y + this.z * v1.z;
    }
    
    @Override
    public int hashCode() {
        int bits = 7;
        bits = 31 * bits + Float.floatToIntBits(this.x);
        bits = 31 * bits + Float.floatToIntBits(this.y);
        bits = 31 * bits + Float.floatToIntBits(this.z);
        return bits;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Vec3f) {
            final Vec3f v = (Vec3f)obj;
            return this.x == v.x && this.y == v.y && this.z == v.z;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Vec3f[" + this.x + ", " + this.y + ", " + this.z + "]";
    }
    
    public float getX() {
        return this.x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public float getZ() {
        return this.z;
    }
}
