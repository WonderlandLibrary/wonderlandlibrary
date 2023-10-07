// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen;

import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import java.util.Random;
import net.minecraft.world.chunk.ChunkPrimer;

public class MapGenCavesHell extends MapGenBase
{
    private static final String __OBFID = "CL_00000395";
    
    protected void func_180705_a(final long p_180705_1_, final int p_180705_3_, final int p_180705_4_, final ChunkPrimer p_180705_5_, final double p_180705_6_, final double p_180705_8_, final double p_180705_10_) {
        this.func_180704_a(p_180705_1_, p_180705_3_, p_180705_4_, p_180705_5_, p_180705_6_, p_180705_8_, p_180705_10_, 1.0f + this.rand.nextFloat() * 6.0f, 0.0f, 0.0f, -1, -1, 0.5);
    }
    
    protected void func_180704_a(final long p_180704_1_, final int p_180704_3_, final int p_180704_4_, final ChunkPrimer p_180704_5_, double p_180704_6_, double p_180704_8_, double p_180704_10_, final float p_180704_12_, float p_180704_13_, float p_180704_14_, int p_180704_15_, int p_180704_16_, final double p_180704_17_) {
        final double var19 = p_180704_3_ * 16 + 8;
        final double var20 = p_180704_4_ * 16 + 8;
        float var21 = 0.0f;
        float var22 = 0.0f;
        final Random var23 = new Random(p_180704_1_);
        if (p_180704_16_ <= 0) {
            final int var24 = this.range * 16 - 16;
            p_180704_16_ = var24 - var23.nextInt(var24 / 4);
        }
        boolean var25 = false;
        if (p_180704_15_ == -1) {
            p_180704_15_ = p_180704_16_ / 2;
            var25 = true;
        }
        final int var26 = var23.nextInt(p_180704_16_ / 2) + p_180704_16_ / 4;
        final boolean var27 = var23.nextInt(6) == 0;
        while (p_180704_15_ < p_180704_16_) {
            final double var28 = 1.5 + MathHelper.sin(p_180704_15_ * 3.1415927f / p_180704_16_) * p_180704_12_ * 1.0f;
            final double var29 = var28 * p_180704_17_;
            final float var30 = MathHelper.cos(p_180704_14_);
            final float var31 = MathHelper.sin(p_180704_14_);
            p_180704_6_ += MathHelper.cos(p_180704_13_) * var30;
            p_180704_8_ += var31;
            p_180704_10_ += MathHelper.sin(p_180704_13_) * var30;
            if (var27) {
                p_180704_14_ *= 0.92f;
            }
            else {
                p_180704_14_ *= 0.7f;
            }
            p_180704_14_ += var22 * 0.1f;
            p_180704_13_ += var21 * 0.1f;
            var22 *= 0.9f;
            var21 *= 0.75f;
            var22 += (var23.nextFloat() - var23.nextFloat()) * var23.nextFloat() * 2.0f;
            var21 += (var23.nextFloat() - var23.nextFloat()) * var23.nextFloat() * 4.0f;
            if (!var25 && p_180704_15_ == var26 && p_180704_12_ > 1.0f) {
                this.func_180704_a(var23.nextLong(), p_180704_3_, p_180704_4_, p_180704_5_, p_180704_6_, p_180704_8_, p_180704_10_, var23.nextFloat() * 0.5f + 0.5f, p_180704_13_ - 1.5707964f, p_180704_14_ / 3.0f, p_180704_15_, p_180704_16_, 1.0);
                this.func_180704_a(var23.nextLong(), p_180704_3_, p_180704_4_, p_180704_5_, p_180704_6_, p_180704_8_, p_180704_10_, var23.nextFloat() * 0.5f + 0.5f, p_180704_13_ + 1.5707964f, p_180704_14_ / 3.0f, p_180704_15_, p_180704_16_, 1.0);
                return;
            }
            if (var25 || var23.nextInt(4) != 0) {
                final double var32 = p_180704_6_ - var19;
                final double var33 = p_180704_10_ - var20;
                final double var34 = p_180704_16_ - p_180704_15_;
                final double var35 = p_180704_12_ + 2.0f + 16.0f;
                if (var32 * var32 + var33 * var33 - var34 * var34 > var35 * var35) {
                    return;
                }
                if (p_180704_6_ >= var19 - 16.0 - var28 * 2.0 && p_180704_10_ >= var20 - 16.0 - var28 * 2.0 && p_180704_6_ <= var19 + 16.0 + var28 * 2.0 && p_180704_10_ <= var20 + 16.0 + var28 * 2.0) {
                    int var36 = MathHelper.floor_double(p_180704_6_ - var28) - p_180704_3_ * 16 - 1;
                    int var37 = MathHelper.floor_double(p_180704_6_ + var28) - p_180704_3_ * 16 + 1;
                    int var38 = MathHelper.floor_double(p_180704_8_ - var29) - 1;
                    int var39 = MathHelper.floor_double(p_180704_8_ + var29) + 1;
                    int var40 = MathHelper.floor_double(p_180704_10_ - var28) - p_180704_4_ * 16 - 1;
                    int var41 = MathHelper.floor_double(p_180704_10_ + var28) - p_180704_4_ * 16 + 1;
                    if (var36 < 0) {
                        var36 = 0;
                    }
                    if (var37 > 16) {
                        var37 = 16;
                    }
                    if (var38 < 1) {
                        var38 = 1;
                    }
                    if (var39 > 120) {
                        var39 = 120;
                    }
                    if (var40 < 0) {
                        var40 = 0;
                    }
                    if (var41 > 16) {
                        var41 = 16;
                    }
                    boolean var42 = false;
                    for (int var43 = var36; !var42 && var43 < var37; ++var43) {
                        for (int var44 = var40; !var42 && var44 < var41; ++var44) {
                            for (int var45 = var39 + 1; !var42 && var45 >= var38 - 1; --var45) {
                                if (var45 >= 0 && var45 < 128) {
                                    final IBlockState var46 = p_180704_5_.getBlockState(var43, var45, var44);
                                    if (var46.getBlock() == Blocks.flowing_lava || var46.getBlock() == Blocks.lava) {
                                        var42 = true;
                                    }
                                    if (var45 != var38 - 1 && var43 != var36 && var43 != var37 - 1 && var44 != var40 && var44 != var41 - 1) {
                                        var45 = var38;
                                    }
                                }
                            }
                        }
                    }
                    if (!var42) {
                        for (int var43 = var36; var43 < var37; ++var43) {
                            final double var47 = (var43 + p_180704_3_ * 16 + 0.5 - p_180704_6_) / var28;
                            for (int var48 = var40; var48 < var41; ++var48) {
                                final double var49 = (var48 + p_180704_4_ * 16 + 0.5 - p_180704_10_) / var28;
                                for (int var50 = var39; var50 > var38; --var50) {
                                    final double var51 = (var50 - 1 + 0.5 - p_180704_8_) / var29;
                                    if (var51 > -0.7 && var47 * var47 + var51 * var51 + var49 * var49 < 1.0) {
                                        final IBlockState var52 = p_180704_5_.getBlockState(var43, var50, var48);
                                        if (var52.getBlock() == Blocks.netherrack || var52.getBlock() == Blocks.dirt || var52.getBlock() == Blocks.grass) {
                                            p_180704_5_.setBlockState(var43, var50, var48, Blocks.air.getDefaultState());
                                        }
                                    }
                                }
                            }
                        }
                        if (var25) {
                            break;
                        }
                    }
                }
            }
            ++p_180704_15_;
        }
    }
    
