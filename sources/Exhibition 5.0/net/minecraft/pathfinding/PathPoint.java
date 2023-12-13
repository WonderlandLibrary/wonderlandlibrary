// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.pathfinding;

import net.minecraft.util.MathHelper;

public class PathPoint
{
    public final int xCoord;
    public final int yCoord;
    public final int zCoord;
    private final int hash;
    int index;
    float totalPathDistance;
    float distanceToNext;
    float distanceToTarget;
    PathPoint previous;
    public boolean visited;
    private static final String __OBFID = "CL_00000574";
    
    public PathPoint(final int p_i2135_1_, final int p_i2135_2_, final int p_i2135_3_) {
        this.index = -1;
        this.xCoord = p_i2135_1_;
        this.yCoord = p_i2135_2_;
        this.zCoord = p_i2135_3_;
        this.hash = makeHash(p_i2135_1_, p_i2135_2_, p_i2135_3_);
    }
    
    public static int makeHash(final int p_75830_0_, final int p_75830_1_, final int p_75830_2_) {
        return (p_75830_1_ & 0xFF) | (p_75830_0_ & 0x7FFF) << 8 | (p_75830_2_ & 0x7FFF) << 24 | ((p_75830_0_ < 0) ? Integer.MIN_VALUE : 0) | ((p_75830_2_ < 0) ? 32768 : 0);
    }
    
    public float distanceTo(final PathPoint p_75829_1_) {
        final float var2 = p_75829_1_.xCoord - this.xCoord;
        final float var3 = p_75829_1_.yCoord - this.yCoord;
        final float var4 = p_75829_1_.zCoord - this.zCoord;
        return MathHelper.sqrt_float(var2 * var2 + var3 * var3 + var4 * var4);
    }
    
    public float distanceToSquared(final PathPoint p_75832_1_) {
        final float var2 = p_75832_1_.xCoord - this.xCoord;
        final float var3 = p_75832_1_.yCoord - this.yCoord;
        final float var4 = p_75832_1_.zCoord - this.zCoord;
        return var2 * var2 + var3 * var3 + var4 * var4;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (!(p_equals_1_ instanceof PathPoint)) {
            return false;
        }
        final PathPoint var2 = (PathPoint)p_equals_1_;
        return this.hash == var2.hash && this.xCoord == var2.xCoord && this.yCoord == var2.yCoord && this.zCoord == var2.zCoord;
    }
    
    @Override
    public int hashCode() {
        return this.hash;
    }
    
    public boolean isAssigned() {
        return this.index >= 0;
    }
    
    @Override
    public String toString() {
        return this.xCoord + ", " + this.yCoord + ", " + this.zCoord;
    }
}
