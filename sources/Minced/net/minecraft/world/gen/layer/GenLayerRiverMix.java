// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.Biome;
import net.minecraft.init.Biomes;

public class GenLayerRiverMix extends GenLayer
{
    private final GenLayer biomePatternGeneratorChain;
    private final GenLayer riverPatternGeneratorChain;
    
    public GenLayerRiverMix(final long p_i2129_1_, final GenLayer p_i2129_3_, final GenLayer p_i2129_4_) {
        super(p_i2129_1_);
        this.biomePatternGeneratorChain = p_i2129_3_;
        this.riverPatternGeneratorChain = p_i2129_4_;
    }
    
    @Override
    public void initWorldGenSeed(final long seed) {
        this.biomePatternGeneratorChain.initWorldGenSeed(seed);
        this.riverPatternGeneratorChain.initWorldGenSeed(seed);
        super.initWorldGenSeed(seed);
    }
    
    @Override
    public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
        final int[] aint = this.biomePatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
        final int[] aint2 = this.riverPatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
        final int[] aint3 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i = 0; i < areaWidth * areaHeight; ++i) {
            if (aint[i] != Biome.getIdForBiome(Biomes.OCEAN) && aint[i] != Biome.getIdForBiome(Biomes.DEEP_OCEAN)) {
                if (aint2[i] == Biome.getIdForBiome(Biomes.RIVER)) {
                    if (aint[i] == Biome.getIdForBiome(Biomes.ICE_PLAINS)) {
                        aint3[i] = Biome.getIdForBiome(Biomes.FROZEN_RIVER);
                    }
                    else if (aint[i] != Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND) && aint[i] != Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND_SHORE)) {
                        aint3[i] = (aint2[i] & 0xFF);
                    }
                    else {
                        aint3[i] = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND_SHORE);
                    }
                }
                else {
                    aint3[i] = aint[i];
                }
            }
            else {
                aint3[i] = aint[i];
            }
        }
        return aint3;
    }
}
