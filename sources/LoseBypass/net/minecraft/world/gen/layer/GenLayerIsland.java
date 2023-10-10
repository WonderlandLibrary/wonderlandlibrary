/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerIsland
extends GenLayer {
    public GenLayerIsland(long p_i2124_1_) {
        super(p_i2124_1_);
    }

    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] aint = IntCache.getIntCache(areaWidth * areaHeight);
        int i = 0;
        while (true) {
            if (i >= areaHeight) {
                if (areaX <= -areaWidth) return aint;
                if (areaX > 0) return aint;
                if (areaY <= -areaHeight) return aint;
                if (areaY > 0) return aint;
                aint[-areaX + -areaY * areaWidth] = 1;
                return aint;
            }
            for (int j = 0; j < areaWidth; ++j) {
                this.initChunkSeed(areaX + j, areaY + i);
                aint[j + i * areaWidth] = this.nextInt(10) == 0 ? 1 : 0;
            }
            ++i;
        }
    }
}

