/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.world.gen.NoiseGenerator;

public class NoiseGeneratorImproved
extends NoiseGenerator {
    private static final double[] field_152384_h;
    private static final double[] field_152385_i;
    public double xCoord;
    private static final double[] field_152381_e;
    private static final double[] field_152383_g;
    public double yCoord;
    private static final double[] field_152382_f;
    public double zCoord;
    private int[] permutations = new int[512];

    public final double func_76309_a(int n, double d, double d2) {
        int n2 = n & 0xF;
        return field_152384_h[n2] * d + field_152385_i[n2] * d2;
    }

    static {
        field_152381_e = new double[]{1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, -1.0, 0.0};
        field_152382_f = new double[]{1.0, 1.0, -1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0};
        field_152383_g = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, -1.0, -1.0, 0.0, 1.0, 0.0, -1.0};
        field_152384_h = new double[]{1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, -1.0, 0.0};
        field_152385_i = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, -1.0, -1.0, 0.0, 1.0, 0.0, -1.0};
    }

    public NoiseGeneratorImproved(Random random) {
        this.xCoord = random.nextDouble() * 256.0;
        this.yCoord = random.nextDouble() * 256.0;
        this.zCoord = random.nextDouble() * 256.0;
        int n = 0;
        while (n < 256) {
            this.permutations[n] = n++;
        }
        n = 0;
        while (n < 256) {
            int n2 = random.nextInt(256 - n) + n;
            int n3 = this.permutations[n];
            this.permutations[n] = this.permutations[n2];
            this.permutations[n2] = n3;
            this.permutations[n + 256] = this.permutations[n];
            ++n;
        }
    }

    public final double lerp(double d, double d2, double d3) {
        return d2 + d * (d3 - d2);
    }

    public final double grad(int n, double d, double d2, double d3) {
        int n2 = n & 0xF;
        return field_152381_e[n2] * d + field_152382_f[n2] * d2 + field_152383_g[n2] * d3;
    }

    public void populateNoiseArray(double[] dArray, double d, double d2, double d3, int n, int n2, int n3, double d4, double d5, double d6, double d7) {
        if (n2 == 1) {
            int n4 = 0;
            int n5 = 0;
            int n6 = 0;
            int n7 = 0;
            double d8 = 0.0;
            double d9 = 0.0;
            int n8 = 0;
            double d10 = 1.0 / d7;
            int n9 = 0;
            while (n9 < n) {
                double d11 = d + (double)n9 * d4 + this.xCoord;
                int n10 = (int)d11;
                if (d11 < (double)n10) {
                    --n10;
                }
                int n11 = n10 & 0xFF;
                double d12 = (d11 -= (double)n10) * d11 * d11 * (d11 * (d11 * 6.0 - 15.0) + 10.0);
                int n12 = 0;
                while (n12 < n3) {
                    int n13;
                    double d13 = d3 + (double)n12 * d6 + this.zCoord;
                    int n14 = (int)d13;
                    if (d13 < (double)n14) {
                        --n14;
                    }
                    int n15 = n14 & 0xFF;
                    double d14 = (d13 -= (double)n14) * d13 * d13 * (d13 * (d13 * 6.0 - 15.0) + 10.0);
                    n4 = this.permutations[n11] + 0;
                    n5 = this.permutations[n4] + n15;
                    n6 = this.permutations[n11 + 1] + 0;
                    n7 = this.permutations[n6] + n15;
                    d8 = this.lerp(d12, this.func_76309_a(this.permutations[n5], d11, d13), this.grad(this.permutations[n7], d11 - 1.0, 0.0, d13));
                    d9 = this.lerp(d12, this.grad(this.permutations[n5 + 1], d11, 0.0, d13 - 1.0), this.grad(this.permutations[n7 + 1], d11 - 1.0, 0.0, d13 - 1.0));
                    double d15 = this.lerp(d14, d8, d9);
                    int n16 = n13 = n8++;
                    dArray[n16] = dArray[n16] + d15 * d10;
                    ++n12;
                }
                ++n9;
            }
        } else {
            int n17 = 0;
            double d16 = 1.0 / d7;
            int n18 = -1;
            int n19 = 0;
            int n20 = 0;
            int n21 = 0;
            int n22 = 0;
            int n23 = 0;
            int n24 = 0;
            double d17 = 0.0;
            double d18 = 0.0;
            double d19 = 0.0;
            double d20 = 0.0;
            int n25 = 0;
            while (n25 < n) {
                double d21 = d + (double)n25 * d4 + this.xCoord;
                int n26 = (int)d21;
                if (d21 < (double)n26) {
                    --n26;
                }
                int n27 = n26 & 0xFF;
                double d22 = (d21 -= (double)n26) * d21 * d21 * (d21 * (d21 * 6.0 - 15.0) + 10.0);
                int n28 = 0;
                while (n28 < n3) {
                    double d23 = d3 + (double)n28 * d6 + this.zCoord;
                    int n29 = (int)d23;
                    if (d23 < (double)n29) {
                        --n29;
                    }
                    int n30 = n29 & 0xFF;
                    double d24 = (d23 -= (double)n29) * d23 * d23 * (d23 * (d23 * 6.0 - 15.0) + 10.0);
                    int n31 = 0;
                    while (n31 < n2) {
                        int n32;
                        double d25 = d2 + (double)n31 * d5 + this.yCoord;
                        int n33 = (int)d25;
                        if (d25 < (double)n33) {
                            --n33;
                        }
                        int n34 = n33 & 0xFF;
                        double d26 = (d25 -= (double)n33) * d25 * d25 * (d25 * (d25 * 6.0 - 15.0) + 10.0);
                        if (n31 == 0 || n34 != n18) {
                            n18 = n34;
                            n19 = this.permutations[n27] + n34;
                            n20 = this.permutations[n19] + n30;
                            n21 = this.permutations[n19 + 1] + n30;
                            n22 = this.permutations[n27 + 1] + n34;
                            n23 = this.permutations[n22] + n30;
                            n24 = this.permutations[n22 + 1] + n30;
                            d17 = this.lerp(d22, this.grad(this.permutations[n20], d21, d25, d23), this.grad(this.permutations[n23], d21 - 1.0, d25, d23));
                            d18 = this.lerp(d22, this.grad(this.permutations[n21], d21, d25 - 1.0, d23), this.grad(this.permutations[n24], d21 - 1.0, d25 - 1.0, d23));
                            d19 = this.lerp(d22, this.grad(this.permutations[n20 + 1], d21, d25, d23 - 1.0), this.grad(this.permutations[n23 + 1], d21 - 1.0, d25, d23 - 1.0));
                            d20 = this.lerp(d22, this.grad(this.permutations[n21 + 1], d21, d25 - 1.0, d23 - 1.0), this.grad(this.permutations[n24 + 1], d21 - 1.0, d25 - 1.0, d23 - 1.0));
                        }
                        double d27 = this.lerp(d26, d17, d18);
                        double d28 = this.lerp(d26, d19, d20);
                        double d29 = this.lerp(d24, d27, d28);
                        int n35 = n32 = n17++;
                        dArray[n35] = dArray[n35] + d29 * d16;
                        ++n31;
                    }
                    ++n28;
                }
                ++n25;
            }
        }
    }

    public NoiseGeneratorImproved() {
        this(new Random());
    }
}

