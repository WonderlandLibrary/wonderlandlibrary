/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerAddSnow
extends GenLayer {
    public GenLayerAddSnow(long l, GenLayer genLayer) {
        super(l);
        this.parent = genLayer;
    }

    @Override
    public int[] getInts(int n, int n2, int n3, int n4) {
        int n5 = n - 1;
        int n6 = n2 - 1;
        int n7 = n3 + 2;
        int n8 = n4 + 2;
        int[] nArray = this.parent.getInts(n5, n6, n7, n8);
        int[] nArray2 = IntCache.getIntCache(n3 * n4);
        int n9 = 0;
        while (n9 < n4) {
            int n10 = 0;
            while (n10 < n3) {
                int n11 = nArray[n10 + 1 + (n9 + 1) * n7];
                this.initChunkSeed(n10 + n, n9 + n2);
                if (n11 == 0) {
                    nArray2[n10 + n9 * n3] = 0;
                } else {
                    int n12 = this.nextInt(6);
                    n12 = n12 == 0 ? 4 : (n12 <= 1 ? 3 : 1);
                    nArray2[n10 + n9 * n3] = n12;
                }
                ++n10;
            }
            ++n9;
        }
        return nArray2;
    }
}

