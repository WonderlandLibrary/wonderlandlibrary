/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerZoom
extends GenLayer {
    public GenLayerZoom(long p_i2134_1_, GenLayer p_i2134_3_) {
        super(p_i2134_1_);
        this.parent = p_i2134_3_;
    }

    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int i2 = areaX >> 1;
        int j2 = areaY >> 1;
        int k2 = (areaWidth >> 1) + 2;
        int l2 = (areaHeight >> 1) + 2;
        int[] aint = this.parent.getInts(i2, j2, k2, l2);
        int i1 = k2 - 1 << 1;
        int j1 = l2 - 1 << 1;
        int[] aint1 = IntCache.getIntCache(i1 * j1);
        for (int k1 = 0; k1 < l2 - 1; ++k1) {
            int i22;
            int l1 = (k1 << 1) * i1;
            int j22 = aint[i22 + 0 + (k1 + 0) * k2];
            int k22 = aint[i22 + 0 + (k1 + 1) * k2];
            for (i22 = 0; i22 < k2 - 1; ++i22) {
                this.initChunkSeed(i22 + i2 << 1, k1 + j2 << 1);
                int l22 = aint[i22 + 1 + (k1 + 0) * k2];
                int i3 = aint[i22 + 1 + (k1 + 1) * k2];
                aint1[l1] = j22;
                aint1[l1++ + i1] = this.selectRandom(j22, k22);
                aint1[l1] = this.selectRandom(j22, l22);
                aint1[l1++ + i1] = this.selectModeOrRandom(j22, l22, k22, i3);
                j22 = l22;
                k22 = i3;
            }
        }
        int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int j3 = 0; j3 < areaHeight; ++j3) {
            System.arraycopy(aint1, (j3 + (areaY & 1)) * i1 + (areaX & 1), aint2, j3 * areaWidth, areaWidth);
        }
        return aint2;
    }

    public static GenLayer magnify(long p_75915_0_, GenLayer p_75915_2_, int p_75915_3_) {
        GenLayer genlayer = p_75915_2_;
        for (int i2 = 0; i2 < p_75915_3_; ++i2) {
            genlayer = new GenLayerZoom(p_75915_0_ + (long)i2, genlayer);
        }
        return genlayer;
    }
}

