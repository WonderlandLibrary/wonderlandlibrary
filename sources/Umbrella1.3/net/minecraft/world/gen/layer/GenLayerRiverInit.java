/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerRiverInit
extends GenLayer {
    private static final String __OBFID = "CL_00000565";

    public GenLayerRiverInit(long p_i2127_1_, GenLayer p_i2127_3_) {
        super(p_i2127_1_);
        this.parent = p_i2127_3_;
    }

    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] var5 = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
        int[] var6 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int var7 = 0; var7 < areaHeight; ++var7) {
            for (int var8 = 0; var8 < areaWidth; ++var8) {
                this.initChunkSeed(var8 + areaX, var7 + areaY);
                var6[var8 + var7 * areaWidth] = var5[var8 + var7 * areaWidth] > 0 ? this.nextInt(299999) + 2 : 0;
            }
        }
        return var6;
    }
}

