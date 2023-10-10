/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerRiverInit
extends GenLayer {
    public GenLayerRiverInit(long p_i2127_1_, GenLayer p_i2127_3_) {
        super(p_i2127_1_);
        this.parent = p_i2127_3_;
    }

    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] aint = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i2 = 0; i2 < areaHeight; ++i2) {
            for (int j2 = 0; j2 < areaWidth; ++j2) {
                this.initChunkSeed(j2 + areaX, i2 + areaY);
                aint1[j2 + i2 * areaWidth] = aint[j2 + i2 * areaWidth] > 0 ? this.nextInt(299999) + 2 : 0;
            }
        }
        return aint1;
    }
}

