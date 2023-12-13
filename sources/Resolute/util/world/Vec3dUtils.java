// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.world;

import net.minecraft.util.Vec3i;

public class Vec3dUtils
{
    public static final Vec3dUtils ZERO;
    public final double xCoord;
    public final double yCoord;
    public final double zCoord;
    
    public Vec3dUtils(double x, double y2, double z) {
        if (x == -0.0) {
            x = 0.0;
        }
        if (y2 == -0.0) {
            y2 = 0.0;
        }
        if (z == -0.0) {
            z = 0.0;
        }
        this.xCoord = x;
        this.yCoord = y2;
        this.zCoord = z;
    }
    
    public Vec3dUtils(final Vec3i vector) {
        this(vector.getX(), vector.getY(), vector.getZ());
    }
    
    public Vec3dUtils normalize() {
        final double d0 = Math.sqrt(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
        return (d0 < 1.0E-4) ? Vec3dUtils.ZERO : new Vec3dUtils(this.xCoord / d0, this.yCoord / d0, this.zCoord / d0);
    }
    
    public Vec3dUtils subtract(final Vec3dUtils vec) {
        return this.subtract(vec.xCoord, vec.yCoord, vec.zCoord);
    }
    
    public Vec3dUtils subtract(final double x, final double y2, final double z) {
        return this.addVector(-x, -y2, -z);
    }
    
    public Vec3dUtils add(final Vec3dUtils vec) {
        return this.addVector(vec.xCoord, vec.yCoord, vec.zCoord);
    }
    
    public Vec3dUtils addVector(final double x, final double y2, final double z) {
        return new Vec3dUtils(this.xCoord + x, this.yCoord + y2, this.zCoord + z);
    }
    
    public double squareDistanceTo(final Vec3dUtils vec) {
        final double d0 = vec.xCoord - this.xCoord;
        final double d2 = vec.yCoord - this.yCoord;
        final double d3 = vec.zCoord - this.zCoord;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }
    
    public double squareDistanceTo(final double xIn, final double yIn, final double zIn) {
        final double d0 = xIn - this.xCoord;
        final double d2 = yIn - this.yCoord;
        final double d3 = zIn - this.zCoord;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }
    
    public Vec3dUtils scale(final double p_186678_1_) {
        return new Vec3dUtils(this.xCoord * p_186678_1_, this.yCoord * p_186678_1_, this.zCoord * p_186678_1_);
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof Vec3dUtils)) {
            return false;
        }
        final Vec3dUtils vec3d = (Vec3dUtils)p_equals_1_;
        return Double.compare(vec3d.xCoord, this.xCoord) == 0 && Double.compare(vec3d.yCoord, this.yCoord) == 0 && Double.compare(vec3d.zCoord, this.zCoord) == 0;
    }
    
    @Override
    public int hashCode() {
        long j = Double.doubleToLongBits(this.xCoord);
        int i = (int)(j ^ j >>> 32);
        j = Double.doubleToLongBits(this.yCoord);
        i = 31 * i + (int)(j ^ j >>> 32);
        j = Double.doubleToLongBits(this.zCoord);
        i = 31 * i + (int)(j ^ j >>> 32);
        return i;
    }
    
    @Override
    public String toString() {
        return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
    }
    
    static {
        ZERO = new Vec3dUtils(0.0, 0.0, 0.0);
    }
}
