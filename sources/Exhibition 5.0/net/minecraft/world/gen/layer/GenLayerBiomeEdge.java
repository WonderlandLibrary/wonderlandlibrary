// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerBiomeEdge extends GenLayer
{
    private static final String __OBFID = "CL_00000554";
    
    public GenLayerBiomeEdge(final long p_i45475_1_, final GenLayer p_i45475_3_) {
        super(p_i45475_1_);
        this.parent = p_i45475_3_;
    }
    
    @Override
    public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
        final int[] var5 = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        final int[] var6 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int var7 = 0; var7 < areaHeight; ++var7) {
            for (int var8 = 0; var8 < areaWidth; ++var8) {
                this.initChunkSeed(var8 + areaX, var7 + areaY);
                final int var9 = var5[var8 + 1 + (var7 + 1) * (areaWidth + 2)];
                if (!this.replaceBiomeEdgeIfNecessary(var5, var6, var8, var7, areaWidth, var9, BiomeGenBase.extremeHills.biomeID, BiomeGenBase.extremeHillsEdge.biomeID) && !this.replaceBiomeEdge(var5, var6, var8, var7, areaWidth, var9, BiomeGenBase.mesaPlateau_F.biomeID, BiomeGenBase.mesa.biomeID) && !this.replaceBiomeEdge(var5, var6, var8, var7, areaWidth, var9, BiomeGenBase.mesaPlateau.biomeID, BiomeGenBase.mesa.biomeID) && !this.replaceBiomeEdge(var5, var6, var8, var7, areaWidth, var9, BiomeGenBase.megaTaiga.biomeID, BiomeGenBase.taiga.biomeID)) {
                    if (var9 == BiomeGenBase.desert.biomeID) {
                        final int var10 = var5[var8 + 1 + (var7 + 1 - 1) * (areaWidth + 2)];
                        final int var11 = var5[var8 + 1 + 1 + (var7 + 1) * (areaWidth + 2)];
                        final int var12 = var5[var8 + 1 - 1 + (var7 + 1) * (areaWidth + 2)];
                        final int var13 = var5[var8 + 1 + (var7 + 1 + 1) * (areaWidth + 2)];
                        if (var10 != BiomeGenBase.icePlains.biomeID && var11 != BiomeGenBase.icePlains.biomeID && var12 != BiomeGenBase.icePlains.biomeID && var13 != BiomeGenBase.icePlains.biomeID) {
                            var6[var8 + var7 * areaWidth] = var9;
                        }
                        else {
                            var6[var8 + var7 * areaWidth] = BiomeGenBase.extremeHillsPlus.biomeID;
                        }
                    }
                    else if (var9 == BiomeGenBase.swampland.biomeID) {
                        final int var10 = var5[var8 + 1 + (var7 + 1 - 1) * (areaWidth + 2)];
                        final int var11 = var5[var8 + 1 + 1 + (var7 + 1) * (areaWidth + 2)];
                        final int var12 = var5[var8 + 1 - 1 + (var7 + 1) * (areaWidth + 2)];
                        final int var13 = var5[var8 + 1 + (var7 + 1 + 1) * (areaWidth + 2)];
                        if (var10 != BiomeGenBase.desert.biomeID && var11 != BiomeGenBase.desert.biomeID && var12 != BiomeGenBase.desert.biomeID && var13 != BiomeGenBase.desert.biomeID && var10 != BiomeGenBase.coldTaiga.biomeID && var11 != BiomeGenBase.coldTaiga.biomeID && var12 != BiomeGenBase.coldTaiga.biomeID && var13 != BiomeGenBase.coldTaiga.biomeID && var10 != BiomeGenBase.icePlains.biomeID && var11 != BiomeGenBase.icePlains.biomeID && var12 != BiomeGenBase.icePlains.biomeID && var13 != BiomeGenBase.icePlains.biomeID) {
                            if (var10 != BiomeGenBase.jungle.biomeID && var13 != BiomeGenBase.jungle.biomeID && var11 != BiomeGenBase.jungle.biomeID && var12 != BiomeGenBase.jungle.biomeID) {
                                var6[var8 + var7 * areaWidth] = var9;
                            }
                            else {
                                var6[var8 + var7 * areaWidth] = BiomeGenBase.jungleEdge.biomeID;
                            }
                        }
                        else {
                            var6[var8 + var7 * areaWidth] = BiomeGenBase.plains.biomeID;
                        }
                    }
                    else {
                        var6[var8 + var7 * areaWidth] = var9;
                    }
                }
            }
        }
        return var6;
    }
    
    private boolean replaceBiomeEdgeIfNecessary(final int[] p_151636_1_, final int[] p_151636_2_, final int p_151636_3_, final int p_151636_4_, final int p_151636_5_, final int p_151636_6_, final int p_151636_7_, final int p_151636_8_) {
        if (!GenLayer.biomesEqualOrMesaPlateau(p_151636_6_, p_151636_7_)) {
            return false;
        }
        final int var9 = p_151636_1_[p_151636_3_ + 1 + (p_151636_4_ + 1 - 1) * (p_151636_5_ + 2)];
        final int var10 = p_151636_1_[p_151636_3_ + 1 + 1 + (p_151636_4_ + 1) * (p_151636_5_ + 2)];
        final int var11 = p_151636_1_[p_151636_3_ + 1 - 1 + (p_151636_4_ + 1) * (p_151636_5_ + 2)];
        final int var12 = p_151636_1_[p_151636_3_ + 1 + (p_151636_4_ + 1 + 1) * (p_151636_5_ + 2)];
        if (this.canBiomesBeNeighbors(var9, p_151636_7_) && this.canBiomesBeNeighbors(var10, p_151636_7_) && this.canBiomesBeNeighbors(var11, p_151636_7_) && this.canBiomesBeNeighbors(var12, p_151636_7_)) {
            p_151636_2_[p_151636_3_ + p_151636_4_ * p_151636_5_] = p_151636_6_;
        }
        else {
            p_151636_2_[p_151636_3_ + p_151636_4_ * p_151636_5_] = p_151636_8_;
        }
        return true;
    }
    
    private boolean replaceBiomeEdge(final int[] p_151635_1_, final int[] p_151635_2_, final int p_151635_3_, final int p_151635_4_, final int p_151635_5_, final int p_151635_6_, final int p_151635_7_, final int p_151635_8_) {
        if (p_151635_6_ != p_151635_7_) {
            return false;
        }
        final int var9 = p_151635_1_[p_151635_3_ + 1 + (p_151635_4_ + 1 - 1) * (p_151635_5_ + 2)];
        final int var10 = p_151635_1_[p_151635_3_ + 1 + 1 + (p_151635_4_ + 1) * (p_151635_5_ + 2)];
        final int var11 = p_151635_1_[p_151635_3_ + 1 - 1 + (p_151635_4_ + 1) * (p_151635_5_ + 2)];
        final int var12 = p_151635_1_[p_151635_3_ + 1 + (p_151635_4_ + 1 + 1) * (p_151635_5_ + 2)];
        if (GenLayer.biomesEqualOrMesaPlateau(var9, p_151635_7_) && GenLayer.biomesEqualOrMesaPlateau(var10, p_151635_7_) && GenLayer.biomesEqualOrMesaPlateau(var11, p_151635_7_) && GenLayer.biomesEqualOrMesaPlateau(var12, p_151635_7_)) {
            p_151635_2_[p_151635_3_ + p_151635_4_ * p_151635_5_] = p_151635_6_;
        }
        else {
            p_151635_2_[p_151635_3_ + p_151635_4_ * p_151635_5_] = p_151635_8_;
        }
        return true;
    }
    
    private boolean canBiomesBeNeighbors(final int p_151634_1_, final int p_151634_2_) {
        if (GenLayer.biomesEqualOrMesaPlateau(p_151634_1_, p_151634_2_)) {
            return true;
        }
        final BiomeGenBase var3 = BiomeGenBase.getBiome(p_151634_1_);
        final BiomeGenBase var4 = BiomeGenBase.getBiome(p_151634_2_);
        if (var3 != null && var4 != null) {
            final BiomeGenBase.TempCategory var5 = var3.getTempCategory();
            final BiomeGenBase.TempCategory var6 = var4.getTempCategory();
            return var5 == var6 || var5 == BiomeGenBase.TempCategory.MEDIUM || var6 == BiomeGenBase.TempCategory.MEDIUM;
        }
        return false;
    }
}
