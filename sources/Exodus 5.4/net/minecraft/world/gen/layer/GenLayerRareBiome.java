/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerRareBiome
extends GenLayer {
    @Override
    public int[] getInts(int n, int n2, int n3, int n4) {
        int[] nArray = this.parent.getInts(n - 1, n2 - 1, n3 + 2, n4 + 2);
        int[] nArray2 = IntCache.getIntCache(n3 * n4);
        int n5 = 0;
        while (n5 < n4) {
            int n6 = 0;
            while (n6 < n3) {
                this.initChunkSeed(n6 + n, n5 + n2);
                int n7 = nArray[n6 + 1 + (n5 + 1) * (n3 + 2)];
                nArray2[n6 + n5 * n3] = this.nextInt(57) == 0 ? (n7 == BiomeGenBase.plains.biomeID ? BiomeGenBase.plains.biomeID + 128 : n7) : n7;
                ++n6;
            }
            ++n5;
        }
        return nArray2;
    }

    public GenLayerRareBiome(long l, GenLayer genLayer) {
        super(l);
        this.parent = genLayer;
    }
}