    @Override
    protected void func_180701_a(final World worldIn, final int p_180701_2_, final int p_180701_3_, final int p_180701_4_, final int p_180701_5_, final ChunkPrimer p_180701_6_) {
        int var7 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(10) + 1) + 1);
        if (this.rand.nextInt(5) != 0) {
            var7 = 0;
        }
        for (int var8 = 0; var8 < var7; ++var8) {
            final double var9 = p_180701_2_ * 16 + this.rand.nextInt(16);
            final double var10 = this.rand.nextInt(128);
            final double var11 = p_180701_3_ * 16 + this.rand.nextInt(16);
            int var12 = 1;
            if (this.rand.nextInt(4) == 0) {
                this.func_180705_a(this.rand.nextLong(), p_180701_4_, p_180701_5_, p_180701_6_, var9, var10, var11);
                var12 += this.rand.nextInt(4);
            }
            for (int var13 = 0; var13 < var12; ++var13) {
                final float var14 = this.rand.nextFloat() * 3.1415927f * 2.0f;
                final float var15 = (this.rand.nextFloat() - 0.5f) * 2.0f / 8.0f;
                final float var16 = this.rand.nextFloat() * 2.0f + this.rand.nextFloat();
                this.func_180704_a(this.rand.nextLong(), p_180701_4_, p_180701_5_, p_180701_6_, var9, var10, var11, var16 * 2.0f, var14, var15, 0, 0, 0.5);
            }
        }
    }
}
