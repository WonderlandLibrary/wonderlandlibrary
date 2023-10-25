/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GenLayerHills
extends GenLayer {
    private static final Logger LOGGER = LogManager.getLogger();
    private final GenLayer riverLayer;

    public GenLayerHills(long p_i45479_1_, GenLayer p_i45479_3_, GenLayer p_i45479_4_) {
        super(p_i45479_1_);
        this.parent = p_i45479_3_;
        this.riverLayer = p_i45479_4_;
    }

    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        int[] aint1 = this.riverLayer.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i = 0; i < areaHeight; ++i) {
            for (int j = 0; j < areaWidth; ++j) {
                Biome biome;
                boolean flag1;
                boolean flag;
                this.initChunkSeed(j + areaX, i + areaY);
                int k = aint[j + 1 + (i + 1) * (areaWidth + 2)];
                int l = aint1[j + 1 + (i + 1) * (areaWidth + 2)];
                boolean bl = flag = (l - 2) % 29 == 0;
                if (k > 255) {
                    LOGGER.debug("old! {}", k);
                }
                boolean bl2 = flag1 = (biome = Biome.getBiomeForId(k)) != null && biome.isMutation();
                if (k != 0 && l >= 2 && (l - 2) % 29 == 1 && !flag1) {
                    Biome biome3 = Biome.getMutationForBiome(biome);
                    aint2[j + i * areaWidth] = biome3 == null ? k : Biome.getIdForBiome(biome3);
                    continue;
                }
                if (this.nextInt(3) != 0 && !flag) {
                    aint2[j + i * areaWidth] = k;
                    continue;
                }
                Biome biome1 = biome;
                if (biome == Biomes.DESERT) {
                    biome1 = Biomes.DESERT_HILLS;
                } else if (biome == Biomes.FOREST) {
                    biome1 = Biomes.FOREST_HILLS;
                } else if (biome == Biomes.BIRCH_FOREST) {
                    biome1 = Biomes.BIRCH_FOREST_HILLS;
                } else if (biome == Biomes.ROOFED_FOREST) {
                    biome1 = Biomes.PLAINS;
                } else if (biome == Biomes.TAIGA) {
                    biome1 = Biomes.TAIGA_HILLS;
                } else if (biome == Biomes.REDWOOD_TAIGA) {
                    biome1 = Biomes.REDWOOD_TAIGA_HILLS;
                } else if (biome == Biomes.COLD_TAIGA) {
                    biome1 = Biomes.COLD_TAIGA_HILLS;
                } else if (biome == Biomes.PLAINS) {
                    biome1 = this.nextInt(3) == 0 ? Biomes.FOREST_HILLS : Biomes.FOREST;
                } else if (biome == Biomes.ICE_PLAINS) {
                    biome1 = Biomes.ICE_MOUNTAINS;
                } else if (biome == Biomes.JUNGLE) {
                    biome1 = Biomes.JUNGLE_HILLS;
                } else if (biome == Biomes.OCEAN) {
                    biome1 = Biomes.DEEP_OCEAN;
                } else if (biome == Biomes.EXTREME_HILLS) {
                    biome1 = Biomes.EXTREME_HILLS_WITH_TREES;
                } else if (biome == Biomes.SAVANNA) {
                    biome1 = Biomes.SAVANNA_PLATEAU;
                } else if (GenLayerHills.biomesEqualOrMesaPlateau(k, Biome.getIdForBiome(Biomes.MESA_ROCK))) {
                    biome1 = Biomes.MESA;
                } else if (biome == Biomes.DEEP_OCEAN && this.nextInt(3) == 0) {
                    int i1 = this.nextInt(2);
                    biome1 = i1 == 0 ? Biomes.PLAINS : Biomes.FOREST;
                }
                int j2 = Biome.getIdForBiome(biome1);
                if (flag && j2 != k) {
                    Biome biome2 = Biome.getMutationForBiome(biome1);
                    int n = j2 = biome2 == null ? k : Biome.getIdForBiome(biome2);
                }
                if (j2 == k) {
                    aint2[j + i * areaWidth] = k;
                    continue;
                }
                int k2 = aint[j + 1 + (i + 0) * (areaWidth + 2)];
                int j1 = aint[j + 2 + (i + 1) * (areaWidth + 2)];
                int k1 = aint[j + 0 + (i + 1) * (areaWidth + 2)];
                int l1 = aint[j + 1 + (i + 2) * (areaWidth + 2)];
                int i2 = 0;
                if (GenLayerHills.biomesEqualOrMesaPlateau(k2, k)) {
                    ++i2;
                }
                if (GenLayerHills.biomesEqualOrMesaPlateau(j1, k)) {
                    ++i2;
                }
                if (GenLayerHills.biomesEqualOrMesaPlateau(k1, k)) {
                    ++i2;
                }
                if (GenLayerHills.biomesEqualOrMesaPlateau(l1, k)) {
                    ++i2;
                }
                aint2[j + i * areaWidth] = i2 >= 3 ? j2 : k;
            }
        }
        return aint2;
    }
}

