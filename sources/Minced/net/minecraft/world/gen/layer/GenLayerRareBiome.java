// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.Biome;
import net.minecraft.init.Biomes;

public class GenLayerRareBiome extends GenLayer
{
    public GenLayerRareBiome(final long p_i45478_1_, final GenLayer p_i45478_3_) {
        super(p_i45478_1_);
        this.parent = p_i45478_3_;
    }
    
    @Override
    public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
        final int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        final int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i = 0; i < areaHeight; ++i) {
            for (int j = 0; j < areaWidth; ++j) {
                this.initChunkSeed(j + areaX, i + areaY);
                final int k = aint[j + 1 + (i + 1) * (areaWidth + 2)];
                if (this.nextInt(57) == 0) {
                    if (k == Biome.getIdForBiome(Biomes.PLAINS)) {
                        aint2[j + i * areaWidth] = Biome.getIdForBiome(Biomes.MUTATED_PLAINS);
                    }
                    else {
                        aint2[j + i * areaWidth] = k;
                    }
                }
                else {
                    aint2[j + i * areaWidth] = k;
                }
            }
        }
        return aint2;
    }
}
