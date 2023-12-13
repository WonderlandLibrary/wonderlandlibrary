// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import com.google.common.base.Objects;

public class Vec3i implements Comparable
{
    public static final Vec3i NULL_VECTOR;
    private final int x;
    private final int y;
    private final int z;
    private static final String __OBFID = "CL_00002315";
    
    public Vec3i(final int p_i46007_1_, final int p_i46007_2_, final int p_i46007_3_) {
        this.x = p_i46007_1_;
        this.y = p_i46007_2_;
        this.z = p_i46007_3_;
    }
    
    public Vec3i(final double p_i46008_1_, final double p_i46008_3_, final double p_i46008_5_) {
        this(MathHelper.floor_double(p_i46008_1_), MathHelper.floor_double(p_i46008_3_), MathHelper.floor_double(p_i46008_5_));
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof Vec3i)) {
            return false;
        }
        final Vec3i var2 = (Vec3i)p_equals_1_;
        return this.getX() == var2.getX() && this.getY() == var2.getY() && this.getZ() == var2.getZ();
    }
    
    @Override
    public int hashCode() {
        return (this.getY() + this.getZ() * 31) * 31 + this.getX();
    }
    
    public int compareTo(final Vec3i other) {
        return (this.getY() == other.getY()) ? ((this.getZ() == other.getZ()) ? (this.getX() - other.getX()) : (this.getZ() - other.getZ())) : (this.getY() - other.getY());
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getZ() {
        return this.z;
    }
    
    public Vec3i crossProduct(final Vec3i vec) {
        return new Vec3i(this.getY() * vec.getZ() - this.getZ() * vec.getY(), this.getZ() * vec.getX() - this.getX() * vec.getZ(), this.getX() * vec.getY() - this.getY() * vec.getX());
    }
    
    public double distanceSq(final double toX, final double toY, final double toZ) {
        final double var7 = this.getX() - toX;
        final double var8 = this.getY() - toY;
        final double var9 = this.getZ() - toZ;
        return var7 * var7 + var8 * var8 + var9 * var9;
    }
    
    public double distanceSqToCenter(final double p_177957_1_, final double p_177957_3_, final double p_177957_5_) {
        final double var7 = this.getX() + 0.5 - p_177957_1_;
        final double var8 = this.getY() + 0.5 - p_177957_3_;
        final double var9 = this.getZ() + 0.5 - p_177957_5_;
        return var7 * var7 + var8 * var8 + var9 * var9;
    }
    
    public double distanceSq(final Vec3i to) {
        return this.distanceSq(to.getX(), to.getY(), to.getZ());
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper((Object)this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).toString();
    }
    
    @Override
    public int compareTo(final Object p_compareTo_1_) {
        return this.compareTo((Vec3i)p_compareTo_1_);
    }
    
    static {
        NULL_VECTOR = new Vec3i(0, 0, 0);
    }
}
