/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerEdge
extends GenLayer {
    private final Mode field_151627_c;

    public GenLayerEdge(long p_i45474_1_, GenLayer p_i45474_3_, Mode p_i45474_4_) {
        super(p_i45474_1_);
        this.parent = p_i45474_3_;
        this.field_151627_c = p_i45474_4_;
    }

    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        switch (this.field_151627_c) {
            default: {
                return this.getIntsCoolWarm(areaX, areaY, areaWidth, areaHeight);
            }
            case HEAT_ICE: {
                return this.getIntsHeatIce(areaX, areaY, areaWidth, areaHeight);
            }
            case SPECIAL: 
        }
        return this.getIntsSpecial(areaX, areaY, areaWidth, areaHeight);
    }

    private int[] getIntsCoolWarm(int p_151626_1_, int p_151626_2_, int p_151626_3_, int p_151626_4_) {
        int i2 = p_151626_1_ - 1;
        int j2 = p_151626_2_ - 1;
        int k2 = 1 + p_151626_3_ + 1;
        int l2 = 1 + p_151626_4_ + 1;
        int[] aint = this.parent.getInts(i2, j2, k2, l2);
        int[] aint1 = IntCache.getIntCache(p_151626_3_ * p_151626_4_);
        for (int i1 = 0; i1 < p_151626_4_; ++i1) {
            for (int j1 = 0; j1 < p_151626_3_; ++j1) {
                this.initChunkSeed(j1 + p_151626_1_, i1 + p_151626_2_);
                int k1 = aint[j1 + 1 + (i1 + 1) * k2];
                if (k1 == 1) {
                    boolean flag1;
                    int l1 = aint[j1 + 1 + (i1 + 1 - 1) * k2];
                    int i22 = aint[j1 + 1 + 1 + (i1 + 1) * k2];
                    int j22 = aint[j1 + 1 - 1 + (i1 + 1) * k2];
                    int k22 = aint[j1 + 1 + (i1 + 1 + 1) * k2];
                    boolean flag = l1 == 3 || i22 == 3 || j22 == 3 || k22 == 3;
                    boolean bl = flag1 = l1 == 4 || i22 == 4 || j22 == 4 || k22 == 4;
                    if (flag || flag1) {
                        k1 = 2;
                    }
                }
                aint1[j1 + i1 * p_151626_3_] = k1;
            }
        }
        return aint1;
    }

    private int[] getIntsHeatIce(int p_151624_1_, int p_151624_2_, int p_151624_3_, int p_151624_4_) {
        int i2 = p_151624_1_ - 1;
        int j2 = p_151624_2_ - 1;
        int k2 = 1 + p_151624_3_ + 1;
        int l2 = 1 + p_151624_4_ + 1;
        int[] aint = this.parent.getInts(i2, j2, k2, l2);
        int[] aint1 = IntCache.getIntCache(p_151624_3_ * p_151624_4_);
        for (int i1 = 0; i1 < p_151624_4_; ++i1) {
            for (int j1 = 0; j1 < p_151624_3_; ++j1) {
                int k1 = aint[j1 + 1 + (i1 + 1) * k2];
                if (k1 == 4) {
                    boolean flag1;
                    int l1 = aint[j1 + 1 + (i1 + 1 - 1) * k2];
                    int i22 = aint[j1 + 1 + 1 + (i1 + 1) * k2];
                    int j22 = aint[j1 + 1 - 1 + (i1 + 1) * k2];
                    int k22 = aint[j1 + 1 + (i1 + 1 + 1) * k2];
                    boolean flag = l1 == 2 || i22 == 2 || j22 == 2 || k22 == 2;
                    boolean bl = flag1 = l1 == 1 || i22 == 1 || j22 == 1 || k22 == 1;
                    if (flag1 || flag) {
                        k1 = 3;
                    }
                }
                aint1[j1 + i1 * p_151624_3_] = k1;
            }
        }
        return aint1;
    }

    private int[] getIntsSpecial(int p_151625_1_, int p_151625_2_, int p_151625_3_, int p_151625_4_) {
        int[] aint = this.parent.getInts(p_151625_1_, p_151625_2_, p_151625_3_, p_151625_4_);
        int[] aint1 = IntCache.getIntCache(p_151625_3_ * p_151625_4_);
        for (int i2 = 0; i2 < p_151625_4_; ++i2) {
            for (int j2 = 0; j2 < p_151625_3_; ++j2) {
                this.initChunkSeed(j2 + p_151625_1_, i2 + p_151625_2_);
                int k2 = aint[j2 + i2 * p_151625_3_];
                if (k2 != 0 && this.nextInt(13) == 0) {
                    k2 |= 1 + this.nextInt(15) << 8 & 0xF00;
                }
                aint1[j2 + i2 * p_151625_3_] = k2;
            }
        }
        return aint1;
    }

    public static enum Mode {
        COOL_WARM,
        HEAT_ICE,
        SPECIAL;

    }
}

