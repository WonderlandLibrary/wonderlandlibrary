/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerRiver
extends GenLayer {
    public GenLayerRiver(long p_i2128_1_, GenLayer p_i2128_3_) {
        super(p_i2128_1_);
        this.parent = p_i2128_3_;
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
                int k1 = this.func_151630_c(aint[j1 + 0 + (i1 + 1) * k2]);
                int l1 = this.func_151630_c(aint[j1 + 2 + (i1 + 1) * k2]);
                int i22 = this.func_151630_c(aint[j1 + 1 + (i1 + 0) * k2]);
                int j22 = this.func_151630_c(aint[j1 + 1 + (i1 + 2) * k2]);
                int k22 = this.func_151630_c(aint[j1 + 1 + (i1 + 1) * k2]);
                aint1[j1 + i1 * areaWidth] = k22 == k1 && k22 == i22 && k22 == l1 && k22 == j22 ? -1 : BiomeGenBase.river.biomeID;
            }
        }
        return aint1;
    }

    private int func_151630_c(int p_151630_1_) {
        return p_151630_1_ >= 2 ? 2 + (p_151630_1_ & 1) : p_151630_1_;
    }
}

