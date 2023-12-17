// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.Biome;
import net.minecraft.init.Biomes;

public class GenLayerBiomeEdge extends GenLayer
{
    public GenLayerBiomeEdge(final long p_i45475_1_, final GenLayer p_i45475_3_) {
        super(p_i45475_1_);
        this.parent = p_i45475_3_;
    }
    
    @Override
    public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
        final int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        final int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i = 0; i < areaHeight; ++i) {
            for (int j = 0; j < areaWidth; ++j) {
                this.initChunkSeed(j + areaX, i + areaY);
                final int k = aint[j + 1 + (i + 1) * (areaWidth + 2)];
                if (!this.replaceBiomeEdgeIfNecessary(aint, aint2, j, i, areaWidth, k, Biome.getIdForBiome(Biomes.EXTREME_HILLS), Biome.getIdForBiome(Biomes.EXTREME_HILLS_EDGE)) && !this.replaceBiomeEdge(aint, aint2, j, i, areaWidth, k, Biome.getIdForBiome(Biomes.MESA_ROCK), Biome.getIdForBiome(Biomes.MESA)) && !this.replaceBiomeEdge(aint, aint2, j, i, areaWidth, k, Biome.getIdForBiome(Biomes.MESA_CLEAR_ROCK), Biome.getIdForBiome(Biomes.MESA)) && !this.replaceBiomeEdge(aint, aint2, j, i, areaWidth, k, Biome.getIdForBiome(Biomes.REDWOOD_TAIGA), Biome.getIdForBiome(Biomes.TAIGA))) {
                    if (k == Biome.getIdForBiome(Biomes.DESERT)) {
                        final int l1 = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
                        final int i2 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
                        final int j2 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
                        final int k2 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
                        if (l1 != Biome.getIdForBiome(Biomes.ICE_PLAINS) && i2 != Biome.getIdForBiome(Biomes.ICE_PLAINS) && j2 != Biome.getIdForBiome(Biomes.ICE_PLAINS) && k2 != Biome.getIdForBiome(Biomes.ICE_PLAINS)) {
                            aint2[j + i * areaWidth] = k;
                        }
                        else {
                            aint2[j + i * areaWidth] = Biome.getIdForBiome(Biomes.EXTREME_HILLS_WITH_TREES);
                        }
                    }
                    else if (k == Biome.getIdForBiome(Biomes.SWAMPLAND)) {
                        final int m = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
                        final int i3 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
                        final int j3 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
                        final int k3 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
                        if (m != Biome.getIdForBiome(Biomes.DESERT) && i3 != Biome.getIdForBiome(Biomes.DESERT) && j3 != Biome.getIdForBiome(Biomes.DESERT) && k3 != Biome.getIdForBiome(Biomes.DESERT) && m != Biome.getIdForBiome(Biomes.COLD_TAIGA) && i3 != Biome.getIdForBiome(Biomes.COLD_TAIGA) && j3 != Biome.getIdForBiome(Biomes.COLD_TAIGA) && k3 != Biome.getIdForBiome(Biomes.COLD_TAIGA) && m != Biome.getIdForBiome(Biomes.ICE_PLAINS) && i3 != Biome.getIdForBiome(Biomes.ICE_PLAINS) && j3 != Biome.getIdForBiome(Biomes.ICE_PLAINS) && k3 != Biome.getIdForBiome(Biomes.ICE_PLAINS)) {
                            if (m != Biome.getIdForBiome(Biomes.JUNGLE) && k3 != Biome.getIdForBiome(Biomes.JUNGLE) && i3 != Biome.getIdForBiome(Biomes.JUNGLE) && j3 != Biome.getIdForBiome(Biomes.JUNGLE)) {
                                aint2[j + i * areaWidth] = k;
                            }
                            else {
                                aint2[j + i * areaWidth] = Biome.getIdForBiome(Biomes.JUNGLE_EDGE);
                            }
                        }
                        else {
                            aint2[j + i * areaWidth] = Biome.getIdForBiome(Biomes.PLAINS);
                        }
                    }
                    else {
                        aint2[j + i * areaWidth] = k;
                    }
                }
            }
        }
        return aint2;
    }
    
    private boolean replaceBiomeEdgeIfNecessary(final int[] p_151636_1_, final int[] p_151636_2_, final int p_151636_3_, final int p_151636_4_, final int p_151636_5_, final int p_151636_6_, final int p_151636_7_, final int p_151636_8_) {
        if (!GenLayer.biomesEqualOrMesaPlateau(p_151636_6_, p_151636_7_)) {
            return false;
        }
        final int i = p_151636_1_[p_151636_3_ + 1 + (p_151636_4_ + 1 - 1) * (p_151636_5_ + 2)];
        final int j = p_151636_1_[p_151636_3_ + 1 + 1 + (p_151636_4_ + 1) * (p_151636_5_ + 2)];
        final int k = p_151636_1_[p_151636_3_ + 1 - 1 + (p_151636_4_ + 1) * (p_151636_5_ + 2)];
        final int l = p_151636_1_[p_151636_3_ + 1 + (p_151636_4_ + 1 + 1) * (p_151636_5_ + 2)];
        if (this.canBiomesBeNeighbors(i, p_151636_7_) && this.canBiomesBeNeighbors(j, p_151636_7_) && this.canBiomesBeNeighbors(k, p_151636_7_) && this.canBiomesBeNeighbors(l, p_151636_7_)) {
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
        final int i = p_151635_1_[p_151635_3_ + 1 + (p_151635_4_ + 1 - 1) * (p_151635_5_ + 2)];
        final int j = p_151635_1_[p_151635_3_ + 1 + 1 + (p_151635_4_ + 1) * (p_151635_5_ + 2)];
        final int k = p_151635_1_[p_151635_3_ + 1 - 1 + (p_151635_4_ + 1) * (p_151635_5_ + 2)];
        final int l = p_151635_1_[p_151635_3_ + 1 + (p_151635_4_ + 1 + 1) * (p_151635_5_ + 2)];
        if (GenLayer.biomesEqualOrMesaPlateau(i, p_151635_7_) && GenLayer.biomesEqualOrMesaPlateau(j, p_151635_7_) && GenLayer.biomesEqualOrMesaPlateau(k, p_151635_7_) && GenLayer.biomesEqualOrMesaPlateau(l, p_151635_7_)) {
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
        final Biome biome = Biome.getBiome(p_151634_1_);
        final Biome biome2 = Biome.getBiome(p_151634_2_);
        if (biome != null && biome2 != null) {
            final Biome.TempCategory biome$tempcategory = biome.getTempCategory();
            final Biome.TempCategory biome$tempcategory2 = biome2.getTempCategory();
            return biome$tempcategory == biome$tempcategory2 || biome$tempcategory == Biome.TempCategory.MEDIUM || biome$tempcategory2 == Biome.TempCategory.MEDIUM;
        }
        return false;
    }
}
