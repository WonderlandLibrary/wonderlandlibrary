/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerAddMushroomIsland
extends GenLayer {
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
                int n11 = nArray[n10 + 0 + (n9 + 0) * n7];
                int n12 = nArray[n10 + 2 + (n9 + 0) * n7];
                int n13 = nArray[n10 + 0 + (n9 + 2) * n7];
                int n14 = nArray[n10 + 2 + (n9 + 2) * n7];
                int n15 = nArray[n10 + 1 + (n9 + 1) * n7];
                this.initChunkSeed(n10 + n, n9 + n2);
                nArray2[n10 + n9 * n3] = n15 == 0 && n11 == 0 && n12 == 0 && n13 == 0 && n14 == 0 && this.nextInt(100) == 0 ? BiomeGenBase.mushroomIsland.biomeID : n15;
                ++n10;
            }
            ++n9;
        }
        return nArray2;
    }

    public GenLayerAddMushroomIsland(long l, GenLayer genLayer) {
        super(l);
        this.parent = genLayer;
    }
}

