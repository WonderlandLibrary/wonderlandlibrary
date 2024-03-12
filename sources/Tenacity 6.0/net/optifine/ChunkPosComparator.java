// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import java.util.Comparator;

public class ChunkPosComparator implements Comparator<ChunkCoordIntPair>
{
    private int chunkPosX;
    private int chunkPosZ;
    private double yawRad;
    private double pitchNorm;
    
    public ChunkPosComparator(final int chunkPosX, final int chunkPosZ, final double yawRad, final double pitchRad) {
        this.chunkPosX = chunkPosX;
        this.chunkPosZ = chunkPosZ;
        this.yawRad = yawRad;
        this.pitchNorm = 1.0 - MathHelper.clamp_double(Math.abs(pitchRad) / 1.5707963267948966, 0.0, 1.0);
    }
    
    @Override
    public int compare(final ChunkCoordIntPair cp1, final ChunkCoordIntPair cp2) {
        final int i = this.getDistSq(cp1);
        final int j = this.getDistSq(cp2);
        return i - j;
    }
    
    private int getDistSq(final ChunkCoordIntPair cp) {
        final int i = cp.chunkXPos - this.chunkPosX;
        final int j = cp.chunkZPos - this.chunkPosZ;
        int k = i * i + j * j;
        final double d0 = MathHelper.atan2(j, i);
        double d2 = Math.abs(d0 - this.yawRad);
        if (d2 > 3.141592653589793) {
            d2 = 6.283185307179586 - d2;
        }
        k = (int)(k * 1000.0 * this.pitchNorm * d2 * d2);
        return k;
    }
}
