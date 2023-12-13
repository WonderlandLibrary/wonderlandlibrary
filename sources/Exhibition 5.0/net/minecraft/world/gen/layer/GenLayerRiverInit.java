// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.layer;

public class GenLayerRiverInit extends GenLayer
{
    private static final String __OBFID = "CL_00000565";
    
    public GenLayerRiverInit(final long p_i2127_1_, final GenLayer p_i2127_3_) {
        super(p_i2127_1_);
        this.parent = p_i2127_3_;
    }
    
    @Override
    public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
        final int[] var5 = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
        final int[] var6 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int var7 = 0; var7 < areaHeight; ++var7) {
            for (int var8 = 0; var8 < areaWidth; ++var8) {
                this.initChunkSeed(var8 + areaX, var7 + areaY);
                var6[var8 + var7 * areaWidth] = ((var5[var8 + var7 * areaWidth] > 0) ? (this.nextInt(299999) + 2) : 0);
            }
        }
        return var6;
    }
}
