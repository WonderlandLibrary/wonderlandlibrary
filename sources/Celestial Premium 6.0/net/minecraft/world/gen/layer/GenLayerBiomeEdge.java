/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerBiomeEdge
extends GenLayer {
    public GenLayerBiomeEdge(long p_i45475_1_, GenLayer p_i45475_3_) {
        super(p_i45475_1_);
        this.parent = p_i45475_3_;
    }

    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i = 0; i < areaHeight; ++i) {
            for (int j = 0; j < areaWidth; ++j) {
                this.initChunkSeed(j + areaX, i + areaY);
                int k = aint[j + 1 + (i + 1) * (areaWidth + 2)];
                if (this.replaceBiomeEdgeIfNecessary(aint, aint1, j, i, areaWidth, k, Biome.getIdForBiome(Biomes.EXTREME_HILLS), Biome.getIdForBiome(Biomes.EXTREME_HILLS_EDGE)) || this.replaceBiomeEdge(aint, aint1, j, i, areaWidth, k, Biome.getIdForBiome(Biomes.MESA_ROCK), Biome.getIdForBiome(Biomes.MESA)) || this.replaceBiomeEdge(aint, aint1, j, i, areaWidth, k, Biome.getIdForBiome(Biomes.MESA_CLEAR_ROCK), Biome.getIdForBiome(Biomes.MESA)) || this.replaceBiomeEdge(aint, aint1, j, i, areaWidth, k, Biome.getIdForBiome(Biomes.REDWOOD_TAIGA), Biome.getIdForBiome(Biomes.TAIGA))) continue;
                if (k == Biome.getIdForBiome(Biomes.DESERT)) {
                    int l1 = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
                    int i2 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
                    int j2 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
                    int k2 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
                    if (l1 != Biome.getIdForBiome(Biomes.ICE_PLAINS) && i2 != Biome.getIdForBiome(Biomes.ICE_PLAINS) && j2 != Biome.getIdForBiome(Biomes.ICE_PLAINS) && k2 != Biome.getIdForBiome(Biomes.ICE_PLAINS)) {
                        aint1[j + i * areaWidth] = k;
                        continue;
                    }
                    aint1[j + i * areaWidth] = Biome.getIdForBiome(Biomes.EXTREME_HILLS_WITH_TREES);
                    continue;
                }
                if (k == Biome.getIdForBiome(Biomes.SWAMPLAND)) {
                    int l = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
                    int i1 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
                    int j1 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
                    int k1 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
                    if (l != Biome.getIdForBiome(Biomes.DESERT) && i1 != Biome.getIdForBiome(Biomes.DESERT) && j1 != Biome.getIdForBiome(Biomes.DESERT) && k1 != Biome.getIdForBiome(Biomes.DESERT) && l != Biome.getIdForBiome(Biomes.COLD_TAIGA) && i1 != Biome.getIdForBiome(Biomes.COLD_TAIGA) && j1 != Biome.getIdForBiome(Biomes.COLD_TAIGA) && k1 != Biome.getIdForBiome(Biomes.COLD_TAIGA) && l != Biome.getIdForBiome(Biomes.ICE_PLAINS) && i1 != Biome.getIdForBiome(Biomes.ICE_PLAINS) && j1 != Biome.getIdForBiome(Biomes.ICE_PLAINS) && k1 != Biome.getIdForBiome(Biomes.ICE_PLAINS)) {
                        if (l != Biome.getIdForBiome(Biomes.JUNGLE) && k1 != Biome.getIdForBiome(Biomes.JUNGLE) && i1 != Biome.getIdForBiome(Biomes.JUNGLE) && j1 != Biome.getIdForBiome(Biomes.JUNGLE)) {
                            aint1[j + i * areaWidth] = k;
                            continue;
                        }
                        aint1[j + i * areaWidth] = Biome.getIdForBiome(Biomes.JUNGLE_EDGE);
                        continue;
                    }
                    aint1[j + i * areaWidth] = Biome.getIdForBiome(Biomes.PLAINS);
                    continue;
                }
                aint1[j + i * areaWidth] = k;
            }
        }
        return aint1;
    }

    private boolean replaceBiomeEdgeIfNecessary(int[] p_151636_1_, int[] p_151636_2_, int p_151636_3_, int p_151636_4_, int p_151636_5_, int p_151636_6_, int p_151636_7_, int p_151636_8_) {
        if (!GenLayerBiomeEdge.biomesEqualOrMesaPlateau(p_151636_6_, p_151636_7_)) {
            return false;
        }
        int i = p_151636_1_[p_151636_3_ + 1 + (p_151636_4_ + 1 - 1) * (p_151636_5_ + 2)];
        int j = p_151636_1_[p_151636_3_ + 1 + 1 + (p_151636_4_ + 1) * (p_151636_5_ + 2)];
        int k = p_151636_1_[p_151636_3_ + 1 - 1 + (p_151636_4_ + 1) * (p_151636_5_ + 2)];
        int l = p_151636_1_[p_151636_3_ + 1 + (p_151636_4_ + 1 + 1) * (p_151636_5_ + 2)];
        p_151636_2_[p_151636_3_ + p_151636_4_ * p_151636_5_] = this.canBiomesBeNeighbors(i, p_151636_7_) && this.canBiomesBeNeighbors(j, p_151636_7_) && this.canBiomesBeNeighbors(k, p_151636_7_) && this.canBiomesBeNeighbors(l, p_151636_7_) ? p_151636_6_ : p_151636_8_;
        return true;
    }

    private boolean replaceBiomeEdge(int[] p_151635_1_, int[] p_151635_2_, int p_151635_3_, int p_151635_4_, int p_151635_5_, int p_151635_6_, int p_151635_7_, int p_151635_8_) {
        if (p_151635_6_ != p_151635_7_) {
            return false;
        }
        int i = p_151635_1_[p_151635_3_ + 1 + (p_151635_4_ + 1 - 1) * (p_151635_5_ + 2)];
        int j = p_151635_1_[p_151635_3_ + 1 + 1 + (p_151635_4_ + 1) * (p_151635_5_ + 2)];
        int k = p_151635_1_[p_151635_3_ + 1 - 1 + (p_151635_4_ + 1) * (p_151635_5_ + 2)];
        int l = p_151635_1_[p_151635_3_ + 1 + (p_151635_4_ + 1 + 1) * (p_151635_5_ + 2)];
        p_151635_2_[p_151635_3_ + p_151635_4_ * p_151635_5_] = GenLayerBiomeEdge.biomesEqualOrMesaPlateau(i, p_151635_7_) && GenLayerBiomeEdge.biomesEqualOrMesaPlateau(j, p_151635_7_) && GenLayerBiomeEdge.biomesEqualOrMesaPlateau(k, p_151635_7_) && GenLayerBiomeEdge.biomesEqualOrMesaPlateau(l, p_151635_7_) ? p_151635_6_ : p_151635_8_;
        return true;
    }

    private boolean canBiomesBeNeighbors(int p_151634_1_, int p_151634_2_) {
        if (GenLayerBiomeEdge.biomesEqualOrMesaPlateau(p_151634_1_, p_151634_2_)) {
            return true;
        }
        Biome biome = Biome.getBiome(p_151634_1_);
        Biome biome1 = Biome.getBiome(p_151634_2_);
        if (biome != null && biome1 != null) {
            Biome.TempCategory biome$tempcategory1;
            Biome.TempCategory biome$tempcategory = biome.getTempCategory();
            return biome$tempcategory == (biome$tempcategory1 = biome1.getTempCategory()) || biome$tempcategory == Biome.TempCategory.MEDIUM || biome$tempcategory1 == Biome.TempCategory.MEDIUM;
        }
        return false;
    }
}

