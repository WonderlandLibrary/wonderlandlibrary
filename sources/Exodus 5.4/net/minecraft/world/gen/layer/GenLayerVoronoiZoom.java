/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerVoronoiZoom
extends GenLayer {
    @Override
    public int[] getInts(int n, int n2, int n3, int n4) {
        int n5;
        int n6 = (n -= 2) >> 2;
        int n7 = (n2 -= 2) >> 2;
        int n8 = (n3 >> 2) + 2;
        int n9 = (n4 >> 2) + 2;
        int[] nArray = this.parent.getInts(n6, n7, n8, n9);
        int n10 = n8 - 1 << 2;
        int n11 = n9 - 1 << 2;
        int[] nArray2 = IntCache.getIntCache(n10 * n11);
        int n12 = 0;
        while (n12 < n9 - 1) {
            n5 = 0;
            int n13 = nArray[n5 + 0 + (n12 + 0) * n8];
            int n14 = nArray[n5 + 0 + (n12 + 1) * n8];
            while (n5 < n8 - 1) {
                double d = 3.6;
                this.initChunkSeed(n5 + n6 << 2, n12 + n7 << 2);
                double d2 = ((double)this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
                double d3 = ((double)this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
                this.initChunkSeed(n5 + n6 + 1 << 2, n12 + n7 << 2);
                double d4 = ((double)this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
                double d5 = ((double)this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
                this.initChunkSeed(n5 + n6 << 2, n12 + n7 + 1 << 2);
                double d6 = ((double)this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
                double d7 = ((double)this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
                this.initChunkSeed(n5 + n6 + 1 << 2, n12 + n7 + 1 << 2);
                double d8 = ((double)this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
                double d9 = ((double)this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
                int n15 = nArray[n5 + 1 + (n12 + 0) * n8] & 0xFF;
                int n16 = nArray[n5 + 1 + (n12 + 1) * n8] & 0xFF;
                int n17 = 0;
                while (n17 < 4) {
                    int n18 = ((n12 << 2) + n17) * n10 + (n5 << 2);
                    int n19 = 0;
                    while (n19 < 4) {
                        double d10 = ((double)n17 - d3) * ((double)n17 - d3) + ((double)n19 - d2) * ((double)n19 - d2);
                        double d11 = ((double)n17 - d5) * ((double)n17 - d5) + ((double)n19 - d4) * ((double)n19 - d4);
                        double d12 = ((double)n17 - d7) * ((double)n17 - d7) + ((double)n19 - d6) * ((double)n19 - d6);
                        double d13 = ((double)n17 - d9) * ((double)n17 - d9) + ((double)n19 - d8) * ((double)n19 - d8);
                        nArray2[n18++] = d10 < d11 && d10 < d12 && d10 < d13 ? n13 : (d11 < d10 && d11 < d12 && d11 < d13 ? n15 : (d12 < d10 && d12 < d11 && d12 < d13 ? n14 : n16));
                        ++n19;
                    }
                    ++n17;
                }
                n13 = n15;
                n14 = n16;
                ++n5;
            }
            ++n12;
        }
        int[] nArray3 = IntCache.getIntCache(n3 * n4);
        n5 = 0;
        while (n5 < n4) {
            System.arraycopy(nArray2, (n5 + (n2 & 3)) * n10 + (n & 3), nArray3, n5 * n3, n3);
            ++n5;
        }
        return nArray3;
    }

    public GenLayerVoronoiZoom(long l, GenLayer genLayer) {
        super(l);
        this.parent = genLayer;
    }
}

