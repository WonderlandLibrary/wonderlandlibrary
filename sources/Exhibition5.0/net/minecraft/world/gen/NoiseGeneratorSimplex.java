// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen;

import java.util.Random;

public class NoiseGeneratorSimplex
{
    private static int[][] field_151611_e;
    public static final double field_151614_a;
    private int[] field_151608_f;
    public double field_151612_b;
    public double field_151613_c;
    public double field_151610_d;
    private static final double field_151609_g;
    private static final double field_151615_h;
    private static final String __OBFID = "CL_00000537";
    
    public NoiseGeneratorSimplex() {
        this(new Random());
    }
    
    public NoiseGeneratorSimplex(final Random p_i45471_1_) {
        this.field_151608_f = new int[512];
        this.field_151612_b = p_i45471_1_.nextDouble() * 256.0;
        this.field_151613_c = p_i45471_1_.nextDouble() * 256.0;
        this.field_151610_d = p_i45471_1_.nextDouble() * 256.0;
        for (int var2 = 0; var2 < 256; this.field_151608_f[var2] = var2++) {}
        for (int var2 = 0; var2 < 256; ++var2) {
            final int var3 = p_i45471_1_.nextInt(256 - var2) + var2;
            final int var4 = this.field_151608_f[var2];
            this.field_151608_f[var2] = this.field_151608_f[var3];
            this.field_151608_f[var3] = var4;
            this.field_151608_f[var2 + 256] = this.field_151608_f[var2];
        }
    }
    
    private static int func_151607_a(final double p_151607_0_) {
        return (p_151607_0_ > 0.0) ? ((int)p_151607_0_) : ((int)p_151607_0_ - 1);
    }
    
    private static double func_151604_a(final int[] p_151604_0_, final double p_151604_1_, final double p_151604_3_) {
        return p_151604_0_[0] * p_151604_1_ + p_151604_0_[1] * p_151604_3_;
    }
    
    public double func_151605_a(final double p_151605_1_, final double p_151605_3_) {
        final double var11 = 0.5 * (NoiseGeneratorSimplex.field_151614_a - 1.0);
        final double var12 = (p_151605_1_ + p_151605_3_) * var11;
        final int var13 = func_151607_a(p_151605_1_ + var12);
        final int var14 = func_151607_a(p_151605_3_ + var12);
        final double var15 = (3.0 - NoiseGeneratorSimplex.field_151614_a) / 6.0;
        final double var16 = (var13 + var14) * var15;
        final double var17 = var13 - var16;
        final double var18 = var14 - var16;
        final double var19 = p_151605_1_ - var17;
        final double var20 = p_151605_3_ - var18;
        byte var21;
        byte var22;
        if (var19 > var20) {
            var21 = 1;
            var22 = 0;
        }
        else {
            var21 = 0;
            var22 = 1;
        }
        final double var23 = var19 - var21 + var15;
        final double var24 = var20 - var22 + var15;
        final double var25 = var19 - 1.0 + 2.0 * var15;
        final double var26 = var20 - 1.0 + 2.0 * var15;
        final int var27 = var13 & 0xFF;
        final int var28 = var14 & 0xFF;
        final int var29 = this.field_151608_f[var27 + this.field_151608_f[var28]] % 12;
        final int var30 = this.field_151608_f[var27 + var21 + this.field_151608_f[var28 + var22]] % 12;
        final int var31 = this.field_151608_f[var27 + 1 + this.field_151608_f[var28 + 1]] % 12;
        double var32 = 0.5 - var19 * var19 - var20 * var20;
        double var33;
        if (var32 < 0.0) {
            var33 = 0.0;
        }
        else {
            var32 *= var32;
            var33 = var32 * var32 * func_151604_a(NoiseGeneratorSimplex.field_151611_e[var29], var19, var20);
        }
        double var34 = 0.5 - var23 * var23 - var24 * var24;
        double var35;
        if (var34 < 0.0) {
            var35 = 0.0;
        }
        else {
            var34 *= var34;
            var35 = var34 * var34 * func_151604_a(NoiseGeneratorSimplex.field_151611_e[var30], var23, var24);
        }
        double var36 = 0.5 - var25 * var25 - var26 * var26;
        double var37;
        if (var36 < 0.0) {
            var37 = 0.0;
        }
        else {
            var36 *= var36;
            var37 = var36 * var36 * func_151604_a(NoiseGeneratorSimplex.field_151611_e[var31], var25, var26);
        }
        return 70.0 * (var33 + var35 + var37);
    }
    
