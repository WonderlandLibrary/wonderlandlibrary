/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerSmooth
extends GenLayer {
    public GenLayerSmooth(long p_i2131_1_, GenLayer p_i2131_3_) {
        super(p_i2131_1_);
        this.parent = p_i2131_3_;
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
                int k1 = aint[j1 + 0 + (i1 + 1) * k2];
                int l1 = aint[j1 + 2 + (i1 + 1) * k2];
                int i22 = aint[j1 + 1 + (i1 + 0) * k2];
                int j22 = aint[j1 + 1 + (i1 + 2) * k2];
                int k22 = aint[j1 + 1 + (i1 + 1) * k2];
                if (k1 == l1 && i22 == j22) {
                    this.initChunkSeed(j1 + areaX, i1 + areaY);
                    k22 = this.nextInt(2) == 0 ? k1 : i22;
                } else {
                    if (k1 == l1) {
                        k22 = k1;
                    }
                    if (i22 == j22) {
                        k22 = i22;
                    }
                }
                aint1[j1 + i1 * areaWidth] = k22;
            }
        }
        return aint1;
    }
}

