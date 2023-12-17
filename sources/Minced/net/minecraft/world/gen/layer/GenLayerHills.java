// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

import org.apache.logging.log4j.LogManager;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.Logger;

public class GenLayerHills extends GenLayer
{
    private static final Logger LOGGER;
    private final GenLayer riverLayer;
    
    public GenLayerHills(final long p_i45479_1_, final GenLayer p_i45479_3_, final GenLayer p_i45479_4_) {
        super(p_i45479_1_);
        this.parent = p_i45479_3_;
        this.riverLayer = p_i45479_4_;
    }
    
    @Override
    public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
        final int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        final int[] aint2 = this.riverLayer.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        final int[] aint3 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i = 0; i < areaHeight; ++i) {
            for (int j = 0; j < areaWidth; ++j) {
                this.initChunkSeed(j + areaX, i + areaY);
                final int k = aint[j + 1 + (i + 1) * (areaWidth + 2)];
                final int l = aint2[j + 1 + (i + 1) * (areaWidth + 2)];
                final boolean flag = (l - 2) % 29 == 0;
                if (k > 255) {
                    GenLayerHills.LOGGER.debug("old! {}", (Object)k);
                }
                final Biome biome = Biome.getBiomeForId(k);
                final boolean flag2 = biome != null && biome.isMutation();
                if (k != 0 && l >= 2 && (l - 2) % 29 == 1 && !flag2) {
                    final Biome biome2 = Biome.getMutationForBiome(biome);
                    aint3[j + i * areaWidth] = ((biome2 == null) ? k : Biome.getIdForBiome(biome2));
                }
                else if (this.nextInt(3) != 0 && !flag) {
                    aint3[j + i * areaWidth] = k;
                }
                else {
                    Biome biome3;
                    if ((biome3 = biome) == Biomes.DESERT) {
                        biome3 = Biomes.DESERT_HILLS;
                    }
                    else if (biome == Biomes.FOREST) {
                        biome3 = Biomes.FOREST_HILLS;
                    }
                    else if (biome == Biomes.BIRCH_FOREST) {
                        biome3 = Biomes.BIRCH_FOREST_HILLS;
                    }
                    else if (biome == Biomes.ROOFED_FOREST) {
                        biome3 = Biomes.PLAINS;
                    }
                    else if (biome == Biomes.TAIGA) {
                        biome3 = Biomes.TAIGA_HILLS;
                    }
                    else if (biome == Biomes.REDWOOD_TAIGA) {
                        biome3 = Biomes.REDWOOD_TAIGA_HILLS;
                    }
                    else if (biome == Biomes.COLD_TAIGA) {
                        biome3 = Biomes.COLD_TAIGA_HILLS;
                    }
                    else if (biome == Biomes.PLAINS) {
                        if (this.nextInt(3) == 0) {
                            biome3 = Biomes.FOREST_HILLS;
                        }
                        else {
                            biome3 = Biomes.FOREST;
                        }
                    }
                    else if (biome == Biomes.ICE_PLAINS) {
                        biome3 = Biomes.ICE_MOUNTAINS;
                    }
                    else if (biome == Biomes.JUNGLE) {
                        biome3 = Biomes.JUNGLE_HILLS;
                    }
                    else if (biome == Biomes.OCEAN) {
                        biome3 = Biomes.DEEP_OCEAN;
                    }
                    else if (biome == Biomes.EXTREME_HILLS) {
                        biome3 = Biomes.EXTREME_HILLS_WITH_TREES;
                    }
                    else if (biome == Biomes.SAVANNA) {
                        biome3 = Biomes.SAVANNA_PLATEAU;
                    }
                    else if (GenLayer.biomesEqualOrMesaPlateau(k, Biome.getIdForBiome(Biomes.MESA_ROCK))) {
                        biome3 = Biomes.MESA;
                    }
                    else if (biome == Biomes.DEEP_OCEAN && this.nextInt(3) == 0) {
                        final int i2 = this.nextInt(2);
                        if (i2 == 0) {
                            biome3 = Biomes.PLAINS;
                        }
                        else {
                            biome3 = Biomes.FOREST;
                        }
                    }
                    int j2 = Biome.getIdForBiome(biome3);
                    if (flag && j2 != k) {
                        final Biome biome4 = Biome.getMutationForBiome(biome3);
                        j2 = ((biome4 == null) ? k : Biome.getIdForBiome(biome4));
                    }
                    if (j2 == k) {
                        aint3[j + i * areaWidth] = k;
                    }
                    else {
                        final int k2 = aint[j + 1 + (i + 0) * (areaWidth + 2)];
                        final int j3 = aint[j + 2 + (i + 1) * (areaWidth + 2)];
                        final int k3 = aint[j + 0 + (i + 1) * (areaWidth + 2)];
                        final int l2 = aint[j + 1 + (i + 2) * (areaWidth + 2)];
                        int i3 = 0;
                        if (GenLayer.biomesEqualOrMesaPlateau(k2, k)) {
                            ++i3;
                        }
                        if (GenLayer.biomesEqualOrMesaPlateau(j3, k)) {
                            ++i3;
                        }
                        if (GenLayer.biomesEqualOrMesaPlateau(k3, k)) {
                            ++i3;
                        }
                        if (GenLayer.biomesEqualOrMesaPlateau(l2, k)) {
                            ++i3;
                        }
                        if (i3 >= 3) {
                            aint3[j + i * areaWidth] = j2;
                        }
                        else {
                            aint3[j + i * areaWidth] = k;
                        }
                    }
                }
            }
        }
        return aint3;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
