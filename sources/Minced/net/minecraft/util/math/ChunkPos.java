// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.math;

import net.minecraft.entity.Entity;

public class ChunkPos
{
    public final int x;
    public final int z;
    private int cachedHashCode;
    
    public ChunkPos(final int x, final int z) {
        this.cachedHashCode = 0;
        this.x = x;
        this.z = z;
    }
    
    public ChunkPos(final BlockPos pos) {
        this.cachedHashCode = 0;
        this.x = pos.getX() >> 4;
        this.z = pos.getZ() >> 4;
    }
    
    public static long asLong(final int x, final int z) {
        return ((long)x & 0xFFFFFFFFL) | ((long)z & 0xFFFFFFFFL) << 32;
    }
    
    @Override
    public int hashCode() {
        if (this.cachedHashCode != 0) {
            return this.cachedHashCode;
        }
        final int i = 1664525 * this.x + 1013904223;
        final int j = 1664525 * (this.z ^ 0xDEADBEEF) + 1013904223;
        return this.cachedHashCode = (i ^ j);
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChunkPos)) {
            return false;
        }
        final ChunkPos chunkpos = (ChunkPos)p_equals_1_;
        return this.x == chunkpos.x && this.z == chunkpos.z;
    }
    
    public double getDistanceSq(final Entity entityIn) {
        final double d0 = this.x * 16 + 8;
        final double d2 = this.z * 16 + 8;
        final double d3 = d0 - entityIn.posX;
        final double d4 = d2 - entityIn.posZ;
        return d3 * d3 + d4 * d4;
    }
    
    public int getXStart() {
        return this.x << 4;
    }
    
    public int getZStart() {
        return this.z << 4;
    }
    
    public int getXEnd() {
        return (this.x << 4) + 15;
    }
    
    public int getZEnd() {
        return (this.z << 4) + 15;
    }
    
    public BlockPos getBlock(final int x, final int y, final int z) {
        return new BlockPos((this.x << 4) + x, y, (this.z << 4) + z);
    }
    
    @Override
    public String toString() {
        return "[" + this.x + ", " + this.z + "]";
    }
}
