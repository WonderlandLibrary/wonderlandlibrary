// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import java.util.Random;

public class NoiseGeneratorSimplex
{
    private static final int[][] grad3;
    public static final double SQRT_3;
    private final int[] p;
    public double xo;
    public double yo;
    public double zo;
    private static final double F2;
    private static final double G2;
    
    public NoiseGeneratorSimplex() {
        this(new Random());
    }
    
    public NoiseGeneratorSimplex(final Random seed) {
        this.p = new int[512];
        this.xo = seed.nextDouble() * 256.0;
        this.yo = seed.nextDouble() * 256.0;
        this.zo = seed.nextDouble() * 256.0;
        for (int i = 0; i < 256; this.p[i] = i++) {}
        for (int l = 0; l < 256; ++l) {
            final int j = seed.nextInt(256 - l) + l;
            final int k = this.p[l];
            this.p[l] = this.p[j];
            this.p[j] = k;
            this.p[l + 256] = this.p[l];
        }
    }
    
    private static int fastFloor(final double value) {
        return (value > 0.0) ? ((int)value) : ((int)value - 1);
    }
    
    private static double dot(final int[] p_151604_0_, final double p_151604_1_, final double p_151604_3_) {
        return p_151604_0_[0] * p_151604_1_ + p_151604_0_[1] * p_151604_3_;
    }
    
    public double getValue(final double p_151605_1_, final double p_151605_3_) {
        final double d3 = 0.5 * (NoiseGeneratorSimplex.SQRT_3 - 1.0);
        final double d4 = (p_151605_1_ + p_151605_3_) * d3;
        final int i = fastFloor(p_151605_1_ + d4);
        final int j = fastFloor(p_151605_3_ + d4);
        final double d5 = (3.0 - NoiseGeneratorSimplex.SQRT_3) / 6.0;
        final double d6 = (i + j) * d5;
        final double d7 = i - d6;
        final double d8 = j - d6;
        final double d9 = p_151605_1_ - d7;
        final double d10 = p_151605_3_ - d8;
        int k;
        int l;
        if (d9 > d10) {
            k = 1;
            l = 0;
        }
        else {
            k = 0;
            l = 1;
        }
        final double d11 = d9 - k + d5;
        final double d12 = d10 - l + d5;
        final double d13 = d9 - 1.0 + 2.0 * d5;
        final double d14 = d10 - 1.0 + 2.0 * d5;
        final int i2 = i & 0xFF;
        final int j2 = j & 0xFF;
        final int k2 = this.p[i2 + this.p[j2]] % 12;
        final int l2 = this.p[i2 + k + this.p[j2 + l]] % 12;
        final int i3 = this.p[i2 + 1 + this.p[j2 + 1]] % 12;
        double d15 = 0.5 - d9 * d9 - d10 * d10;
        double d16;
        if (d15 < 0.0) {
            d16 = 0.0;
        }
        else {
            d15 *= d15;
            d16 = d15 * d15 * dot(NoiseGeneratorSimplex.grad3[k2], d9, d10);
        }
        double d17 = 0.5 - d11 * d11 - d12 * d12;
        double d18;
        if (d17 < 0.0) {
            d18 = 0.0;
        }
        else {
            d17 *= d17;
            d18 = d17 * d17 * dot(NoiseGeneratorSimplex.grad3[l2], d11, d12);
        }
        double d19 = 0.5 - d13 * d13 - d14 * d14;
        double d20;
        if (d19 < 0.0) {
            d20 = 0.0;
        }
        else {
            d19 *= d19;
            d20 = d19 * d19 * dot(NoiseGeneratorSimplex.grad3[i3], d13, d14);
        }
        return 70.0 * (d16 + d18 + d20);
    }
    
    public void add(final double[] p_151606_1_, final double p_151606_2_, final double p_151606_4_, final int p_151606_6_, final int p_151606_7_, final double p_151606_8_, final double p_151606_10_, final double p_151606_12_) {
        int i = 0;
        for (int j = 0; j < p_151606_7_; ++j) {
            final double d0 = (p_151606_4_ + j) * p_151606_10_ + this.yo;
            for (int k = 0; k < p_151606_6_; ++k) {
                final double d2 = (p_151606_2_ + k) * p_151606_8_ + this.xo;
                final double d3 = (d2 + d0) * NoiseGeneratorSimplex.F2;
                final int l = fastFloor(d2 + d3);
                final int i2 = fastFloor(d0 + d3);
                final double d4 = (l + i2) * NoiseGeneratorSimplex.G2;
                final double d5 = l - d4;
                final double d6 = i2 - d4;
                final double d7 = d2 - d5;
                final double d8 = d0 - d6;
                int j2;
                int k2;
                if (d7 > d8) {
                    j2 = 1;
                    k2 = 0;
                }
                else {
                    j2 = 0;
                    k2 = 1;
                }
                final double d9 = d7 - j2 + NoiseGeneratorSimplex.G2;
                final double d10 = d8 - k2 + NoiseGeneratorSimplex.G2;
                final double d11 = d7 - 1.0 + 2.0 * NoiseGeneratorSimplex.G2;
                final double d12 = d8 - 1.0 + 2.0 * NoiseGeneratorSimplex.G2;
                final int l2 = l & 0xFF;
                final int i3 = i2 & 0xFF;
                final int j3 = this.p[l2 + this.p[i3]] % 12;
                final int k3 = this.p[l2 + j2 + this.p[i3 + k2]] % 12;
                final int l3 = this.p[l2 + 1 + this.p[i3 + 1]] % 12;
                double d13 = 0.5 - d7 * d7 - d8 * d8;
                double d14;
                if (d13 < 0.0) {
                    d14 = 0.0;
                }
                else {
                    d13 *= d13;
                    d14 = d13 * d13 * dot(NoiseGeneratorSimplex.grad3[j3], d7, d8);
                }
                double d15 = 0.5 - d9 * d9 - d10 * d10;
                double d16;
                if (d15 < 0.0) {
                    d16 = 0.0;
                }
                else {
                    d15 *= d15;
                    d16 = d15 * d15 * dot(NoiseGeneratorSimplex.grad3[k3], d9, d10);
                }
                double d17 = 0.5 - d11 * d11 - d12 * d12;
                double d18;
                if (d17 < 0.0) {
                    d18 = 0.0;
                }
                else {
                    d17 *= d17;
                    d18 = d17 * d17 * dot(NoiseGeneratorSimplex.grad3[l3], d11, d12);
                }
                final int n;
                final int i4 = n = i++;
                p_151606_1_[n] += 70.0 * (d14 + d16 + d18) * p_151606_12_;
            }
        }
    }
    
    static {
        grad3 = new int[][] { { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 }, { -1, -1, 0 }, { 1, 0, 1 }, { -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 }, { 0, -1, -1 } };
        SQRT_3 = Math.sqrt(3.0);
        F2 = 0.5 * (NoiseGeneratorSimplex.SQRT_3 - 1.0);
        G2 = (3.0 - NoiseGeneratorSimplex.SQRT_3) / 6.0;
    }
}
