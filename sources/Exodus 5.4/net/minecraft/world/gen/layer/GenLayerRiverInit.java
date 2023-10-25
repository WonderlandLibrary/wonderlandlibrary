/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerRiverInit
extends GenLayer {
    @Override
    public int[] getInts(int n, int n2, int n3, int n4) {
        int[] nArray = this.parent.getInts(n, n2, n3, n4);
        int[] nArray2 = IntCache.getIntCache(n3 * n4);
        int n5 = 0;
        while (n5 < n4) {
            int n6 = 0;
            while (n6 < n3) {
                this.initChunkSeed(n6 + n, n5 + n2);
                nArray2[n6 + n5 * n3] = nArray[n6 + n5 * n3] > 0 ? this.nextInt(299999) + 2 : 0;
                ++n6;
            }
            ++n5;
        }
        return nArray2;
    }

    public GenLayerRiverInit(long l, GenLayer genLayer) {
        super(l);
        this.parent = genLayer;
    }
}

