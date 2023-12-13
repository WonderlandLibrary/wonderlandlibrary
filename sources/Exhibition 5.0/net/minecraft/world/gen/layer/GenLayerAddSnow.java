// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.layer;

public class GenLayerAddSnow extends GenLayer
{
    private static final String __OBFID = "CL_00000553";
    
    public GenLayerAddSnow(final long p_i2121_1_, final GenLayer p_i2121_3_) {
        super(p_i2121_1_);
        this.parent = p_i2121_3_;
    }
    
    @Override
    public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
        final int var5 = areaX - 1;
        final int var6 = areaY - 1;
        final int var7 = areaWidth + 2;
        final int var8 = areaHeight + 2;
        final int[] var9 = this.parent.getInts(var5, var6, var7, var8);
        final int[] var10 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int var11 = 0; var11 < areaHeight; ++var11) {
            for (int var12 = 0; var12 < areaWidth; ++var12) {
                final int var13 = var9[var12 + 1 + (var11 + 1) * var7];
                this.initChunkSeed(var12 + areaX, var11 + areaY);
                if (var13 == 0) {
                    var10[var12 + var11 * areaWidth] = 0;
                }
                else {
                    final int var14 = this.nextInt(6);
                    byte var15;
                    if (var14 == 0) {
                        var15 = 4;
                    }
                    else if (var14 <= 1) {
                        var15 = 3;
                    }
                    else {
                        var15 = 1;
                    }
                    var10[var12 + var11 * areaWidth] = var15;
                }
            }
        }
        return var10;
    }
}
