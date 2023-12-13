/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenHugeTrees;

public class WorldGenMegaJungle
extends WorldGenHugeTrees {
    public WorldGenMegaJungle(boolean p_i46448_1_, int p_i46448_2_, int p_i46448_3_, IBlockState p_i46448_4_, IBlockState p_i46448_5_) {
        super(p_i46448_1_, p_i46448_2_, p_i46448_3_, p_i46448_4_, p_i46448_5_);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int i2 = this.func_150533_a(rand);
        if (!this.func_175929_a(worldIn, rand, position, i2)) {
            return false;
        }
        this.func_175930_c(worldIn, position.up(i2), 2);
        for (int j2 = position.getY() + i2 - 2 - rand.nextInt(4); j2 > position.getY() + i2 / 2; j2 -= 2 + rand.nextInt(4)) {
            float f2 = rand.nextFloat() * (float)Math.PI * 2.0f;
            int k2 = position.getX() + (int)(0.5f + MathHelper.cos(f2) * 4.0f);
            int l2 = position.getZ() + (int)(0.5f + MathHelper.sin(f2) * 4.0f);
            for (int i1 = 0; i1 < 5; ++i1) {
                k2 = position.getX() + (int)(1.5f + MathHelper.cos(f2) * (float)i1);
                l2 = position.getZ() + (int)(1.5f + MathHelper.sin(f2) * (float)i1);
                this.setBlockAndNotifyAdequately(worldIn, new BlockPos(k2, j2 - 3 + i1 / 2, l2), this.woodMetadata);
            }
            int j22 = 1 + rand.nextInt(2);
            int j1 = j2;
            for (int k1 = j2 - j22; k1 <= j1; ++k1) {
                int l1 = k1 - j1;
                this.func_175928_b(worldIn, new BlockPos(k2, k1, l2), 1 - l1);
            }
        }
        for (int i22 = 0; i22 < i2; ++i22) {
            BlockPos blockpos3;
            BlockPos blockpos2;
            BlockPos blockpos = position.up(i22);
            if (this.func_150523_a(worldIn.getBlockState(blockpos).getBlock())) {
                this.setBlockAndNotifyAdequately(worldIn, blockpos, this.woodMetadata);
                if (i22 > 0) {
                    this.func_181632_a(worldIn, rand, blockpos.west(), BlockVine.EAST);
                    this.func_181632_a(worldIn, rand, blockpos.north(), BlockVine.SOUTH);
                }
            }
            if (i22 >= i2 - 1) continue;
            BlockPos blockpos1 = blockpos.east();
            if (this.func_150523_a(worldIn.getBlockState(blockpos1).getBlock())) {
                this.setBlockAndNotifyAdequately(worldIn, blockpos1, this.woodMetadata);
                if (i22 > 0) {
                    this.func_181632_a(worldIn, rand, blockpos1.east(), BlockVine.WEST);
                    this.func_181632_a(worldIn, rand, blockpos1.north(), BlockVine.SOUTH);
                }
            }
            if (this.func_150523_a(worldIn.getBlockState(blockpos2 = blockpos.south().east()).getBlock())) {
                this.setBlockAndNotifyAdequately(worldIn, blockpos2, this.woodMetadata);
                if (i22 > 0) {
                    this.func_181632_a(worldIn, rand, blockpos2.east(), BlockVine.WEST);
                    this.func_181632_a(worldIn, rand, blockpos2.south(), BlockVine.NORTH);
                }
            }
            if (!this.func_150523_a(worldIn.getBlockState(blockpos3 = blockpos.south()).getBlock())) continue;
            this.setBlockAndNotifyAdequately(worldIn, blockpos3, this.woodMetadata);
            if (i22 <= 0) continue;
            this.func_181632_a(worldIn, rand, blockpos3.west(), BlockVine.EAST);
            this.func_181632_a(worldIn, rand, blockpos3.south(), BlockVine.NORTH);
        }
        return true;
    }

    private void func_181632_a(World p_181632_1_, Random p_181632_2_, BlockPos p_181632_3_, PropertyBool p_181632_4_) {
        if (p_181632_2_.nextInt(3) > 0 && p_181632_1_.isAirBlock(p_181632_3_)) {
            this.setBlockAndNotifyAdequately(p_181632_1_, p_181632_3_, Blocks.vine.getDefaultState().withProperty(p_181632_4_, true));
        }
    }

    private void func_175930_c(World worldIn, BlockPos p_175930_2_, int p_175930_3_) {
        int i2 = 2;
        for (int j2 = -i2; j2 <= 0; ++j2) {
            this.func_175925_a(worldIn, p_175930_2_.up(j2), p_175930_3_ + 1 - j2);
        }
    }
}

