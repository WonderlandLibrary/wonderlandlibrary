/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen;

import java.util.Random;

public class NoiseGeneratorSimplex {
    private static int[][] field_151611_e;
    public static final double field_151614_a;
    private int[] field_151608_f = new int[512];
    public double field_151612_b;
    public double field_151613_c;
    public double field_151610_d;
    private static final double field_151609_g;
    private static final double field_151615_h;
    private static final String __OBFID = "CL_00000537";

    static {
        int[][] arrarrn = new int[12][];
        int[] arrn = new int[3];
        arrn[0] = 1;
        arrn[1] = 1;
        arrarrn[0] = arrn;
        int[] arrn2 = new int[3];
        arrn2[0] = -1;
        arrn2[1] = 1;
        arrarrn[1] = arrn2;
        int[] arrn3 = new int[3];
        arrn3[0] = 1;
        arrn3[1] = -1;
        arrarrn[2] = arrn3;
        int[] arrn4 = new int[3];
        arrn4[0] = -1;
        arrn4[1] = -1;
        arrarrn[3] = arrn4;
        int[] arrn5 = new int[3];
        arrn5[0] = 1;
        arrn5[2] = 1;
        arrarrn[4] = arrn5;
        int[] arrn6 = new int[3];
        arrn6[0] = -1;
        arrn6[2] = 1;
        arrarrn[5] = arrn6;
        int[] arrn7 = new int[3];
        arrn7[0] = 1;
        arrn7[2] = -1;
        arrarrn[6] = arrn7;
        int[] arrn8 = new int[3];
        arrn8[0] = -1;
        arrn8[2] = -1;
        arrarrn[7] = arrn8;
        int[] arrn9 = new int[3];
        arrn9[1] = 1;
        arrn9[2] = 1;
        arrarrn[8] = arrn9;
        int[] arrn10 = new int[3];
        arrn10[1] = -1;
        arrn10[2] = 1;
        arrarrn[9] = arrn10;
        int[] arrn11 = new int[3];
        arrn11[1] = 1;
        arrn11[2] = -1;
        arrarrn[10] = arrn11;
        int[] arrn12 = new int[3];
        arrn12[1] = -1;
        arrn12[2] = -1;
        arrarrn[11] = arrn12;
        field_151611_e = arrarrn;
        field_151614_a = Math.sqrt(3.0);
        field_151609_g = 0.5 * (field_151614_a - 1.0);
        field_151615_h = (3.0 - field_151614_a) / 6.0;
    }

    public NoiseGeneratorSimplex() {
        this(new Random());
    }

    public NoiseGeneratorSimplex(Random p_i45471_1_) {
        this.field_151612_b = p_i45471_1_.nextDouble() * 256.0;
        this.field_151613_c = p_i45471_1_.nextDouble() * 256.0;
        this.field_151610_d = p_i45471_1_.nextDouble() * 256.0;
        int var2 = 0;
        while (var2 < 256) {
            this.field_151608_f[var2] = var2++;
        }
        for (var2 = 0; var2 < 256; ++var2) {
            int var3 = p_i45471_1_.nextInt(256 - var2) + var2;
            int var4 = this.field_151608_f[var2];
            this.field_151608_f[var2] = this.field_151608_f[var3];
            this.field_151608_f[var3] = var4;
            this.field_151608_f[var2 + 256] = this.field_151608_f[var2];
        }
    }

    private static int func_151607_a(double p_151607_0_) {
        return p_151607_0_ > 0.0 ? (int)p_151607_0_ : (int)p_151607_0_ - 1;
    }

    private static double func_151604_a(int[] p_151604_0_, double p_151604_1_, double p_151604_3_) {
        return (double)p_151604_0_[0] * p_151604_1_ + (double)p_151604_0_[1] * p_151604_3_;
    }

