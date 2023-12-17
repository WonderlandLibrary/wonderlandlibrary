// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.biome.Biome;

public class GenLayerBiome extends GenLayer
{
    private Biome[] warmBiomes;
    private final Biome[] mediumBiomes;
    private final Biome[] coldBiomes;
    private final Biome[] iceBiomes;
    private final ChunkGeneratorSettings settings;
    
    public GenLayerBiome(final long p_i45560_1_, final GenLayer p_i45560_3_, final WorldType p_i45560_4_, final ChunkGeneratorSettings p_i45560_5_) {
        super(p_i45560_1_);
        this.warmBiomes = new Biome[] { Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.SAVANNA, Biomes.SAVANNA, Biomes.PLAINS };
        this.mediumBiomes = new Biome[] { Biomes.FOREST, Biomes.ROOFED_FOREST, Biomes.EXTREME_HILLS, Biomes.PLAINS, Biomes.BIRCH_FOREST, Biomes.SWAMPLAND };
        this.coldBiomes = new Biome[] { Biomes.FOREST, Biomes.EXTREME_HILLS, Biomes.TAIGA, Biomes.PLAINS };
        this.iceBiomes = new Biome[] { Biomes.ICE_PLAINS, Biomes.ICE_PLAINS, Biomes.ICE_PLAINS, Biomes.COLD_TAIGA };
        this.parent = p_i45560_3_;
        if (p_i45560_4_ == WorldType.DEFAULT_1_1) {
            this.warmBiomes = new Biome[] { Biomes.DESERT, Biomes.FOREST, Biomes.EXTREME_HILLS, Biomes.SWAMPLAND, Biomes.PLAINS, Biomes.TAIGA };
            this.settings = null;
        }
        else {
            this.settings = p_i45560_5_;
        }
    }
    
    @Override
    public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
        final int[] aint = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
        final int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i = 0; i < areaHeight; ++i) {
            for (int j = 0; j < areaWidth; ++j) {
                this.initChunkSeed(j + areaX, i + areaY);
                int k = aint[j + i * areaWidth];
                final int l = (k & 0xF00) >> 8;
                k &= 0xFFFFF0FF;
                if (this.settings != null && this.settings.fixedBiome >= 0) {
                    aint2[j + i * areaWidth] = this.settings.fixedBiome;
                }
                else if (GenLayer.isBiomeOceanic(k)) {
                    aint2[j + i * areaWidth] = k;
                }
                else if (k == Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND)) {
                    aint2[j + i * areaWidth] = k;
                }
                else if (k == 1) {
                    if (l > 0) {
                        if (this.nextInt(3) == 0) {
                            aint2[j + i * areaWidth] = Biome.getIdForBiome(Biomes.MESA_CLEAR_ROCK);
                        }
                        else {
                            aint2[j + i * areaWidth] = Biome.getIdForBiome(Biomes.MESA_ROCK);
                        }
                    }
                    else {
                        aint2[j + i * areaWidth] = Biome.getIdForBiome(this.warmBiomes[this.nextInt(this.warmBiomes.length)]);
                    }
                }
                else if (k == 2) {
                    if (l > 0) {
                        aint2[j + i * areaWidth] = Biome.getIdForBiome(Biomes.JUNGLE);
                    }
                    else {
                        aint2[j + i * areaWidth] = Biome.getIdForBiome(this.mediumBiomes[this.nextInt(this.mediumBiomes.length)]);
                    }
                }
                else if (k == 3) {
                    if (l > 0) {
                        aint2[j + i * areaWidth] = Biome.getIdForBiome(Biomes.REDWOOD_TAIGA);
                    }
                    else {
                        aint2[j + i * areaWidth] = Biome.getIdForBiome(this.coldBiomes[this.nextInt(this.coldBiomes.length)]);
                    }
                }
                else if (k == 4) {
                    aint2[j + i * areaWidth] = Biome.getIdForBiome(this.iceBiomes[this.nextInt(this.iceBiomes.length)]);
                }
                else {
                    aint2[j + i * areaWidth] = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND);
                }
            }
        }
        return aint2;
    }
}
