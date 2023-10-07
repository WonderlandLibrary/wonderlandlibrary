// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.layer;

public class GenLayerIsland extends GenLayer
{
    private static final String __OBFID = "CL_00000558";
    
    public GenLayerIsland(final long p_i2124_1_) {
        super(p_i2124_1_);
    }
    
    @Override
    public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
        final int[] var5 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int var6 = 0; var6 < areaHeight; ++var6) {
            for (int var7 = 0; var7 < areaWidth; ++var7) {
                this.initChunkSeed(areaX + var7, areaY + var6);
                var5[var7 + var6 * areaWidth] = ((this.nextInt(10) == 0) ? 1 : 0);
            }
        }
        if (areaX > -areaWidth && areaX <= 0 && areaY > -areaHeight && areaY <= 0) {
            var5[-areaX + -areaY * areaWidth] = 1;
        }
        return var5;
    }
}
