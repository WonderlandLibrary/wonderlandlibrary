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

public class WorldGenTaiga1
extends WorldGenAbstractTree {
    private static final IBlockState field_181636_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
    private static final IBlockState field_181637_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, false);

    public WorldGenTaiga1() {
        super(false);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int k2;
        int i = rand.nextInt(5) + 7;
        int j = i - rand.nextInt(2) - 3;
        int k = i - j;
        int l = 1 + rand.nextInt(k + 1);
        boolean flag = true;
        if (position.getY() < 1) return false;
        if (position.getY() + i + 1 > 256) return false;
        int i1 = position.getY();
        block0: while (true) {
            if (i1 > position.getY() + 1 + i || !flag) {
                if (!flag) {
                    return false;
                }
                Block block = worldIn.getBlockState(position.down()).getBlock();
                if (block != Blocks.grass) {
                    if (block != Blocks.dirt) return false;
                }
                if (position.getY() >= 256 - i - 1) return false;
                this.func_175921_a(worldIn, position.down());
                k2 = 0;
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
                        if (this.func_150523_a(worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, i1, l1)).getBlock())) continue;
                        flag = false;
                        continue;
                    }
                    flag = false;
                }
                ++k1;
            }
            break;
        }
        for (int l2 = position.getY() + i; l2 >= position.getY() + j; --l2) {
            for (int j3 = position.getX() - k2; j3 <= position.getX() + k2; ++j3) {
                int k3 = j3 - position.getX();
                for (int i2 = position.getZ() - k2; i2 <= position.getZ() + k2; ++i2) {
                    BlockPos blockpos;
                    int j2 = i2 - position.getZ();
                    if (Math.abs(k3) == k2 && Math.abs(j2) == k2 && k2 > 0 || worldIn.getBlockState(blockpos = new BlockPos(j3, l2, i2)).getBlock().isFullBlock()) continue;
                    this.setBlockAndNotifyAdequately(worldIn, blockpos, field_181637_b);
                }
            }
            if (k2 >= 1 && l2 == position.getY() + j + 1) {
                --k2;
                continue;
            }
            if (k2 >= l) continue;
            ++k2;
        }
        int i3 = 0;
        while (i3 < i - 1) {
            Block block1 = worldIn.getBlockState(position.up(i3)).getBlock();
            if (block1.getMaterial() == Material.air || block1.getMaterial() == Material.leaves) {
                this.setBlockAndNotifyAdequately(worldIn, position.up(i3), field_181636_a);
            }
            ++i3;
        }
        return true;
    }
}

