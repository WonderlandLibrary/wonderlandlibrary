/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen;

import java.util.Random;

public class NoiseGeneratorSimplex {
    public double field_151610_d;
    private static final double field_151609_g;
    public double field_151613_c;
    public double field_151612_b;
    private static int[][] field_151611_e;
    private static final double field_151615_h;
    public static final double field_151614_a;
    private int[] field_151608_f = new int[512];

    public double func_151605_a(double d, double d2) {
        double d3;
        double d4;
        double d5;
        int n;
        int n2;
        double d6;
        double d7;
        double d8;
        int n3;
        double d9;
        double d10 = 0.5 * (field_151614_a - 1.0);
        double d11 = (d + d2) * d10;
        int n4 = NoiseGeneratorSimplex.func_151607_a(d + d11);
        double d12 = (double)n4 - (d9 = (double)(n4 + (n3 = NoiseGeneratorSimplex.func_151607_a(d2 + d11))) * (d8 = (3.0 - field_151614_a) / 6.0));
        double d13 = d - d12;
        if (d13 > (d7 = d2 - (d6 = (double)n3 - d9))) {
            n2 = 1;
            n = 0;
        } else {
            n2 = 0;
            n = 1;
        }
        double d14 = d13 - (double)n2 + d8;
        double d15 = d7 - (double)n + d8;
        double d16 = d13 - 1.0 + 2.0 * d8;
        double d17 = d7 - 1.0 + 2.0 * d8;
        int n5 = n4 & 0xFF;
        int n6 = n3 & 0xFF;
        int n7 = this.field_151608_f[n5 + this.field_151608_f[n6]] % 12;
        int n8 = this.field_151608_f[n5 + n2 + this.field_151608_f[n6 + n]] % 12;
        int n9 = this.field_151608_f[n5 + 1 + this.field_151608_f[n6 + 1]] % 12;
        double d18 = 0.5 - d13 * d13 - d7 * d7;
        if (d18 < 0.0) {
            d5 = 0.0;
        } else {
            d18 *= d18;
            d5 = d18 * d18 * NoiseGeneratorSimplex.func_151604_a(field_151611_e[n7], d13, d7);
        }
        double d19 = 0.5 - d14 * d14 - d15 * d15;
        if (d19 < 0.0) {
            d4 = 0.0;
        } else {
            d19 *= d19;
            d4 = d19 * d19 * NoiseGeneratorSimplex.func_151604_a(field_151611_e[n8], d14, d15);
        }
        double d20 = 0.5 - d16 * d16 - d17 * d17;
        if (d20 < 0.0) {
            d3 = 0.0;
        } else {
            d20 *= d20;
            d3 = d20 * d20 * NoiseGeneratorSimplex.func_151604_a(field_151611_e[n9], d16, d17);
        }
        return 70.0 * (d5 + d4 + d3);
    }

    public NoiseGeneratorSimplex(Random random) {
        this.field_151612_b = random.nextDouble() * 256.0;
        this.field_151613_c = random.nextDouble() * 256.0;
        this.field_151610_d = random.nextDouble() * 256.0;
        int n = 0;
        while (n < 256) {
            this.field_151608_f[n] = n++;
        }
        n = 0;
        while (n < 256) {
            int n2 = random.nextInt(256 - n) + n;
            int n3 = this.field_151608_f[n];
            this.field_151608_f[n] = this.field_151608_f[n2];
            this.field_151608_f[n2] = n3;
            this.field_151608_f[n + 256] = this.field_151608_f[n];
            ++n;
        }
    }

    public NoiseGeneratorSimplex() {
        this(new Random());
    }

