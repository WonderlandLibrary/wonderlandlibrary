/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerAddMushroomIsland
extends GenLayer {
    public GenLayerAddMushroomIsland(long p_i2120_1_, GenLayer p_i2120_3_) {
        super(p_i2120_1_);
        this.parent = p_i2120_3_;
    }

    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int i2 = areaX - 1;
        int j2 = areaY - 1;
        int k2 = areaWidth + 2;
        int l2 = areaHeight + 2;
        int[] aint = this.parent.getInts(i2, j2, k2, l2);
        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i1 = 0; i1 < areaHeight; ++i1) {
            for (int j1 = 0; j1 < areaWidth; ++j1) {
                int k1 = aint[j1 + 0 + (i1 + 0) * k2];
                int l1 = aint[j1 + 2 + (i1 + 0) * k2];
                int i22 = aint[j1 + 0 + (i1 + 2) * k2];
                int j22 = aint[j1 + 2 + (i1 + 2) * k2];
                int k22 = aint[j1 + 1 + (i1 + 1) * k2];
                this.initChunkSeed(j1 + areaX, i1 + areaY);
                aint1[j1 + i1 * areaWidth] = k22 == 0 && k1 == 0 && l1 == 0 && i22 == 0 && j22 == 0 && this.nextInt(100) == 0 ? BiomeGenBase.mushroomIsland.biomeID : k22;
            }
        }
        return aint1;
    }
}

