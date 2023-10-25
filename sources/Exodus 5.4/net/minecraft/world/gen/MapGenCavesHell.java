/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;

public class MapGenCavesHell
extends MapGenBase {
    @Override
    protected void recursiveGenerate(World world, int n, int n2, int n3, int n4, ChunkPrimer chunkPrimer) {
        int n5 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(10) + 1) + 1);
        if (this.rand.nextInt(5) != 0) {
            n5 = 0;
        }
        int n6 = 0;
        while (n6 < n5) {
            double d = n * 16 + this.rand.nextInt(16);
            double d2 = this.rand.nextInt(128);
            double d3 = n2 * 16 + this.rand.nextInt(16);
            int n7 = 1;
            if (this.rand.nextInt(4) == 0) {
                this.func_180705_a(this.rand.nextLong(), n3, n4, chunkPrimer, d, d2, d3);
                n7 += this.rand.nextInt(4);
            }
            int n8 = 0;
            while (n8 < n7) {
                float f = this.rand.nextFloat() * (float)Math.PI * 2.0f;
                float f2 = (this.rand.nextFloat() - 0.5f) * 2.0f / 8.0f;
                float f3 = this.rand.nextFloat() * 2.0f + this.rand.nextFloat();
                this.func_180704_a(this.rand.nextLong(), n3, n4, chunkPrimer, d, d2, d3, f3 * 2.0f, f, f2, 0, 0, 0.5);
                ++n8;
            }
            ++n6;
        }
    }

    protected void func_180705_a(long l, int n, int n2, ChunkPrimer chunkPrimer, double d, double d2, double d3) {
        this.func_180704_a(l, n, n2, chunkPrimer, d, d2, d3, 1.0f + this.rand.nextFloat() * 6.0f, 0.0f, 0.0f, -1, -1, 0.5);
    }

    protected void func_180704_a(long l, int n, int n2, ChunkPrimer chunkPrimer, double d, double d2, double d3, float f, float f2, float f3, int n3, int n4, double d4) {
        int n5;
        double d5 = n * 16 + 8;
        double d6 = n2 * 16 + 8;
        float f4 = 0.0f;
        float f5 = 0.0f;
        Random random = new Random(l);
        if (n4 <= 0) {
            n5 = this.range * 16 - 16;
            n4 = n5 - random.nextInt(n5 / 4);
        }
        n5 = 0;
        if (n3 == -1) {
            n3 = n4 / 2;
            n5 = 1;
        }
        int n6 = random.nextInt(n4 / 2) + n4 / 4;
        boolean bl = random.nextInt(6) == 0;
        while (n3 < n4) {
            double d7 = 1.5 + (double)(MathHelper.sin((float)n3 * (float)Math.PI / (float)n4) * f * 1.0f);
            double d8 = d7 * d4;
            float f6 = MathHelper.cos(f3);
            float f7 = MathHelper.sin(f3);
            d += (double)(MathHelper.cos(f2) * f6);
            d2 += (double)f7;
            d3 += (double)(MathHelper.sin(f2) * f6);
            f3 = bl ? (f3 *= 0.92f) : (f3 *= 0.7f);
            f3 += f5 * 0.1f;
            f2 += f4 * 0.1f;
            f5 *= 0.9f;
            f4 *= 0.75f;
            f5 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0f;
            f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0f;
            if (n5 == 0 && n3 == n6 && f > 1.0f) {
                this.func_180704_a(random.nextLong(), n, n2, chunkPrimer, d, d2, d3, random.nextFloat() * 0.5f + 0.5f, f2 - 1.5707964f, f3 / 3.0f, n3, n4, 1.0);
                this.func_180704_a(random.nextLong(), n, n2, chunkPrimer, d, d2, d3, random.nextFloat() * 0.5f + 0.5f, f2 + 1.5707964f, f3 / 3.0f, n3, n4, 1.0);
                return;
            }
            if (n5 != 0 || random.nextInt(4) != 0) {
                double d9 = d - d5;
                double d10 = d3 - d6;
                double d11 = n4 - n3;
                double d12 = f + 2.0f + 16.0f;
                if (d9 * d9 + d10 * d10 - d11 * d11 > d12 * d12) {
                    return;
                }
                if (d >= d5 - 16.0 - d7 * 2.0 && d3 >= d6 - 16.0 - d7 * 2.0 && d <= d5 + 16.0 + d7 * 2.0 && d3 <= d6 + 16.0 + d7 * 2.0) {
                    int n7 = MathHelper.floor_double(d - d7) - n * 16 - 1;
                    int n8 = MathHelper.floor_double(d + d7) - n * 16 + 1;
                    int n9 = MathHelper.floor_double(d2 - d8) - 1;
                    int n10 = MathHelper.floor_double(d2 + d8) + 1;
                    int n11 = MathHelper.floor_double(d3 - d7) - n2 * 16 - 1;
                    int n12 = MathHelper.floor_double(d3 + d7) - n2 * 16 + 1;
                    if (n7 < 0) {
                        n7 = 0;
                    }
                    if (n8 > 16) {
                        n8 = 16;
                    }
                    if (n9 < 1) {
                        n9 = 1;
                    }
                    if (n10 > 120) {
                        n10 = 120;
                    }
                    if (n11 < 0) {
                        n11 = 0;
                    }
                    if (n12 > 16) {
                        n12 = 16;
                    }
                    boolean bl2 = false;
                    int n13 = n7;
                    while (!bl2 && n13 < n8) {
                        int n14 = n11;
                        while (!bl2 && n14 < n12) {
                            int n15 = n10 + 1;
                            while (!bl2 && n15 >= n9 - 1) {
                                if (n15 >= 0 && n15 < 128) {
                                    IBlockState iBlockState = chunkPrimer.getBlockState(n13, n15, n14);
                                    if (iBlockState.getBlock() == Blocks.flowing_lava || iBlockState.getBlock() == Blocks.lava) {
                                        bl2 = true;
                                    }
                                    if (n15 != n9 - 1 && n13 != n7 && n13 != n8 - 1 && n14 != n11 && n14 != n12 - 1) {
                                        n15 = n9;
                                    }
                                }
                                --n15;
                            }
                            ++n14;
                        }
                        ++n13;
                    }
                    if (!bl2) {
                        n13 = n7;
                        while (n13 < n8) {
                            double d13 = ((double)(n13 + n * 16) + 0.5 - d) / d7;
                            int n16 = n11;
                            while (n16 < n12) {
                                double d14 = ((double)(n16 + n2 * 16) + 0.5 - d3) / d7;
                                int n17 = n10;
                                while (n17 > n9) {
                                    IBlockState iBlockState;
                                    double d15 = ((double)(n17 - 1) + 0.5 - d2) / d8;
                                    if (d15 > -0.7 && d13 * d13 + d15 * d15 + d14 * d14 < 1.0 && ((iBlockState = chunkPrimer.getBlockState(n13, n17, n16)).getBlock() == Blocks.netherrack || iBlockState.getBlock() == Blocks.dirt || iBlockState.getBlock() == Blocks.grass)) {
                                        chunkPrimer.setBlockState(n13, n17, n16, Blocks.air.getDefaultState());
                                    }
                                    --n17;
                                }
                                ++n16;
                            }
                            ++n13;
                        }
                        if (n5 != 0) break;
                    }
                }
            }
            ++n3;
        }
    }
}

