/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;

public class MapGenRavine
extends MapGenBase {
    private float[] field_75046_d = new float[1024];

    protected void func_180707_a(long l, int n, int n2, ChunkPrimer chunkPrimer, double d, double d2, double d3, float f, float f2, float f3, int n3, int n4, double d4) {
        int n5;
        Random random = new Random(l);
        double d5 = n * 16 + 8;
        double d6 = n2 * 16 + 8;
        float f4 = 0.0f;
        float f5 = 0.0f;
        if (n4 <= 0) {
            n5 = this.range * 16 - 16;
            n4 = n5 - random.nextInt(n5 / 4);
        }
        n5 = 0;
        if (n3 == -1) {
            n3 = n4 / 2;
            n5 = 1;
        }
        float f6 = 1.0f;
        int n6 = 0;
        while (n6 < 256) {
            if (n6 == 0 || random.nextInt(3) == 0) {
                f6 = 1.0f + random.nextFloat() * random.nextFloat() * 1.0f;
            }
            this.field_75046_d[n6] = f6 * f6;
            ++n6;
        }
        while (n3 < n4) {
            double d7 = 1.5 + (double)(MathHelper.sin((float)n3 * (float)Math.PI / (float)n4) * f * 1.0f);
            double d8 = d7 * d4;
            d7 *= (double)random.nextFloat() * 0.25 + 0.75;
            d8 *= (double)random.nextFloat() * 0.25 + 0.75;
            float f7 = MathHelper.cos(f3);
            float f8 = MathHelper.sin(f3);
            d += (double)(MathHelper.cos(f2) * f7);
            d2 += (double)f8;
            d3 += (double)(MathHelper.sin(f2) * f7);
            f3 *= 0.7f;
            f3 += f5 * 0.05f;
            f2 += f4 * 0.05f;
            f5 *= 0.8f;
            f4 *= 0.5f;
            f5 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0f;
            f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0f;
            if (n5 != 0 || random.nextInt(4) != 0) {
                double d9 = d - d5;
                double d10 = d3 - d6;
                double d11 = n4 - n3;
                double d12 = f + 2.0f + 16.0f;
                if (d9 * d9 + d10 * d10 - d11 * d11 > d12 * d12) {
                    return;
                }
                if (d >= d5 - 16.0 - d7 * 2.0 && d3 >= d6 - 16.0 - d7 * 2.0 && d <= d5 + 16.0 + d7 * 2.0 && d3 <= d6 + 16.0 + d7 * 2.0) {
                    int n7;
                    int n8 = MathHelper.floor_double(d - d7) - n * 16 - 1;
                    int n9 = MathHelper.floor_double(d + d7) - n * 16 + 1;
                    int n10 = MathHelper.floor_double(d2 - d8) - 1;
                    int n11 = MathHelper.floor_double(d2 + d8) + 1;
                    int n12 = MathHelper.floor_double(d3 - d7) - n2 * 16 - 1;
                    int n13 = MathHelper.floor_double(d3 + d7) - n2 * 16 + 1;
                    if (n8 < 0) {
                        n8 = 0;
                    }
                    if (n9 > 16) {
                        n9 = 16;
                    }
                    if (n10 < 1) {
                        n10 = 1;
                    }
                    if (n11 > 248) {
                        n11 = 248;
                    }
                    if (n12 < 0) {
                        n12 = 0;
                    }
                    if (n13 > 16) {
                        n13 = 16;
                    }
                    boolean bl = false;
                    int n14 = n8;
                    while (!bl && n14 < n9) {
                        n7 = n12;
                        while (!bl && n7 < n13) {
                            int n15 = n11 + 1;
                            while (!bl && n15 >= n10 - 1) {
                                if (n15 >= 0 && n15 < 256) {
                                    IBlockState iBlockState = chunkPrimer.getBlockState(n14, n15, n7);
                                    if (iBlockState.getBlock() == Blocks.flowing_water || iBlockState.getBlock() == Blocks.water) {
                                        bl = true;
                                    }
                                    if (n15 != n10 - 1 && n14 != n8 && n14 != n9 - 1 && n7 != n12 && n7 != n13 - 1) {
                                        n15 = n10;
                                    }
                                }
                                --n15;
                            }
                            ++n7;
                        }
                        ++n14;
                    }
                    if (!bl) {
                        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                        n7 = n8;
                        while (n7 < n9) {
                            double d13 = ((double)(n7 + n * 16) + 0.5 - d) / d7;
                            int n16 = n12;
                            while (n16 < n13) {
                                double d14 = ((double)(n16 + n2 * 16) + 0.5 - d3) / d7;
                                boolean bl2 = false;
                                if (d13 * d13 + d14 * d14 < 1.0) {
                                    int n17 = n11;
                                    while (n17 > n10) {
                                        double d15 = ((double)(n17 - 1) + 0.5 - d2) / d8;
                                        if ((d13 * d13 + d14 * d14) * (double)this.field_75046_d[n17 - 1] + d15 * d15 / 6.0 < 1.0) {
                                            IBlockState iBlockState = chunkPrimer.getBlockState(n7, n17, n16);
                                            if (iBlockState.getBlock() == Blocks.grass) {
                                                bl2 = true;
                                            }
                                            if (iBlockState.getBlock() == Blocks.stone || iBlockState.getBlock() == Blocks.dirt || iBlockState.getBlock() == Blocks.grass) {
                                                if (n17 - 1 < 10) {
                                                    chunkPrimer.setBlockState(n7, n17, n16, Blocks.flowing_lava.getDefaultState());
                                                } else {
                                                    chunkPrimer.setBlockState(n7, n17, n16, Blocks.air.getDefaultState());
                                                    if (bl2 && chunkPrimer.getBlockState(n7, n17 - 1, n16).getBlock() == Blocks.dirt) {
                                                        mutableBlockPos.func_181079_c(n7 + n * 16, 0, n16 + n2 * 16);
                                                        chunkPrimer.setBlockState(n7, n17 - 1, n16, this.worldObj.getBiomeGenForCoords((BlockPos)mutableBlockPos).topBlock);
                                                    }
                                                }
                                            }
                                        }
                                        --n17;
                                    }
                                }
                                ++n16;
                            }
                            ++n7;
                        }
                        if (n5 != 0) break;
                    }
                }
            }
            ++n3;
        }
    }

    @Override
    protected void recursiveGenerate(World world, int n, int n2, int n3, int n4, ChunkPrimer chunkPrimer) {
        if (this.rand.nextInt(50) == 0) {
            double d = n * 16 + this.rand.nextInt(16);
            double d2 = this.rand.nextInt(this.rand.nextInt(40) + 8) + 20;
            double d3 = n2 * 16 + this.rand.nextInt(16);
            int n5 = 1;
            int n6 = 0;
            while (n6 < n5) {
                float f = this.rand.nextFloat() * (float)Math.PI * 2.0f;
                float f2 = (this.rand.nextFloat() - 0.5f) * 2.0f / 8.0f;
                float f3 = (this.rand.nextFloat() * 2.0f + this.rand.nextFloat()) * 2.0f;
                this.func_180707_a(this.rand.nextLong(), n3, n4, chunkPrimer, d, d2, d3, f3, f, f2, 0, 0, 3.0);
                ++n6;
            }
        }
    }
}

