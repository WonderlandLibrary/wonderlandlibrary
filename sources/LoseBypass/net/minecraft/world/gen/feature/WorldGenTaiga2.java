/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenTaiga2
extends WorldGenAbstractTree {
    private static final IBlockState field_181645_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
    private static final IBlockState field_181646_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, false);

    public WorldGenTaiga2(boolean p_i2025_1_) {
        super(p_i2025_1_);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int k3;
        int j3;
        int i3;
        int i = rand.nextInt(4) + 6;
        int j = 1 + rand.nextInt(2);
        int k = i - j;
        int l = 2 + rand.nextInt(2);
        boolean flag = true;
        if (position.getY() < 1) return false;
        if (position.getY() + i + 1 > 256) return false;
        int i1 = position.getY();
        block0: while (true) {
            if (i1 > position.getY() + 1 + i || !flag) {
                if (!flag) {
                    return false;
                }
                Block block1 = worldIn.getBlockState(position.down()).getBlock();
                if (block1 != Blocks.grass && block1 != Blocks.dirt) {
                    if (block1 != Blocks.farmland) return false;
                }
                if (position.getY() >= 256 - i - 1) return false;
                this.func_175921_a(worldIn, position.down());
                i3 = rand.nextInt(2);
                j3 = 1;
                k3 = 0;
                break;
            }
            int j1 = 1;
            j1 = i1 - position.getY() < j ? 0 : l;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            int k1 = position.getX() - j1;
            while (true) {
                if (k1 <= position.getX() + j1 && flag) {
                } else {
                    ++i1;
                    continue block0;
                }
                for (int l1 = position.getZ() - j1; l1 <= position.getZ() + j1 && flag; ++l1) {
                    if (i1 >= 0 && i1 < 256) {
                        Block block = worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, i1, l1)).getBlock();
                        if (block.getMaterial() == Material.air || block.getMaterial() == Material.leaves) continue;
                        flag = false;
                        continue;
                    }
                    flag = false;
                }
                ++k1;
            }
            break;
        }
        for (int l3 = 0; l3 <= k; ++l3) {
            int j4 = position.getY() + i - l3;
            for (int i2 = position.getX() - i3; i2 <= position.getX() + i3; ++i2) {
                int j2 = i2 - position.getX();
                for (int k2 = position.getZ() - i3; k2 <= position.getZ() + i3; ++k2) {
                    BlockPos blockpos;
                    int l2 = k2 - position.getZ();
                    if (Math.abs(j2) == i3 && Math.abs(l2) == i3 && i3 > 0 || worldIn.getBlockState(blockpos = new BlockPos(i2, j4, k2)).getBlock().isFullBlock()) continue;
                    this.setBlockAndNotifyAdequately(worldIn, blockpos, field_181646_b);
                }
            }
            if (i3 >= j3) {
                i3 = k3;
                k3 = 1;
                if (++j3 <= l) continue;
                j3 = l;
                continue;
            }
            ++i3;
        }
        int i4 = rand.nextInt(3);
        int k4 = 0;
        while (k4 < i - i4) {
            Block block2 = worldIn.getBlockState(position.up(k4)).getBlock();
            if (block2.getMaterial() == Material.air || block2.getMaterial() == Material.leaves) {
                this.setBlockAndNotifyAdequately(worldIn, position.up(k4), field_181645_a);
            }
            ++k4;
        }
        return true;
    }
}

