/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;

public class MapGenCavesHell
extends MapGenBase {
    protected void func_180705_a(long p_180705_1_, int p_180705_3_, int p_180705_4_, ChunkPrimer p_180705_5_, double p_180705_6_, double p_180705_8_, double p_180705_10_) {
        this.func_180704_a(p_180705_1_, p_180705_3_, p_180705_4_, p_180705_5_, p_180705_6_, p_180705_8_, p_180705_10_, 1.0f + this.rand.nextFloat() * 6.0f, 0.0f, 0.0f, -1, -1, 0.5);
    }

    /*
     * Unable to fully structure code
     */
    protected void func_180704_a(long p_180704_1_, int p_180704_3_, int p_180704_4_, ChunkPrimer p_180704_5_, double p_180704_6_, double p_180704_8_, double p_180704_10_, float p_180704_12_, float p_180704_13_, float p_180704_14_, int p_180704_15_, int p_180704_16_, double p_180704_17_) {
        d0 = p_180704_3_ * 16 + 8;
        d1 = p_180704_4_ * 16 + 8;
        f = 0.0f;
        f1 = 0.0f;
        random = new Random(p_180704_1_);
        if (p_180704_16_ <= 0) {
            i = this.range * 16 - 16;
            p_180704_16_ = i - random.nextInt(i / 4);
        }
        flag1 = false;
        if (p_180704_15_ == -1) {
            p_180704_15_ = p_180704_16_ / 2;
            flag1 = true;
        }
        j = random.nextInt(p_180704_16_ / 2) + p_180704_16_ / 4;
        flag = random.nextInt(6) == 0;
        block0: while (true) {
            if (p_180704_15_ >= p_180704_16_) return;
            d2 = 1.5 + (double)(MathHelper.sin((float)p_180704_15_ * 3.1415927f / (float)p_180704_16_) * p_180704_12_ * 1.0f);
            d3 = d2 * p_180704_17_;
            f2 = MathHelper.cos(p_180704_14_);
            f3 = MathHelper.sin(p_180704_14_);
            p_180704_6_ += (double)(MathHelper.cos(p_180704_13_) * f2);
            p_180704_8_ += (double)f3;
            p_180704_10_ += (double)(MathHelper.sin(p_180704_13_) * f2);
            p_180704_14_ = flag ? (p_180704_14_ *= 0.92f) : (p_180704_14_ *= 0.7f);
            p_180704_14_ += f1 * 0.1f;
            p_180704_13_ += f * 0.1f;
            f1 *= 0.9f;
            f *= 0.75f;
            f1 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0f;
            f += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0f;
            if (!flag1 && p_180704_15_ == j && p_180704_12_ > 1.0f) {
                this.func_180704_a(random.nextLong(), p_180704_3_, p_180704_4_, p_180704_5_, p_180704_6_, p_180704_8_, p_180704_10_, random.nextFloat() * 0.5f + 0.5f, p_180704_13_ - 1.5707964f, p_180704_14_ / 3.0f, p_180704_15_, p_180704_16_, 1.0);
                this.func_180704_a(random.nextLong(), p_180704_3_, p_180704_4_, p_180704_5_, p_180704_6_, p_180704_8_, p_180704_10_, random.nextFloat() * 0.5f + 0.5f, p_180704_13_ + 1.5707964f, p_180704_14_ / 3.0f, p_180704_15_, p_180704_16_, 1.0);
                return;
            }
            if (!flag1 && random.nextInt(4) == 0) ** GOTO lbl90
            d4 = p_180704_6_ - d0;
            d5 = p_180704_10_ - d1;
            d6 = p_180704_16_ - p_180704_15_;
            d7 = p_180704_12_ + 2.0f + 16.0f;
            if (d4 * d4 + d5 * d5 - d6 * d6 > d7 * d7) {
                return;
            }
            if (!(p_180704_6_ >= d0 - 16.0 - d2 * 2.0) || !(p_180704_10_ >= d1 - 16.0 - d2 * 2.0) || !(p_180704_6_ <= d0 + 16.0 + d2 * 2.0) || !(p_180704_10_ <= d1 + 16.0 + d2 * 2.0)) ** GOTO lbl90
            j2 = MathHelper.floor_double(p_180704_6_ - d2) - p_180704_3_ * 16 - 1;
            k = MathHelper.floor_double(p_180704_6_ + d2) - p_180704_3_ * 16 + 1;
            k2 = MathHelper.floor_double(p_180704_8_ - d3) - 1;
            l = MathHelper.floor_double(p_180704_8_ + d3) + 1;
            l2 = MathHelper.floor_double(p_180704_10_ - d2) - p_180704_4_ * 16 - 1;
            i1 = MathHelper.floor_double(p_180704_10_ + d2) - p_180704_4_ * 16 + 1;
            if (j2 < 0) {
                j2 = 0;
            }
            if (k > 16) {
                k = 16;
            }
            if (k2 < 1) {
                k2 = 1;
            }
            if (l > 120) {
                l = 120;
            }
            if (l2 < 0) {
                l2 = 0;
            }
            if (i1 > 16) {
                i1 = 16;
            }
            flag2 = false;
            j1 = j2;
            while (true) {
                block23: {
                    block22: {
                        if (flag2 || j1 >= k) break block22;
                        break block23;
                    }
                    if (!flag2) {
                        break;
                    }
                    ** GOTO lbl90
                }
                for (k1 = l2; !flag2 && k1 < i1; ++k1) {
                    for (l1 = l + 1; !flag2 && l1 >= k2 - 1; --l1) {
                        if (l1 < 0 || l1 >= 128) continue;
                        iblockstate = p_180704_5_.getBlockState(j1, l1, k1);
                        if (iblockstate.getBlock() == Blocks.flowing_lava || iblockstate.getBlock() == Blocks.lava) {
                            flag2 = true;
                        }
                        if (l1 == k2 - 1 || j1 == j2 || j1 == k - 1 || k1 == l2 || k1 == i1 - 1) continue;
                        l1 = k2;
                    }
                }
                ++j1;
            }
            i3 = j2;
            while (true) {
                if (i3 < k) {
                    d10 = ((double)(i3 + p_180704_3_ * 16) + 0.5 - p_180704_6_) / d2;
                } else {
                    if (flag1) {
                        return;
                    }
lbl90:
                    // 5 sources

                    ++p_180704_15_;
                    continue block0;
                }
                for (j3 = l2; j3 < i1; ++j3) {
                    d8 = ((double)(j3 + p_180704_4_ * 16) + 0.5 - p_180704_10_) / d2;
                    for (i2 = l; i2 > k2; --i2) {
                        d9 = ((double)(i2 - 1) + 0.5 - p_180704_8_) / d3;
                        if (!(d9 > -0.7) || !(d10 * d10 + d9 * d9 + d8 * d8 < 1.0) || (iblockstate1 = p_180704_5_.getBlockState(i3, i2, j3)).getBlock() != Blocks.netherrack && iblockstate1.getBlock() != Blocks.dirt && iblockstate1.getBlock() != Blocks.grass) continue;
                        p_180704_5_.setBlockState(i3, i2, j3, Blocks.air.getDefaultState());
                    }
                }
                ++i3;
            }
            break;
        }
    }

    @Override
    protected void recursiveGenerate(World worldIn, int chunkX, int chunkZ, int p_180701_4_, int p_180701_5_, ChunkPrimer chunkPrimerIn) {
        int i = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(10) + 1) + 1);
        if (this.rand.nextInt(5) != 0) {
            i = 0;
        }
        int j = 0;
        while (j < i) {
            double d0 = chunkX * 16 + this.rand.nextInt(16);
            double d1 = this.rand.nextInt(128);
            double d2 = chunkZ * 16 + this.rand.nextInt(16);
            int k = 1;
            if (this.rand.nextInt(4) == 0) {
                this.func_180705_a(this.rand.nextLong(), p_180701_4_, p_180701_5_, chunkPrimerIn, d0, d1, d2);
                k += this.rand.nextInt(4);
            }
            for (int l = 0; l < k; ++l) {
                float f = this.rand.nextFloat() * (float)Math.PI * 2.0f;
                float f1 = (this.rand.nextFloat() - 0.5f) * 2.0f / 8.0f;
                float f2 = this.rand.nextFloat() * 2.0f + this.rand.nextFloat();
                this.func_180704_a(this.rand.nextLong(), p_180701_4_, p_180701_5_, chunkPrimerIn, d0, d1, d2, f2 * 2.0f, f, f1, 0, 0, 0.5);
            }
            ++j;
        }
    }
}

