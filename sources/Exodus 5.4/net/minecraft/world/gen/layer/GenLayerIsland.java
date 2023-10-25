/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerIsland
extends GenLayer {
    @Override
    public int[] getInts(int n, int n2, int n3, int n4) {
        int[] nArray = IntCache.getIntCache(n3 * n4);
        int n5 = 0;
        while (n5 < n4) {
            int n6 = 0;
            while (n6 < n3) {
                this.initChunkSeed(n + n6, n2 + n5);
                nArray[n6 + n5 * n3] = this.nextInt(10) == 0 ? 1 : 0;
                ++n6;
            }
            ++n5;
        }
        if (n > -n3 && n <= 0 && n2 > -n4 && n2 <= 0) {
            nArray[-n + -n2 * n3] = 1;
        }
        return nArray;
    }

    public GenLayerIsland(long l) {
        super(l);
    }
}

