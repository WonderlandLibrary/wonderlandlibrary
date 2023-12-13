/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public abstract class WorldGenHugeTrees
extends WorldGenAbstractTree {
    protected final int baseHeight;
    protected final IBlockState woodMetadata;
    protected final IBlockState leavesMetadata;
    protected int extraRandomHeight;

    public WorldGenHugeTrees(boolean p_i46447_1_, int p_i46447_2_, int p_i46447_3_, IBlockState p_i46447_4_, IBlockState p_i46447_5_) {
        super(p_i46447_1_);
        this.baseHeight = p_i46447_2_;
        this.extraRandomHeight = p_i46447_3_;
        this.woodMetadata = p_i46447_4_;
        this.leavesMetadata = p_i46447_5_;
    }

    protected int func_150533_a(Random p_150533_1_) {
        int i2 = p_150533_1_.nextInt(3) + this.baseHeight;
        if (this.extraRandomHeight > 1) {
            i2 += p_150533_1_.nextInt(this.extraRandomHeight);
        }
        return i2;
    }

    private boolean func_175926_c(World worldIn, BlockPos p_175926_2_, int p_175926_3_) {
        boolean flag = true;
        if (p_175926_2_.getY() >= 1 && p_175926_2_.getY() + p_175926_3_ + 1 <= 256) {
            for (int i2 = 0; i2 <= 1 + p_175926_3_; ++i2) {
                int j2 = 2;
                if (i2 == 0) {
                    j2 = 1;
                } else if (i2 >= 1 + p_175926_3_ - 2) {
                    j2 = 2;
                }
                for (int k2 = -j2; k2 <= j2 && flag; ++k2) {
                    for (int l2 = -j2; l2 <= j2 && flag; ++l2) {
                        if (p_175926_2_.getY() + i2 >= 0 && p_175926_2_.getY() + i2 < 256 && this.func_150523_a(worldIn.getBlockState(p_175926_2_.add(k2, i2, l2)).getBlock())) continue;
                        flag = false;
                    }
                }
            }
            return flag;
        }
        return false;
    }

    private boolean func_175927_a(BlockPos p_175927_1_, World worldIn) {
        BlockPos blockpos = p_175927_1_.down();
        Block block = worldIn.getBlockState(blockpos).getBlock();
        if ((block == Blocks.grass || block == Blocks.dirt) && p_175927_1_.getY() >= 2) {
            this.func_175921_a(worldIn, blockpos);
            this.func_175921_a(worldIn, blockpos.east());
            this.func_175921_a(worldIn, blockpos.south());
            this.func_175921_a(worldIn, blockpos.south().east());
            return true;
        }
        return false;
    }

    protected boolean func_175929_a(World worldIn, Random p_175929_2_, BlockPos p_175929_3_, int p_175929_4_) {
        return this.func_175926_c(worldIn, p_175929_3_, p_175929_4_) && this.func_175927_a(p_175929_3_, worldIn);
    }

    protected void func_175925_a(World worldIn, BlockPos p_175925_2_, int p_175925_3_) {
        int i2 = p_175925_3_ * p_175925_3_;
        for (int j2 = -p_175925_3_; j2 <= p_175925_3_ + 1; ++j2) {
            for (int k2 = -p_175925_3_; k2 <= p_175925_3_ + 1; ++k2) {
                BlockPos blockpos;
                Material material;
                int l2 = j2 - 1;
                int i1 = k2 - 1;
                if (j2 * j2 + k2 * k2 > i2 && l2 * l2 + i1 * i1 > i2 && j2 * j2 + i1 * i1 > i2 && l2 * l2 + k2 * k2 > i2 || (material = worldIn.getBlockState(blockpos = p_175925_2_.add(j2, 0, k2)).getBlock().getMaterial()) != Material.air && material != Material.leaves) continue;
                this.setBlockAndNotifyAdequately(worldIn, blockpos, this.leavesMetadata);
            }
        }
    }

    protected void func_175928_b(World worldIn, BlockPos p_175928_2_, int p_175928_3_) {
        int i2 = p_175928_3_ * p_175928_3_;
        for (int j2 = -p_175928_3_; j2 <= p_175928_3_; ++j2) {
            for (int k2 = -p_175928_3_; k2 <= p_175928_3_; ++k2) {
                BlockPos blockpos;
                Material material;
                if (j2 * j2 + k2 * k2 > i2 || (material = worldIn.getBlockState(blockpos = p_175928_2_.add(j2, 0, k2)).getBlock().getMaterial()) != Material.air && material != Material.leaves) continue;
                this.setBlockAndNotifyAdequately(worldIn, blockpos, this.leavesMetadata);
            }
        }
    }
}