    public void func_151606_a(final double[] p_151606_1_, final double p_151606_2_, final double p_151606_4_, final int p_151606_6_, final int p_151606_7_, final double p_151606_8_, final double p_151606_10_, final double p_151606_12_) {
        int var14 = 0;
        for (int var15 = 0; var15 < p_151606_7_; ++var15) {
            final double var16 = (p_151606_4_ + var15) * p_151606_10_ + this.field_151613_c;
            for (int var17 = 0; var17 < p_151606_6_; ++var17) {
                final double var18 = (p_151606_2_ + var17) * p_151606_8_ + this.field_151612_b;
                final double var19 = (var18 + var16) * NoiseGeneratorSimplex.field_151609_g;
                final int var20 = func_151607_a(var18 + var19);
                final int var21 = func_151607_a(var16 + var19);
                final double var22 = (var20 + var21) * NoiseGeneratorSimplex.field_151615_h;
                final double var23 = var20 - var22;
                final double var24 = var21 - var22;
                final double var25 = var18 - var23;
                final double var26 = var16 - var24;
                byte var27;
                byte var28;
                if (var25 > var26) {
                    var27 = 1;
                    var28 = 0;
                }
                else {
                    var27 = 0;
                    var28 = 1;
                }
                final double var29 = var25 - var27 + NoiseGeneratorSimplex.field_151615_h;
                final double var30 = var26 - var28 + NoiseGeneratorSimplex.field_151615_h;
                final double var31 = var25 - 1.0 + 2.0 * NoiseGeneratorSimplex.field_151615_h;
                final double var32 = var26 - 1.0 + 2.0 * NoiseGeneratorSimplex.field_151615_h;
                final int var33 = var20 & 0xFF;
                final int var34 = var21 & 0xFF;
                final int var35 = this.field_151608_f[var33 + this.field_151608_f[var34]] % 12;
                final int var36 = this.field_151608_f[var33 + var27 + this.field_151608_f[var34 + var28]] % 12;
                final int var37 = this.field_151608_f[var33 + 1 + this.field_151608_f[var34 + 1]] % 12;
                double var38 = 0.5 - var25 * var25 - var26 * var26;
                double var39;
                if (var38 < 0.0) {
                    var39 = 0.0;
                }
                else {
                    var38 *= var38;
                    var39 = var38 * var38 * func_151604_a(NoiseGeneratorSimplex.field_151611_e[var35], var25, var26);
                }
                double var40 = 0.5 - var29 * var29 - var30 * var30;
                double var41;
                if (var40 < 0.0) {
                    var41 = 0.0;
                }
                else {
                    var40 *= var40;
                    var41 = var40 * var40 * func_151604_a(NoiseGeneratorSimplex.field_151611_e[var36], var29, var30);
                }
                double var42 = 0.5 - var31 * var31 - var32 * var32;
                double var43;
                if (var42 < 0.0) {
                    var43 = 0.0;
                }
                else {
                    var42 *= var42;
                    var43 = var42 * var42 * func_151604_a(NoiseGeneratorSimplex.field_151611_e[var37], var31, var32);
                }
                final int n;
                final int var44 = n = var14++;
                p_151606_1_[n] += 70.0 * (var39 + var41 + var43) * p_151606_12_;
            }
        }
    }
    
    static {
        NoiseGeneratorSimplex.field_151611_e = new int[][] { { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 }, { -1, -1, 0 }, { 1, 0, 1 }, { -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 }, { 0, -1, -1 } };
        field_151614_a = Math.sqrt(3.0);
        field_151609_g = 0.5 * (NoiseGeneratorSimplex.field_151614_a - 1.0);
        field_151615_h = (3.0 - NoiseGeneratorSimplex.field_151614_a) / 6.0;
    }
}
