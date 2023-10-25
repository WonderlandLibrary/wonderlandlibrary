/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerZoom
extends GenLayer {
    public GenLayerZoom(long l, GenLayer genLayer) {
        super(l);
        this.parent = genLayer;
    }

    @Override
    public int[] getInts(int n, int n2, int n3, int n4) {
        int n5;
        int n6 = n >> 1;
        int n7 = n2 >> 1;
        int n8 = (n3 >> 1) + 2;
        int n9 = (n4 >> 1) + 2;
        int[] nArray = this.parent.getInts(n6, n7, n8, n9);
        int n10 = n8 - 1 << 1;
        int n11 = n9 - 1 << 1;
        int[] nArray2 = IntCache.getIntCache(n10 * n11);
        int n12 = 0;
        while (n12 < n9 - 1) {
            n5 = (n12 << 1) * n10;
            int n13 = 0;
            int n14 = nArray[n13 + 0 + (n12 + 0) * n8];
            int n15 = nArray[n13 + 0 + (n12 + 1) * n8];
            while (n13 < n8 - 1) {
                this.initChunkSeed(n13 + n6 << 1, n12 + n7 << 1);
                int n16 = nArray[n13 + 1 + (n12 + 0) * n8];
                int n17 = nArray[n13 + 1 + (n12 + 1) * n8];
                nArray2[n5] = n14;
                nArray2[n5++ + n10] = this.selectRandom(n14, n15);
                nArray2[n5] = this.selectRandom(n14, n16);
                nArray2[n5++ + n10] = this.selectModeOrRandom(n14, n16, n15, n17);
                n14 = n16;
                n15 = n17;
                ++n13;
            }
            ++n12;
        }
        int[] nArray3 = IntCache.getIntCache(n3 * n4);
        n5 = 0;
        while (n5 < n4) {
            System.arraycopy(nArray2, (n5 + (n2 & 1)) * n10 + (n & 1), nArray3, n5 * n3, n3);
            ++n5;
        }
        return nArray3;
    }

    public static GenLayer magnify(long l, GenLayer genLayer, int n) {
        GenLayer genLayer2 = genLayer;
        int n2 = 0;
        while (n2 < n) {
            genLayer2 = new GenLayerZoom(l + (long)n2, genLayer2);
            ++n2;
        }
        return genLayer2;
    }
}

