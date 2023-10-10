/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerDeepOcean
extends GenLayer {
    public GenLayerDeepOcean(long p_i45472_1_, GenLayer p_i45472_3_) {
        super(p_i45472_1_);
        this.parent = p_i45472_3_;
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
                int k1 = aint[j1 + 1 + (i1 + 1 - 1) * (areaWidth + 2)];
                int l1 = aint[j1 + 1 + 1 + (i1 + 1) * (areaWidth + 2)];
                int i22 = aint[j1 + 1 - 1 + (i1 + 1) * (areaWidth + 2)];
                int j22 = aint[j1 + 1 + (i1 + 1 + 1) * (areaWidth + 2)];
                int k22 = aint[j1 + 1 + (i1 + 1) * k2];
                int l22 = 0;
                if (k1 == 0) {
                    ++l22;
                }
                if (l1 == 0) {
                    ++l22;
                }
                if (i22 == 0) {
                    ++l22;
                }
                if (j22 == 0) {
                    ++l22;
                }
                aint1[j1 + i1 * areaWidth] = k22 == 0 && l22 > 3 ? BiomeGenBase.deepOcean.biomeID : k22;
            }
        }
        return aint1;
    }
}