    public double func_151605_a(double p_151605_1_, double p_151605_3_) {
        double var9;
        double var7;
        double var5;
        int var30;
        int var29;
        double var23;
        double var27;
        double var17;
        int var16;
        double var19;
        double var11 = 0.5 * (field_151614_a - 1.0);
        double var13 = (p_151605_1_ + p_151605_3_) * var11;
        int var15 = NoiseGeneratorSimplex.func_151607_a(p_151605_1_ + var13);
        double var21 = (double)var15 - (var19 = (double)(var15 + (var16 = NoiseGeneratorSimplex.func_151607_a(p_151605_3_ + var13))) * (var17 = (3.0 - field_151614_a) / 6.0));
        double var25 = p_151605_1_ - var21;
        if (var25 > (var27 = p_151605_3_ - (var23 = (double)var16 - var19))) {
            var29 = 1;
            var30 = 0;
        } else {
            var29 = 0;
            var30 = 1;
        }
        double var31 = var25 - (double)var29 + var17;
        double var33 = var27 - (double)var30 + var17;
        double var35 = var25 - 1.0 + 2.0 * var17;
        double var37 = var27 - 1.0 + 2.0 * var17;
        int var39 = var15 & 0xFF;
        int var40 = var16 & 0xFF;
        int var41 = this.field_151608_f[var39 + this.field_151608_f[var40]] % 12;
        int var42 = this.field_151608_f[var39 + var29 + this.field_151608_f[var40 + var30]] % 12;
        int var43 = this.field_151608_f[var39 + 1 + this.field_151608_f[var40 + 1]] % 12;
        double var44 = 0.5 - var25 * var25 - var27 * var27;
        if (var44 < 0.0) {
            var5 = 0.0;
        } else {
            var44 *= var44;
            var5 = var44 * var44 * NoiseGeneratorSimplex.func_151604_a(field_151611_e[var41], var25, var27);
        }
        double var46 = 0.5 - var31 * var31 - var33 * var33;
        if (var46 < 0.0) {
            var7 = 0.0;
        } else {
            var46 *= var46;
            var7 = var46 * var46 * NoiseGeneratorSimplex.func_151604_a(field_151611_e[var42], var31, var33);
        }
        double var48 = 0.5 - var35 * var35 - var37 * var37;
        if (var48 < 0.0) {
            var9 = 0.0;
        } else {
            var48 *= var48;
            var9 = var48 * var48 * NoiseGeneratorSimplex.func_151604_a(field_151611_e[var43], var35, var37);
        }
        return 70.0 * (var5 + var7 + var9);
    }

    public void func_151606_a(double[] p_151606_1_, double p_151606_2_, double p_151606_4_, int p_151606_6_, int p_151606_7_, double p_151606_8_, double p_151606_10_, double p_151606_12_) {
        int var14 = 0;
        for (int var15 = 0; var15 < p_151606_7_; ++var15) {
            double var16 = (p_151606_4_ + (double)var15) * p_151606_10_ + this.field_151613_c;
            for (int var18 = 0; var18 < p_151606_6_; ++var18) {
                int var10001;
                double var25;
                double var23;
                double var21;
                int var42;
                int var41;
                double var35;
                double var39;
                int var30;
                double var31;
                double var19 = (p_151606_2_ + (double)var18) * p_151606_8_ + this.field_151612_b;
                double var27 = (var19 + var16) * field_151609_g;
                int var29 = NoiseGeneratorSimplex.func_151607_a(var19 + var27);
                double var33 = (double)var29 - (var31 = (double)(var29 + (var30 = NoiseGeneratorSimplex.func_151607_a(var16 + var27))) * field_151615_h);
                double var37 = var19 - var33;
                if (var37 > (var39 = var16 - (var35 = (double)var30 - var31))) {
                    var41 = 1;
                    var42 = 0;
                } else {
                    var41 = 0;
                    var42 = 1;
                }
                double var43 = var37 - (double)var41 + field_151615_h;
                double var45 = var39 - (double)var42 + field_151615_h;
                double var47 = var37 - 1.0 + 2.0 * field_151615_h;
                double var49 = var39 - 1.0 + 2.0 * field_151615_h;
                int var51 = var29 & 0xFF;
                int var52 = var30 & 0xFF;
                int var53 = this.field_151608_f[var51 + this.field_151608_f[var52]] % 12;
                int var54 = this.field_151608_f[var51 + var41 + this.field_151608_f[var52 + var42]] % 12;
                int var55 = this.field_151608_f[var51 + 1 + this.field_151608_f[var52 + 1]] % 12;
                double var56 = 0.5 - var37 * var37 - var39 * var39;
                if (var56 < 0.0) {
                    var21 = 0.0;
                } else {
                    var56 *= var56;
                    var21 = var56 * var56 * NoiseGeneratorSimplex.func_151604_a(field_151611_e[var53], var37, var39);
                }
                double var58 = 0.5 - var43 * var43 - var45 * var45;
                if (var58 < 0.0) {
                    var23 = 0.0;
                } else {
                    var58 *= var58;
                    var23 = var58 * var58 * NoiseGeneratorSimplex.func_151604_a(field_151611_e[var54], var43, var45);
                }
                double var60 = 0.5 - var47 * var47 - var49 * var49;
                if (var60 < 0.0) {
                    var25 = 0.0;
                } else {
                    var60 *= var60;
                    var25 = var60 * var60 * NoiseGeneratorSimplex.func_151604_a(field_151611_e[var55], var47, var49);
                }
                int n = var10001 = var14++;
                p_151606_1_[n] = p_151606_1_[n] + 70.0 * (var21 + var23 + var25) * p_151606_12_;
            }
        }
    }
}

