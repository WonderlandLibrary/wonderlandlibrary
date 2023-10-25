/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.world.gen.NoiseGenerator;

public class NoiseGeneratorImproved
extends NoiseGenerator {
    private final int[] permutations = new int[512];
    public double xCoord;
    public double yCoord;
    public double zCoord;
    private static final double[] GRAD_X = new double[]{1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, -1.0, 0.0};
    private static final double[] GRAD_Y = new double[]{1.0, 1.0, -1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0};
    private static final double[] GRAD_Z = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, -1.0, -1.0, 0.0, 1.0, 0.0, -1.0};
    private static final double[] GRAD_2X = new double[]{1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, -1.0, 0.0};
    private static final double[] GRAD_2Z = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, -1.0, -1.0, 0.0, 1.0, 0.0, -1.0};

    public NoiseGeneratorImproved() {
        this(new Random());
    }

    public NoiseGeneratorImproved(Random p_i45469_1_) {
        this.xCoord = p_i45469_1_.nextDouble() * 256.0;
        this.yCoord = p_i45469_1_.nextDouble() * 256.0;
        this.zCoord = p_i45469_1_.nextDouble() * 256.0;
        int i = 0;
        while (i < 256) {
            this.permutations[i] = i++;
        }
        for (int l = 0; l < 256; ++l) {
            int j = p_i45469_1_.nextInt(256 - l) + l;
            int k = this.permutations[l];
            this.permutations[l] = this.permutations[j];
            this.permutations[j] = k;
            this.permutations[l + 256] = this.permutations[l];
        }
    }

    public final double lerp(double p_76311_1_, double p_76311_3_, double p_76311_5_) {
        return p_76311_3_ + p_76311_1_ * (p_76311_5_ - p_76311_3_);
    }

    public final double grad2(int p_76309_1_, double p_76309_2_, double p_76309_4_) {
        int i = p_76309_1_ & 0xF;
        return GRAD_2X[i] * p_76309_2_ + GRAD_2Z[i] * p_76309_4_;
    }

    public final double grad(int p_76310_1_, double p_76310_2_, double p_76310_4_, double p_76310_6_) {
        int i = p_76310_1_ & 0xF;
        return GRAD_X[i] * p_76310_2_ + GRAD_Y[i] * p_76310_4_ + GRAD_Z[i] * p_76310_6_;
    }

    public void populateNoiseArray(double[] noiseArray, double xOffset, double yOffset, double zOffset, int xSize, int ySize, int zSize, double xScale, double yScale, double zScale, double noiseScale) {
        if (ySize == 1) {
            int i5 = 0;
            int j5 = 0;
            int j = 0;
            int k5 = 0;
            double d14 = 0.0;
            double d15 = 0.0;
            int l5 = 0;
            double d16 = 1.0 / noiseScale;
            for (int j2 = 0; j2 < xSize; ++j2) {
                double d17 = xOffset + (double)j2 * xScale + this.xCoord;
                int i6 = (int)d17;
                if (d17 < (double)i6) {
                    --i6;
                }
                int k2 = i6 & 0xFF;
                double d18 = (d17 -= (double)i6) * d17 * d17 * (d17 * (d17 * 6.0 - 15.0) + 10.0);
                for (int j6 = 0; j6 < zSize; ++j6) {
                    int i7;
                    double d19 = zOffset + (double)j6 * zScale + this.zCoord;
                    int k6 = (int)d19;
                    if (d19 < (double)k6) {
                        --k6;
                    }
                    int l6 = k6 & 0xFF;
                    double d20 = (d19 -= (double)k6) * d19 * d19 * (d19 * (d19 * 6.0 - 15.0) + 10.0);
                    i5 = this.permutations[k2] + 0;
                    j5 = this.permutations[i5] + l6;
                    j = this.permutations[k2 + 1] + 0;
                    k5 = this.permutations[j] + l6;
                    d14 = this.lerp(d18, this.grad2(this.permutations[j5], d17, d19), this.grad(this.permutations[k5], d17 - 1.0, 0.0, d19));
                    d15 = this.lerp(d18, this.grad(this.permutations[j5 + 1], d17, 0.0, d19 - 1.0), this.grad(this.permutations[k5 + 1], d17 - 1.0, 0.0, d19 - 1.0));
                    double d21 = this.lerp(d20, d14, d15);
                    int n = i7 = l5++;
                    noiseArray[n] = noiseArray[n] + d21 * d16;
                }
            }
        } else {
            int i = 0;
            double d0 = 1.0 / noiseScale;
            int k = -1;
            int l = 0;
            int i1 = 0;
            int j1 = 0;
            int k1 = 0;
            int l1 = 0;
            int i2 = 0;
            double d1 = 0.0;
            double d2 = 0.0;
            double d3 = 0.0;
            double d4 = 0.0;
            for (int l2 = 0; l2 < xSize; ++l2) {
                double d5 = xOffset + (double)l2 * xScale + this.xCoord;
                int i3 = (int)d5;
                if (d5 < (double)i3) {
                    --i3;
                }
                int j3 = i3 & 0xFF;
                double d6 = (d5 -= (double)i3) * d5 * d5 * (d5 * (d5 * 6.0 - 15.0) + 10.0);
                for (int k3 = 0; k3 < zSize; ++k3) {
                    double d7 = zOffset + (double)k3 * zScale + this.zCoord;
                    int l3 = (int)d7;
                    if (d7 < (double)l3) {
                        --l3;
                    }
                    int i4 = l3 & 0xFF;
                    double d8 = (d7 -= (double)l3) * d7 * d7 * (d7 * (d7 * 6.0 - 15.0) + 10.0);
                    for (int j4 = 0; j4 < ySize; ++j4) {
                        int j7;
                        double d9 = yOffset + (double)j4 * yScale + this.yCoord;
                        int k4 = (int)d9;
                        if (d9 < (double)k4) {
                            --k4;
                        }
                        int l4 = k4 & 0xFF;
                        double d10 = (d9 -= (double)k4) * d9 * d9 * (d9 * (d9 * 6.0 - 15.0) + 10.0);
                        if (j4 == 0 || l4 != k) {
                            k = l4;
                            l = this.permutations[j3] + l4;
                            i1 = this.permutations[l] + i4;
                            j1 = this.permutations[l + 1] + i4;
                            k1 = this.permutations[j3 + 1] + l4;
                            l1 = this.permutations[k1] + i4;
                            i2 = this.permutations[k1 + 1] + i4;
                            d1 = this.lerp(d6, this.grad(this.permutations[i1], d5, d9, d7), this.grad(this.permutations[l1], d5 - 1.0, d9, d7));
                            d2 = this.lerp(d6, this.grad(this.permutations[j1], d5, d9 - 1.0, d7), this.grad(this.permutations[i2], d5 - 1.0, d9 - 1.0, d7));
                            d3 = this.lerp(d6, this.grad(this.permutations[i1 + 1], d5, d9, d7 - 1.0), this.grad(this.permutations[l1 + 1], d5 - 1.0, d9, d7 - 1.0));
                            d4 = this.lerp(d6, this.grad(this.permutations[j1 + 1], d5, d9 - 1.0, d7 - 1.0), this.grad(this.permutations[i2 + 1], d5 - 1.0, d9 - 1.0, d7 - 1.0));
                        }
                        double d11 = this.lerp(d10, d1, d2);
                        double d12 = this.lerp(d10, d3, d4);
                        double d13 = this.lerp(d8, d11, d12);
                        int n = j7 = i++;
                        noiseArray[n] = noiseArray[n] + d13 * d0;
                    }
                }
            }
        }
    }
}

