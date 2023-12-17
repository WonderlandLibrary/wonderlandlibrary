// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.math;

import javax.annotation.Nullable;

public class Vec3d
{
    public static final Vec3d ZERO;
    public double x;
    public double y;
    public double z;
    
    public Vec3d(double xIn, double yIn, double zIn) {
        if (xIn == -0.0) {
            xIn = 0.0;
        }
        if (yIn == -0.0) {
            yIn = 0.0;
        }
        if (zIn == -0.0) {
            zIn = 0.0;
        }
        this.x = xIn;
        this.y = yIn;
        this.z = zIn;
    }
    
    public Vec3d(final Vec3i vector) {
        this(vector.getX(), vector.getY(), vector.getZ());
    }
    
    public Vec3d subtractReverse(final Vec3d vec) {
        return new Vec3d(vec.x - this.x, vec.y - this.y, vec.z - this.z);
    }
    
    public Vec3d normalize() {
        final double d0 = MathHelper.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        return (d0 < 1.0E-4) ? Vec3d.ZERO : new Vec3d(this.x / d0, this.y / d0, this.z / d0);
    }
    
    public double dotProduct(final Vec3d vec) {
        return this.x * vec.x + this.y * vec.y + this.z * vec.z;
    }
    
    public Vec3d crossProduct(final Vec3d vec) {
        return new Vec3d(this.y * vec.z - this.z * vec.y, this.z * vec.x - this.x * vec.z, this.x * vec.y - this.y * vec.x);
    }
    
    public Vec3d subtract(final Vec3d vec) {
        return this.subtract(vec.x, vec.y, vec.z);
    }
    
    public Vec3d subtract(final double x, final double y, final double z) {
        return this.add(-x, -y, -z);
    }
    
    public Vec3d add(final Vec3d vec) {
        return this.add(vec.x, vec.y, vec.z);
    }
    
    public Vec3d add(final double x, final double y, final double z) {
        return new Vec3d(this.x + x, this.y + y, this.z + z);
    }
    
    public double distanceTo(final Vec3d vec) {
        final double d0 = vec.x - this.x;
        final double d2 = vec.y - this.y;
        final double d3 = vec.z - this.z;
        return MathHelper.sqrt(d0 * d0 + d2 * d2 + d3 * d3);
    }
    
    public double squareDistanceTo(final Vec3d vec) {
        final double d0 = vec.x - this.x;
        final double d2 = vec.y - this.y;
        final double d3 = vec.z - this.z;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }
    
    public double squareDistanceTo(final double xIn, final double yIn, final double zIn) {
        final double d0 = xIn - this.x;
        final double d2 = yIn - this.y;
        final double d3 = zIn - this.z;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }
    
    public Vec3d scale(final double factor) {
        return new Vec3d(this.x * factor, this.y * factor, this.z * factor);
    }
    
    public double length() {
        return MathHelper.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }
    
    public double lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }
    
    @Nullable
    public Vec3d getIntermediateWithXValue(final Vec3d vec, final double x) {
        final double d0 = vec.x - this.x;
        final double d2 = vec.y - this.y;
        final double d3 = vec.z - this.z;
        if (d0 * d0 < 1.0000000116860974E-7) {
            return null;
        }
        final double d4 = (x - this.x) / d0;
        return (d4 >= 0.0 && d4 <= 1.0) ? new Vec3d(this.x + d0 * d4, this.y + d2 * d4, this.z + d3 * d4) : null;
    }
    
    @Nullable
    public Vec3d getIntermediateWithYValue(final Vec3d vec, final double y) {
        final double d0 = vec.x - this.x;
        final double d2 = vec.y - this.y;
        final double d3 = vec.z - this.z;
        if (d2 * d2 < 1.0000000116860974E-7) {
            return null;
        }
        final double d4 = (y - this.y) / d2;
        return (d4 >= 0.0 && d4 <= 1.0) ? new Vec3d(this.x + d0 * d4, this.y + d2 * d4, this.z + d3 * d4) : null;
    }
    
    @Nullable
    public Vec3d getIntermediateWithZValue(final Vec3d vec, final double z) {
        final double d0 = vec.x - this.x;
        final double d2 = vec.y - this.y;
        final double d3 = vec.z - this.z;
        if (d3 * d3 < 1.0000000116860974E-7) {
            return null;
        }
        final double d4 = (z - this.z) / d3;
        return (d4 >= 0.0 && d4 <= 1.0) ? new Vec3d(this.x + d0 * d4, this.y + d2 * d4, this.z + d3 * d4) : null;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof Vec3d)) {
            return false;
        }
        final Vec3d vec3d = (Vec3d)p_equals_1_;
        return Double.compare(vec3d.x, this.x) == 0 && Double.compare(vec3d.y, this.y) == 0 && Double.compare(vec3d.z, this.z) == 0;
    }
    
    @Override
    public int hashCode() {
        long j = Double.doubleToLongBits(this.x);
        int i = (int)(j ^ j >>> 32);
        j = Double.doubleToLongBits(this.y);
        i = 31 * i + (int)(j ^ j >>> 32);
        j = Double.doubleToLongBits(this.z);
        i = 31 * i + (int)(j ^ j >>> 32);
        return i;
    }
    
    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }
    
    public Vec3d rotatePitch(final float pitch) {
        final float f = MathHelper.cos(pitch);
        final float f2 = MathHelper.sin(pitch);
        final double d0 = this.x;
        final double d2 = this.y * f + this.z * f2;
        final double d3 = this.z * f - this.y * f2;
        return new Vec3d(d0, d2, d3);
    }
    
    public Vec3d rotateYaw(final float yaw) {
        final float f = MathHelper.cos(yaw);
        final float f2 = MathHelper.sin(yaw);
        final double d0 = this.x * f + this.z * f2;
        final double d2 = this.y;
        final double d3 = this.z * f - this.x * f2;
        return new Vec3d(d0, d2, d3);
    }
    
    public static Vec3d fromPitchYaw(final Vec2f p_189984_0_) {
        return fromPitchYaw(p_189984_0_.x, p_189984_0_.y);
    }
    
    public static Vec3d fromPitchYaw(final float p_189986_0_, final float p_189986_1_) {
        final float f = MathHelper.cos(-p_189986_1_ * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-p_189986_1_ * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-p_189986_0_ * 0.017453292f);
        final float f4 = MathHelper.sin(-p_189986_0_ * 0.017453292f);
        return new Vec3d(f2 * f3, f4, f * f3);
    }
    
    static {
        ZERO = new Vec3d(0.0, 0.0, 0.0);
    }
}
