/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerAddIsland
extends GenLayer {
    public GenLayerAddIsland(long p_i2119_1_, GenLayer p_i2119_3_) {
        super(p_i2119_1_);
        this.parent = p_i2119_3_;
    }

    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int i2 = areaX - 1;
        int j2 = areaY - 1;
        int k2 = areaWidth + 2;
        int l2 = areaHeight + 2;
        int[] aint = this.parent.getInts(i2, j2, k2, l2);
        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i1 = 0; i1 < areaHeight; ++i1) {
            for (int j1 = 0; j1 < areaWidth; ++j1) {
                int k1 = aint[j1 + 0 + (i1 + 0) * k2];
                int l1 = aint[j1 + 2 + (i1 + 0) * k2];
                int i22 = aint[j1 + 0 + (i1 + 2) * k2];
                int j22 = aint[j1 + 2 + (i1 + 2) * k2];
                int k22 = aint[j1 + 1 + (i1 + 1) * k2];
                this.initChunkSeed(j1 + areaX, i1 + areaY);
                if (k22 != 0 || k1 == 0 && l1 == 0 && i22 == 0 && j22 == 0) {
                    if (k22 > 0 && (k1 == 0 || l1 == 0 || i22 == 0 || j22 == 0)) {
                        if (this.nextInt(5) == 0) {
                            if (k22 == 4) {
                                aint1[j1 + i1 * areaWidth] = 4;
                                continue;
                            }
                            aint1[j1 + i1 * areaWidth] = 0;
                            continue;
                        }
                        aint1[j1 + i1 * areaWidth] = k22;
                        continue;
                    }
                    aint1[j1 + i1 * areaWidth] = k22;
                    continue;
                }
                int l22 = 1;
                int i3 = 1;
                if (k1 != 0 && this.nextInt(l22++) == 0) {
                    i3 = k1;
                }
                if (l1 != 0 && this.nextInt(l22++) == 0) {
                    i3 = l1;
                }
                if (i22 != 0 && this.nextInt(l22++) == 0) {
                    i3 = i22;
                }
                if (j22 != 0 && this.nextInt(l22++) == 0) {
                    i3 = j22;
                }
                aint1[j1 + i1 * areaWidth] = this.nextInt(3) == 0 ? i3 : (i3 == 4 ? 4 : 0);
            }
        }
        return aint1;
    }
}

