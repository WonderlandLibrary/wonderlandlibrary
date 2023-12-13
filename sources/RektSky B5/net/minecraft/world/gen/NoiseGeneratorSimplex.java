/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen;

import java.util.Random;

public class NoiseGeneratorSimplex {
    private static int[][] field_151611_e = new int[][]{{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0}, {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1}, {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}};
    public static final double field_151614_a = Math.sqrt(3.0);
    private int[] field_151608_f = new int[512];
    public double field_151612_b;
    public double field_151613_c;
    public double field_151610_d;
    private static final double field_151609_g = 0.5 * (field_151614_a - 1.0);
    private static final double field_151615_h = (3.0 - field_151614_a) / 6.0;

    public NoiseGeneratorSimplex() {
        this(new Random());
    }

    public NoiseGeneratorSimplex(Random p_i45471_1_) {
        this.field_151612_b = p_i45471_1_.nextDouble() * 256.0;
        this.field_151613_c = p_i45471_1_.nextDouble() * 256.0;
        this.field_151610_d = p_i45471_1_.nextDouble() * 256.0;
        int i2 = 0;
        while (i2 < 256) {
            this.field_151608_f[i2] = i2++;
        }
        for (int l2 = 0; l2 < 256; ++l2) {
            int j2 = p_i45471_1_.nextInt(256 - l2) + l2;
            int k2 = this.field_151608_f[l2];
            this.field_151608_f[l2] = this.field_151608_f[j2];
            this.field_151608_f[j2] = k2;
            this.field_151608_f[l2 + 256] = this.field_151608_f[l2];
        }
    }

    private static int func_151607_a(double p_151607_0_) {
        return p_151607_0_ > 0.0 ? (int)p_151607_0_ : (int)p_151607_0_ - 1;
    }

    private static double func_151604_a(int[] p_151604_0_, double p_151604_1_, double p_151604_3_) {
        return (double)p_151604_0_[0] * p_151604_1_ + (double)p_151604_0_[1] * p_151604_3_;
    }

    public double func_151605_a(double p_151605_1_, double p_151605_3_) {
        double d2;
        double d1;
        double d0;
        int l2;
        int k2;
        double d8;
        double d10;
        double d5;
        int j2;
        double d6;
        double d3 = 0.5 * (field_151614_a - 1.0);
        double d4 = (p_151605_1_ + p_151605_3_) * d3;
        int i2 = NoiseGeneratorSimplex.func_151607_a(p_151605_1_ + d4);
        double d7 = (double)i2 - (d6 = (double)(i2 + (j2 = NoiseGeneratorSimplex.func_151607_a(p_151605_3_ + d4))) * (d5 = (3.0 - field_151614_a) / 6.0));
        double d9 = p_151605_1_ - d7;
        if (d9 > (d10 = p_151605_3_ - (d8 = (double)j2 - d6))) {
            k2 = 1;
            l2 = 0;
        } else {
            k2 = 0;
            l2 = 1;
        }
        double d11 = d9 - (double)k2 + d5;
        double d12 = d10 - (double)l2 + d5;
        double d13 = d9 - 1.0 + 2.0 * d5;
        double d14 = d10 - 1.0 + 2.0 * d5;
        int i1 = i2 & 0xFF;
        int j1 = j2 & 0xFF;
        int k1 = this.field_151608_f[i1 + this.field_151608_f[j1]] % 12;
        int l1 = this.field_151608_f[i1 + k2 + this.field_151608_f[j1 + l2]] % 12;
        int i22 = this.field_151608_f[i1 + 1 + this.field_151608_f[j1 + 1]] % 12;
        double d15 = 0.5 - d9 * d9 - d10 * d10;
        if (d15 < 0.0) {
            d0 = 0.0;
        } else {
            d15 *= d15;
            d0 = d15 * d15 * NoiseGeneratorSimplex.func_151604_a(field_151611_e[k1], d9, d10);
        }
        double d16 = 0.5 - d11 * d11 - d12 * d12;
        if (d16 < 0.0) {
            d1 = 0.0;
        } else {
            d16 *= d16;
            d1 = d16 * d16 * NoiseGeneratorSimplex.func_151604_a(field_151611_e[l1], d11, d12);
        }
        double d17 = 0.5 - d13 * d13 - d14 * d14;
        if (d17 < 0.0) {
            d2 = 0.0;
        } else {
            d17 *= d17;
            d2 = d17 * d17 * NoiseGeneratorSimplex.func_151604_a(field_151611_e[i22], d13, d14);
        }
        return 70.0 * (d0 + d1 + d2);
    }

    public void func_151606_a(double[] p_151606_1_, double p_151606_2_, double p_151606_4_, int p_151606_6_, int p_151606_7_, double p_151606_8_, double p_151606_10_, double p_151606_12_) {
        int i2 = 0;
        for (int j2 = 0; j2 < p_151606_7_; ++j2) {
            double d0 = (p_151606_4_ + (double)j2) * p_151606_10_ + this.field_151613_c;
            for (int k2 = 0; k2 < p_151606_6_; ++k2) {
                int i3;
                double d4;
                double d3;
                double d2;
                int k1;
                int j1;
                double d8;
                double d10;
                int i1;
                double d6;
                double d1 = (p_151606_2_ + (double)k2) * p_151606_8_ + this.field_151612_b;
                double d5 = (d1 + d0) * field_151609_g;
                int l2 = NoiseGeneratorSimplex.func_151607_a(d1 + d5);
                double d7 = (double)l2 - (d6 = (double)(l2 + (i1 = NoiseGeneratorSimplex.func_151607_a(d0 + d5))) * field_151615_h);
                double d9 = d1 - d7;
                if (d9 > (d10 = d0 - (d8 = (double)i1 - d6))) {
                    j1 = 1;
                    k1 = 0;
                } else {
                    j1 = 0;
                    k1 = 1;
                }
                double d11 = d9 - (double)j1 + field_151615_h;
                double d12 = d10 - (double)k1 + field_151615_h;
                double d13 = d9 - 1.0 + 2.0 * field_151615_h;
                double d14 = d10 - 1.0 + 2.0 * field_151615_h;
                int l1 = l2 & 0xFF;
                int i22 = i1 & 0xFF;
                int j22 = this.field_151608_f[l1 + this.field_151608_f[i22]] % 12;
                int k22 = this.field_151608_f[l1 + j1 + this.field_151608_f[i22 + k1]] % 12;
                int l22 = this.field_151608_f[l1 + 1 + this.field_151608_f[i22 + 1]] % 12;
                double d15 = 0.5 - d9 * d9 - d10 * d10;
                if (d15 < 0.0) {
                    d2 = 0.0;
                } else {
                    d15 *= d15;
                    d2 = d15 * d15 * NoiseGeneratorSimplex.func_151604_a(field_151611_e[j22], d9, d10);
                }
                double d16 = 0.5 - d11 * d11 - d12 * d12;
                if (d16 < 0.0) {
                    d3 = 0.0;
                } else {
                    d16 *= d16;
                    d3 = d16 * d16 * NoiseGeneratorSimplex.func_151604_a(field_151611_e[k22], d11, d12);
                }
                double d17 = 0.5 - d13 * d13 - d14 * d14;
                if (d17 < 0.0) {
                    d4 = 0.0;
                } else {
                    d17 *= d17;
                    d4 = d17 * d17 * NoiseGeneratorSimplex.func_151604_a(field_151611_e[l22], d13, d14);
                }
                int n2 = i3 = i2++;
                p_151606_1_[n2] = p_151606_1_[n2] + 70.0 * (d2 + d3 + d4) * p_151606_12_;
            }
        }
    }
}

