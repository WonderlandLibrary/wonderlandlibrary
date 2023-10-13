/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenJungle;
import net.minecraft.world.biome.BiomeGenMesa;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerShore
extends GenLayer {
    private static final String __OBFID = "CL_00000568";

    public GenLayerShore(long p_i2130_1_, GenLayer p_i2130_3_) {
        super(p_i2130_1_);
        this.parent = p_i2130_3_;
    }

    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] var5 = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        int[] var6 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int var7 = 0; var7 < areaHeight; ++var7) {
            for (int var8 = 0; var8 < areaWidth; ++var8) {
                int var14;
                int var13;
                int var12;
                int var11;
                this.initChunkSeed(var8 + areaX, var7 + areaY);
                int var9 = var5[var8 + 1 + (var7 + 1) * (areaWidth + 2)];
                BiomeGenBase var10 = BiomeGenBase.getBiome(var9);
                if (var9 == BiomeGenBase.mushroomIsland.biomeID) {
                    var11 = var5[var8 + 1 + (var7 + 1 - 1) * (areaWidth + 2)];
                    var12 = var5[var8 + 1 + 1 + (var7 + 1) * (areaWidth + 2)];
                    var13 = var5[var8 + 1 - 1 + (var7 + 1) * (areaWidth + 2)];
                    var14 = var5[var8 + 1 + (var7 + 1 + 1) * (areaWidth + 2)];
                    if (var11 != BiomeGenBase.ocean.biomeID && var12 != BiomeGenBase.ocean.biomeID && var13 != BiomeGenBase.ocean.biomeID && var14 != BiomeGenBase.ocean.biomeID) {
                        var6[var8 + var7 * areaWidth] = var9;
                        continue;
                    }
                    var6[var8 + var7 * areaWidth] = BiomeGenBase.mushroomIslandShore.biomeID;
                    continue;
                }
                if (var10 != null && var10.getBiomeClass() == BiomeGenJungle.class) {
                    var11 = var5[var8 + 1 + (var7 + 1 - 1) * (areaWidth + 2)];
                    var12 = var5[var8 + 1 + 1 + (var7 + 1) * (areaWidth + 2)];
                    var13 = var5[var8 + 1 - 1 + (var7 + 1) * (areaWidth + 2)];
                    var14 = var5[var8 + 1 + (var7 + 1 + 1) * (areaWidth + 2)];
                    if (this.func_151631_c(var11) && this.func_151631_c(var12) && this.func_151631_c(var13) && this.func_151631_c(var14)) {
                        if (!(GenLayerShore.isBiomeOceanic(var11) || GenLayerShore.isBiomeOceanic(var12) || GenLayerShore.isBiomeOceanic(var13) || GenLayerShore.isBiomeOceanic(var14))) {
                            var6[var8 + var7 * areaWidth] = var9;
                            continue;
                        }
                        var6[var8 + var7 * areaWidth] = BiomeGenBase.beach.biomeID;
                        continue;
                    }
                    var6[var8 + var7 * areaWidth] = BiomeGenBase.jungleEdge.biomeID;
                    continue;
                }
                if (var9 != BiomeGenBase.extremeHills.biomeID && var9 != BiomeGenBase.extremeHillsPlus.biomeID && var9 != BiomeGenBase.extremeHillsEdge.biomeID) {
                    if (var10 != null && var10.isSnowyBiome()) {
                        this.func_151632_a(var5, var6, var8, var7, areaWidth, var9, BiomeGenBase.coldBeach.biomeID);
                        continue;
                    }
                    if (var9 != BiomeGenBase.mesa.biomeID && var9 != BiomeGenBase.mesaPlateau_F.biomeID) {
                        if (var9 != BiomeGenBase.ocean.biomeID && var9 != BiomeGenBase.deepOcean.biomeID && var9 != BiomeGenBase.river.biomeID && var9 != BiomeGenBase.swampland.biomeID) {
                            var11 = var5[var8 + 1 + (var7 + 1 - 1) * (areaWidth + 2)];
                            var12 = var5[var8 + 1 + 1 + (var7 + 1) * (areaWidth + 2)];
                            var13 = var5[var8 + 1 - 1 + (var7 + 1) * (areaWidth + 2)];
                            var14 = var5[var8 + 1 + (var7 + 1 + 1) * (areaWidth + 2)];
                            if (!(GenLayerShore.isBiomeOceanic(var11) || GenLayerShore.isBiomeOceanic(var12) || GenLayerShore.isBiomeOceanic(var13) || GenLayerShore.isBiomeOceanic(var14))) {
                                var6[var8 + var7 * areaWidth] = var9;
                                continue;
                            }
                            var6[var8 + var7 * areaWidth] = BiomeGenBase.beach.biomeID;
                            continue;
                        }
                        var6[var8 + var7 * areaWidth] = var9;
                        continue;
                    }
                    var11 = var5[var8 + 1 + (var7 + 1 - 1) * (areaWidth + 2)];
                    var12 = var5[var8 + 1 + 1 + (var7 + 1) * (areaWidth + 2)];
                    var13 = var5[var8 + 1 - 1 + (var7 + 1) * (areaWidth + 2)];
                    var14 = var5[var8 + 1 + (var7 + 1 + 1) * (areaWidth + 2)];
                    if (!(GenLayerShore.isBiomeOceanic(var11) || GenLayerShore.isBiomeOceanic(var12) || GenLayerShore.isBiomeOceanic(var13) || GenLayerShore.isBiomeOceanic(var14))) {
                        if (this.func_151633_d(var11) && this.func_151633_d(var12) && this.func_151633_d(var13) && this.func_151633_d(var14)) {
                            var6[var8 + var7 * areaWidth] = var9;
                            continue;
                        }
                        var6[var8 + var7 * areaWidth] = BiomeGenBase.desert.biomeID;
                        continue;
                    }
                    var6[var8 + var7 * areaWidth] = var9;
                    continue;
                }
                this.func_151632_a(var5, var6, var8, var7, areaWidth, var9, BiomeGenBase.stoneBeach.biomeID);
            }
        }
        return var6;
    }

    private void func_151632_a(int[] p_151632_1_, int[] p_151632_2_, int p_151632_3_, int p_151632_4_, int p_151632_5_, int p_151632_6_, int p_151632_7_) {
        if (GenLayerShore.isBiomeOceanic(p_151632_6_)) {
            p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_6_;
        } else {
            int var8 = p_151632_1_[p_151632_3_ + 1 + (p_151632_4_ + 1 - 1) * (p_151632_5_ + 2)];
            int var9 = p_151632_1_[p_151632_3_ + 1 + 1 + (p_151632_4_ + 1) * (p_151632_5_ + 2)];
            int var10 = p_151632_1_[p_151632_3_ + 1 - 1 + (p_151632_4_ + 1) * (p_151632_5_ + 2)];
            int var11 = p_151632_1_[p_151632_3_ + 1 + (p_151632_4_ + 1 + 1) * (p_151632_5_ + 2)];
            p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = !GenLayerShore.isBiomeOceanic(var8) && !GenLayerShore.isBiomeOceanic(var9) && !GenLayerShore.isBiomeOceanic(var10) && !GenLayerShore.isBiomeOceanic(var11) ? p_151632_6_ : p_151632_7_;
        }
    }

    private boolean func_151631_c(int p_151631_1_) {
        return BiomeGenBase.getBiome(p_151631_1_) != null && BiomeGenBase.getBiome(p_151631_1_).getBiomeClass() == BiomeGenJungle.class ? true : p_151631_1_ == BiomeGenBase.jungleEdge.biomeID || p_151631_1_ == BiomeGenBase.jungle.biomeID || p_151631_1_ == BiomeGenBase.jungleHills.biomeID || p_151631_1_ == BiomeGenBase.forest.biomeID || p_151631_1_ == BiomeGenBase.taiga.biomeID || GenLayerShore.isBiomeOceanic(p_151631_1_);
    }

    private boolean func_151633_d(int p_151633_1_) {
        return BiomeGenBase.getBiome(p_151633_1_) instanceof BiomeGenMesa;
    }
}

