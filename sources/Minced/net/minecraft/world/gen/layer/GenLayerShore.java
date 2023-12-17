// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeMesa;
import net.minecraft.world.biome.BiomeJungle;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

public class GenLayerShore extends GenLayer
{
    public GenLayerShore(final long p_i2130_1_, final GenLayer p_i2130_3_) {
        super(p_i2130_1_);
        this.parent = p_i2130_3_;
    }
    
    @Override
    public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
        final int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        final int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i = 0; i < areaHeight; ++i) {
            for (int j = 0; j < areaWidth; ++j) {
                this.initChunkSeed(j + areaX, i + areaY);
                final int k = aint[j + 1 + (i + 1) * (areaWidth + 2)];
                final Biome biome = Biome.getBiome(k);
                if (k == Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND)) {
                    final int j2 = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
                    final int i2 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
                    final int l3 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
                    final int k2 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
                    if (j2 != Biome.getIdForBiome(Biomes.OCEAN) && i2 != Biome.getIdForBiome(Biomes.OCEAN) && l3 != Biome.getIdForBiome(Biomes.OCEAN) && k2 != Biome.getIdForBiome(Biomes.OCEAN)) {
                        aint2[j + i * areaWidth] = k;
                    }
                    else {
                        aint2[j + i * areaWidth] = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND_SHORE);
                    }
                }
                else if (biome != null && biome.getBiomeClass() == BiomeJungle.class) {
                    final int i3 = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
                    final int l4 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
                    final int k3 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
                    final int j3 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
                    if (this.isJungleCompatible(i3) && this.isJungleCompatible(l4) && this.isJungleCompatible(k3) && this.isJungleCompatible(j3)) {
                        if (!GenLayer.isBiomeOceanic(i3) && !GenLayer.isBiomeOceanic(l4) && !GenLayer.isBiomeOceanic(k3) && !GenLayer.isBiomeOceanic(j3)) {
                            aint2[j + i * areaWidth] = k;
                        }
                        else {
                            aint2[j + i * areaWidth] = Biome.getIdForBiome(Biomes.BEACH);
                        }
                    }
                    else {
                        aint2[j + i * areaWidth] = Biome.getIdForBiome(Biomes.JUNGLE_EDGE);
                    }
                }
                else if (k != Biome.getIdForBiome(Biomes.EXTREME_HILLS) && k != Biome.getIdForBiome(Biomes.EXTREME_HILLS_WITH_TREES) && k != Biome.getIdForBiome(Biomes.EXTREME_HILLS_EDGE)) {
                    if (biome != null && biome.isSnowyBiome()) {
                        this.replaceIfNeighborOcean(aint, aint2, j, i, areaWidth, k, Biome.getIdForBiome(Biomes.COLD_BEACH));
                    }
                    else if (k != Biome.getIdForBiome(Biomes.MESA) && k != Biome.getIdForBiome(Biomes.MESA_ROCK)) {
                        if (k != Biome.getIdForBiome(Biomes.OCEAN) && k != Biome.getIdForBiome(Biomes.DEEP_OCEAN) && k != Biome.getIdForBiome(Biomes.RIVER) && k != Biome.getIdForBiome(Biomes.SWAMPLAND)) {
                            final int l5 = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
                            final int k4 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
                            final int j4 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
                            final int i4 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
                            if (!GenLayer.isBiomeOceanic(l5) && !GenLayer.isBiomeOceanic(k4) && !GenLayer.isBiomeOceanic(j4) && !GenLayer.isBiomeOceanic(i4)) {
                                aint2[j + i * areaWidth] = k;
                            }
                            else {
                                aint2[j + i * areaWidth] = Biome.getIdForBiome(Biomes.BEACH);
                            }
                        }
                        else {
                            aint2[j + i * areaWidth] = k;
                        }
                    }
                    else {
                        final int m = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
                        final int i5 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
                        final int j5 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
                        final int k5 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
                        if (!GenLayer.isBiomeOceanic(m) && !GenLayer.isBiomeOceanic(i5) && !GenLayer.isBiomeOceanic(j5) && !GenLayer.isBiomeOceanic(k5)) {
                            if (this.isMesa(m) && this.isMesa(i5) && this.isMesa(j5) && this.isMesa(k5)) {
                                aint2[j + i * areaWidth] = k;
                            }
                            else {
                                aint2[j + i * areaWidth] = Biome.getIdForBiome(Biomes.DESERT);
                            }
                        }
                        else {
                            aint2[j + i * areaWidth] = k;
                        }
                    }
                }
                else {
                    this.replaceIfNeighborOcean(aint, aint2, j, i, areaWidth, k, Biome.getIdForBiome(Biomes.STONE_BEACH));
                }
            }
        }
        return aint2;
    }
    
    private void replaceIfNeighborOcean(final int[] p_151632_1_, final int[] p_151632_2_, final int p_151632_3_, final int p_151632_4_, final int p_151632_5_, final int p_151632_6_, final int p_151632_7_) {
        if (GenLayer.isBiomeOceanic(p_151632_6_)) {
            p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_6_;
        }
        else {
            final int i = p_151632_1_[p_151632_3_ + 1 + (p_151632_4_ + 1 - 1) * (p_151632_5_ + 2)];
            final int j = p_151632_1_[p_151632_3_ + 1 + 1 + (p_151632_4_ + 1) * (p_151632_5_ + 2)];
            final int k = p_151632_1_[p_151632_3_ + 1 - 1 + (p_151632_4_ + 1) * (p_151632_5_ + 2)];
            final int l = p_151632_1_[p_151632_3_ + 1 + (p_151632_4_ + 1 + 1) * (p_151632_5_ + 2)];
            if (!GenLayer.isBiomeOceanic(i) && !GenLayer.isBiomeOceanic(j) && !GenLayer.isBiomeOceanic(k) && !GenLayer.isBiomeOceanic(l)) {
                p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_6_;
            }
            else {
                p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_7_;
            }
        }
    }
    
    private boolean isJungleCompatible(final int p_151631_1_) {
        return (Biome.getBiome(p_151631_1_) != null && Biome.getBiome(p_151631_1_).getBiomeClass() == BiomeJungle.class) || p_151631_1_ == Biome.getIdForBiome(Biomes.JUNGLE_EDGE) || p_151631_1_ == Biome.getIdForBiome(Biomes.JUNGLE) || p_151631_1_ == Biome.getIdForBiome(Biomes.JUNGLE_HILLS) || p_151631_1_ == Biome.getIdForBiome(Biomes.FOREST) || p_151631_1_ == Biome.getIdForBiome(Biomes.TAIGA) || GenLayer.isBiomeOceanic(p_151631_1_);
    }
    
    private boolean isMesa(final int p_151633_1_) {
        return Biome.getBiome(p_151633_1_) instanceof BiomeMesa;
    }
}
