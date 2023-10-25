/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerEdge
extends GenLayer {
    private final Mode field_151627_c;

    public GenLayerEdge(long l, GenLayer genLayer, Mode mode) {
        super(l);
        this.parent = genLayer;
        this.field_151627_c = mode;
    }

    private int[] getIntsCoolWarm(int n, int n2, int n3, int n4) {
        int n5 = n - 1;
        int n6 = n2 - 1;
        int n7 = 1 + n3 + 1;
        int n8 = 1 + n4 + 1;
        int[] nArray = this.parent.getInts(n5, n6, n7, n8);
        int[] nArray2 = IntCache.getIntCache(n3 * n4);
        int n9 = 0;
        while (n9 < n4) {
            int n10 = 0;
            while (n10 < n3) {
                this.initChunkSeed(n10 + n, n9 + n2);
                int n11 = nArray[n10 + 1 + (n9 + 1) * n7];
                if (n11 == 1) {
                    boolean bl;
                    int n12 = nArray[n10 + 1 + (n9 + 1 - 1) * n7];
                    int n13 = nArray[n10 + 1 + 1 + (n9 + 1) * n7];
                    int n14 = nArray[n10 + 1 - 1 + (n9 + 1) * n7];
                    int n15 = nArray[n10 + 1 + (n9 + 1 + 1) * n7];
                    boolean bl2 = n12 == 3 || n13 == 3 || n14 == 3 || n15 == 3;
                    boolean bl3 = bl = n12 == 4 || n13 == 4 || n14 == 4 || n15 == 4;
                    if (bl2 || bl) {
                        n11 = 2;
                    }
                }
                nArray2[n10 + n9 * n3] = n11;
                ++n10;
            }
            ++n9;
        }
        return nArray2;
    }

    private int[] getIntsSpecial(int n, int n2, int n3, int n4) {
        int[] nArray = this.parent.getInts(n, n2, n3, n4);
        int[] nArray2 = IntCache.getIntCache(n3 * n4);
        int n5 = 0;
        while (n5 < n4) {
            int n6 = 0;
            while (n6 < n3) {
                this.initChunkSeed(n6 + n, n5 + n2);
                int n7 = nArray[n6 + n5 * n3];
                if (n7 != 0 && this.nextInt(13) == 0) {
                    n7 |= 1 + this.nextInt(15) << 8 & 0xF00;
                }
                nArray2[n6 + n5 * n3] = n7;
                ++n6;
            }
            ++n5;
        }
        return nArray2;
    }

    @Override
    public int[] getInts(int n, int n2, int n3, int n4) {
        switch (this.field_151627_c) {
            default: {
                return this.getIntsCoolWarm(n, n2, n3, n4);
            }
            case HEAT_ICE: {
                return this.getIntsHeatIce(n, n2, n3, n4);
            }
            case SPECIAL: 
        }
        return this.getIntsSpecial(n, n2, n3, n4);
    }

    private int[] getIntsHeatIce(int n, int n2, int n3, int n4) {
        int n5 = n - 1;
        int n6 = n2 - 1;
        int n7 = 1 + n3 + 1;
        int n8 = 1 + n4 + 1;
        int[] nArray = this.parent.getInts(n5, n6, n7, n8);
        int[] nArray2 = IntCache.getIntCache(n3 * n4);
        int n9 = 0;
        while (n9 < n4) {
            int n10 = 0;
            while (n10 < n3) {
                int n11 = nArray[n10 + 1 + (n9 + 1) * n7];
                if (n11 == 4) {
                    boolean bl;
                    int n12 = nArray[n10 + 1 + (n9 + 1 - 1) * n7];
                    int n13 = nArray[n10 + 1 + 1 + (n9 + 1) * n7];
                    int n14 = nArray[n10 + 1 - 1 + (n9 + 1) * n7];
                    int n15 = nArray[n10 + 1 + (n9 + 1 + 1) * n7];
                    boolean bl2 = n12 == 2 || n13 == 2 || n14 == 2 || n15 == 2;
                    boolean bl3 = bl = n12 == 1 || n13 == 1 || n14 == 1 || n15 == 1;
                    if (bl || bl2) {
                        n11 = 3;
                    }
                }
                nArray2[n10 + n9 * n3] = n11;
                ++n10;
            }
            ++n9;
        }
        return nArray2;
    }

    public static enum Mode {
        COOL_WARM,
        HEAT_ICE,
        SPECIAL;

    }
}