    public void func_151606_a(double[] dArray, double d, double d2, int n, int n2, double d3, double d4, double d5) {
        int n3 = 0;
        int n4 = 0;
        while (n4 < n2) {
            double d6 = (d2 + (double)n4) * d4 + this.field_151613_c;
            int n5 = 0;
            while (n5 < n) {
                int n6;
                double d7;
                double d8;
                double d9;
                int n7;
                int n8;
                double d10;
                double d11;
                int n9;
                double d12;
                double d13 = (d + (double)n5) * d3 + this.field_151612_b;
                double d14 = (d13 + d6) * field_151609_g;
                int n10 = NoiseGeneratorSimplex.func_151607_a(d13 + d14);
                double d15 = (double)n10 - (d12 = (double)(n10 + (n9 = NoiseGeneratorSimplex.func_151607_a(d6 + d14))) * field_151615_h);
                double d16 = d13 - d15;
                if (d16 > (d11 = d6 - (d10 = (double)n9 - d12))) {
                    n8 = 1;
                    n7 = 0;
                } else {
                    n8 = 0;
                    n7 = 1;
                }
                double d17 = d16 - (double)n8 + field_151615_h;
                double d18 = d11 - (double)n7 + field_151615_h;
                double d19 = d16 - 1.0 + 2.0 * field_151615_h;
                double d20 = d11 - 1.0 + 2.0 * field_151615_h;
                int n11 = n10 & 0xFF;
                int n12 = n9 & 0xFF;
                int n13 = this.field_151608_f[n11 + this.field_151608_f[n12]] % 12;
                int n14 = this.field_151608_f[n11 + n8 + this.field_151608_f[n12 + n7]] % 12;
                int n15 = this.field_151608_f[n11 + 1 + this.field_151608_f[n12 + 1]] % 12;
                double d21 = 0.5 - d16 * d16 - d11 * d11;
                if (d21 < 0.0) {
                    d9 = 0.0;
                } else {
                    d21 *= d21;
                    d9 = d21 * d21 * NoiseGeneratorSimplex.func_151604_a(field_151611_e[n13], d16, d11);
                }
                double d22 = 0.5 - d17 * d17 - d18 * d18;
                if (d22 < 0.0) {
                    d8 = 0.0;
                } else {
                    d22 *= d22;
                    d8 = d22 * d22 * NoiseGeneratorSimplex.func_151604_a(field_151611_e[n14], d17, d18);
                }
                double d23 = 0.5 - d19 * d19 - d20 * d20;
                if (d23 < 0.0) {
                    d7 = 0.0;
                } else {
                    d23 *= d23;
                    d7 = d23 * d23 * NoiseGeneratorSimplex.func_151604_a(field_151611_e[n15], d19, d20);
                }
                int n16 = n6 = n3++;
                dArray[n16] = dArray[n16] + 70.0 * (d9 + d8 + d7) * d5;
                ++n5;
            }
            ++n4;
        }
    }

    private static int func_151607_a(double d) {
        return d > 0.0 ? (int)d : (int)d - 1;
    }

    static {
        int[][] nArrayArray = new int[12][];
        int[] nArray = new int[3];
        nArray[0] = 1;
        nArray[1] = 1;
        nArrayArray[0] = nArray;
        int[] nArray2 = new int[3];
        nArray2[0] = -1;
        nArray2[1] = 1;
        nArrayArray[1] = nArray2;
        int[] nArray3 = new int[3];
        nArray3[0] = 1;
        nArray3[1] = -1;
        nArrayArray[2] = nArray3;
        int[] nArray4 = new int[3];
        nArray4[0] = -1;
        nArray4[1] = -1;
        nArrayArray[3] = nArray4;
        int[] nArray5 = new int[3];
        nArray5[0] = 1;
        nArray5[2] = 1;
        nArrayArray[4] = nArray5;
        int[] nArray6 = new int[3];
        nArray6[0] = -1;
        nArray6[2] = 1;
        nArrayArray[5] = nArray6;
        int[] nArray7 = new int[3];
        nArray7[0] = 1;
        nArray7[2] = -1;
        nArrayArray[6] = nArray7;
        int[] nArray8 = new int[3];
        nArray8[0] = -1;
        nArray8[2] = -1;
        nArrayArray[7] = nArray8;
        int[] nArray9 = new int[3];
        nArray9[1] = 1;
        nArray9[2] = 1;
        nArrayArray[8] = nArray9;
        int[] nArray10 = new int[3];
        nArray10[1] = -1;
        nArray10[2] = 1;
        nArrayArray[9] = nArray10;
        int[] nArray11 = new int[3];
        nArray11[1] = 1;
        nArray11[2] = -1;
        nArrayArray[10] = nArray11;
        int[] nArray12 = new int[3];
        nArray12[1] = -1;
        nArray12[2] = -1;
        nArrayArray[11] = nArray12;
        field_151611_e = nArrayArray;
        field_151614_a = Math.sqrt(3.0);
        field_151609_g = 0.5 * (field_151614_a - 1.0);
        field_151615_h = (3.0 - field_151614_a) / 6.0;
    }

    private static double func_151604_a(int[] nArray, double d, double d2) {
        return (double)nArray[0] * d + (double)nArray[1] * d2;
    }
}

