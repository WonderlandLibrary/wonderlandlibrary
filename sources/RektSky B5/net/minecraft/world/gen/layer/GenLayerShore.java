/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenJungle;
import net.minecraft.world.biome.BiomeGenMesa;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerShore
extends GenLayer {
    public GenLayerShore(long p_i2130_1_, GenLayer p_i2130_3_) {
        super(p_i2130_1_);
        this.parent = p_i2130_3_;
    }

    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i2 = 0; i2 < areaHeight; ++i2) {
            for (int j2 = 0; j2 < areaWidth; ++j2) {
                this.initChunkSeed(j2 + areaX, i2 + areaY);
                int k2 = aint[j2 + 1 + (i2 + 1) * (areaWidth + 2)];
                BiomeGenBase biomegenbase = BiomeGenBase.getBiome(k2);
                if (k2 == BiomeGenBase.mushroomIsland.biomeID) {
                    int j22 = aint[j2 + 1 + (i2 + 1 - 1) * (areaWidth + 2)];
                    int i3 = aint[j2 + 1 + 1 + (i2 + 1) * (areaWidth + 2)];
                    int l3 = aint[j2 + 1 - 1 + (i2 + 1) * (areaWidth + 2)];
                    int k4 = aint[j2 + 1 + (i2 + 1 + 1) * (areaWidth + 2)];
                    if (j22 != BiomeGenBase.ocean.biomeID && i3 != BiomeGenBase.ocean.biomeID && l3 != BiomeGenBase.ocean.biomeID && k4 != BiomeGenBase.ocean.biomeID) {
                        aint1[j2 + i2 * areaWidth] = k2;
                        continue;
                    }
                    aint1[j2 + i2 * areaWidth] = BiomeGenBase.mushroomIslandShore.biomeID;
                    continue;
                }
                if (biomegenbase != null && biomegenbase.getBiomeClass() == BiomeGenJungle.class) {
                    int i22 = aint[j2 + 1 + (i2 + 1 - 1) * (areaWidth + 2)];
                    int l2 = aint[j2 + 1 + 1 + (i2 + 1) * (areaWidth + 2)];
                    int k3 = aint[j2 + 1 - 1 + (i2 + 1) * (areaWidth + 2)];
                    int j4 = aint[j2 + 1 + (i2 + 1 + 1) * (areaWidth + 2)];
                    if (this.func_151631_c(i22) && this.func_151631_c(l2) && this.func_151631_c(k3) && this.func_151631_c(j4)) {
                        if (!(GenLayerShore.isBiomeOceanic(i22) || GenLayerShore.isBiomeOceanic(l2) || GenLayerShore.isBiomeOceanic(k3) || GenLayerShore.isBiomeOceanic(j4))) {
                            aint1[j2 + i2 * areaWidth] = k2;
                            continue;
                        }
                        aint1[j2 + i2 * areaWidth] = BiomeGenBase.beach.biomeID;
                        continue;
                    }
                    aint1[j2 + i2 * areaWidth] = BiomeGenBase.jungleEdge.biomeID;
                    continue;
                }
                if (k2 != BiomeGenBase.extremeHills.biomeID && k2 != BiomeGenBase.extremeHillsPlus.biomeID && k2 != BiomeGenBase.extremeHillsEdge.biomeID) {
                    if (biomegenbase != null && biomegenbase.isSnowyBiome()) {
                        this.func_151632_a(aint, aint1, j2, i2, areaWidth, k2, BiomeGenBase.coldBeach.biomeID);
                        continue;
                    }
                    if (k2 != BiomeGenBase.mesa.biomeID && k2 != BiomeGenBase.mesaPlateau_F.biomeID) {
                        if (k2 != BiomeGenBase.ocean.biomeID && k2 != BiomeGenBase.deepOcean.biomeID && k2 != BiomeGenBase.river.biomeID && k2 != BiomeGenBase.swampland.biomeID) {
                            int l1 = aint[j2 + 1 + (i2 + 1 - 1) * (areaWidth + 2)];
                            int k22 = aint[j2 + 1 + 1 + (i2 + 1) * (areaWidth + 2)];
                            int j3 = aint[j2 + 1 - 1 + (i2 + 1) * (areaWidth + 2)];
                            int i4 = aint[j2 + 1 + (i2 + 1 + 1) * (areaWidth + 2)];
                            if (!(GenLayerShore.isBiomeOceanic(l1) || GenLayerShore.isBiomeOceanic(k22) || GenLayerShore.isBiomeOceanic(j3) || GenLayerShore.isBiomeOceanic(i4))) {
                                aint1[j2 + i2 * areaWidth] = k2;
                                continue;
                            }
                            aint1[j2 + i2 * areaWidth] = BiomeGenBase.beach.biomeID;
                            continue;
                        }
                        aint1[j2 + i2 * areaWidth] = k2;
                        continue;
                    }
                    int l2 = aint[j2 + 1 + (i2 + 1 - 1) * (areaWidth + 2)];
                    int i1 = aint[j2 + 1 + 1 + (i2 + 1) * (areaWidth + 2)];
                    int j1 = aint[j2 + 1 - 1 + (i2 + 1) * (areaWidth + 2)];
                    int k1 = aint[j2 + 1 + (i2 + 1 + 1) * (areaWidth + 2)];
                    if (!(GenLayerShore.isBiomeOceanic(l2) || GenLayerShore.isBiomeOceanic(i1) || GenLayerShore.isBiomeOceanic(j1) || GenLayerShore.isBiomeOceanic(k1))) {
                        if (this.func_151633_d(l2) && this.func_151633_d(i1) && this.func_151633_d(j1) && this.func_151633_d(k1)) {
                            aint1[j2 + i2 * areaWidth] = k2;
                            continue;
                        }
                        aint1[j2 + i2 * areaWidth] = BiomeGenBase.desert.biomeID;
                        continue;
                    }
                    aint1[j2 + i2 * areaWidth] = k2;
                    continue;
                }
                this.func_151632_a(aint, aint1, j2, i2, areaWidth, k2, BiomeGenBase.stoneBeach.biomeID);
            }
        }
        return aint1;
    }

    private void func_151632_a(int[] p_151632_1_, int[] p_151632_2_, int p_151632_3_, int p_151632_4_, int p_151632_5_, int p_151632_6_, int p_151632_7_) {
        if (GenLayerShore.isBiomeOceanic(p_151632_6_)) {
            p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_6_;
        } else {
            int i2 = p_151632_1_[p_151632_3_ + 1 + (p_151632_4_ + 1 - 1) * (p_151632_5_ + 2)];
            int j2 = p_151632_1_[p_151632_3_ + 1 + 1 + (p_151632_4_ + 1) * (p_151632_5_ + 2)];
            int k2 = p_151632_1_[p_151632_3_ + 1 - 1 + (p_151632_4_ + 1) * (p_151632_5_ + 2)];
            int l2 = p_151632_1_[p_151632_3_ + 1 + (p_151632_4_ + 1 + 1) * (p_151632_5_ + 2)];
            p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = !GenLayerShore.isBiomeOceanic(i2) && !GenLayerShore.isBiomeOceanic(j2) && !GenLayerShore.isBiomeOceanic(k2) && !GenLayerShore.isBiomeOceanic(l2) ? p_151632_6_ : p_151632_7_;
        }
    }

    private boolean func_151631_c(int p_151631_1_) {
        return BiomeGenBase.getBiome(p_151631_1_) != null && BiomeGenBase.getBiome(p_151631_1_).getBiomeClass() == BiomeGenJungle.class ? true : p_151631_1_ == BiomeGenBase.jungleEdge.biomeID || p_151631_1_ == BiomeGenBase.jungle.biomeID || p_151631_1_ == BiomeGenBase.jungleHills.biomeID || p_151631_1_ == BiomeGenBase.forest.biomeID || p_151631_1_ == BiomeGenBase.taiga.biomeID || GenLayerShore.isBiomeOceanic(p_151631_1_);
    }

    private boolean func_151633_d(int p_151633_1_) {
        return BiomeGenBase.getBiome(p_151633_1_) instanceof BiomeGenMesa;
    }
}

