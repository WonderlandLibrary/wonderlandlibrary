// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerRiver extends GenLayer
{
    private static final String __OBFID = "CL_00000566";
    
    public GenLayerRiver(final long p_i2128_1_, final GenLayer p_i2128_3_) {
        super(p_i2128_1_);
        super.parent = p_i2128_3_;
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
                final int var13 = this.func_151630_c(var9[var12 + 0 + (var11 + 1) * var7]);
                final int var14 = this.func_151630_c(var9[var12 + 2 + (var11 + 1) * var7]);
                final int var15 = this.func_151630_c(var9[var12 + 1 + (var11 + 0) * var7]);
                final int var16 = this.func_151630_c(var9[var12 + 1 + (var11 + 2) * var7]);
                final int var17 = this.func_151630_c(var9[var12 + 1 + (var11 + 1) * var7]);
                if (var17 == var13 && var17 == var15 && var17 == var14 && var17 == var16) {
                    var10[var12 + var11 * areaWidth] = -1;
                }
                else {
                    var10[var12 + var11 * areaWidth] = BiomeGenBase.river.biomeID;
                }
            }
        }
        return var10;
    }
    
    private int func_151630_c(final int p_151630_1_) {
        return (p_151630_1_ >= 2) ? (2 + (p_151630_1_ & 0x1)) : p_151630_1_;
    }
}
