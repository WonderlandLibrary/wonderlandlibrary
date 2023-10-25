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
    private static final IBlockState field_181637_b;
    private static final IBlockState field_181636_a;

    public WorldGenTaiga1() {
        super(false);
    }

    static {
        field_181636_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
        field_181637_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, false);
    }

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        int n = random.nextInt(5) + 7;
        int n2 = n - random.nextInt(2) - 3;
        int n3 = n - n2;
        int n4 = 1 + random.nextInt(n3 + 1);
        boolean bl = true;
        if (blockPos.getY() >= 1 && blockPos.getY() + n + 1 <= 256) {
            int n5;
            int n6;
            int n7;
            int n8 = blockPos.getY();
            while (n8 <= blockPos.getY() + 1 + n && bl) {
                n7 = 1;
                n7 = n8 - blockPos.getY() < n2 ? 0 : n4;
                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                n6 = blockPos.getX() - n7;
                while (n6 <= blockPos.getX() + n7 && bl) {
                    n5 = blockPos.getZ() - n7;
                    while (n5 <= blockPos.getZ() + n7 && bl) {
                        if (n8 >= 0 && n8 < 256) {
                            if (!this.func_150523_a(world.getBlockState(mutableBlockPos.func_181079_c(n6, n8, n5)).getBlock())) {
                                bl = false;
                            }
                        } else {
                            bl = false;
                        }
                        ++n5;
                    }
                    ++n6;
                }
                ++n8;
            }
            if (!bl) {
                return false;
            }
            Block block = world.getBlockState(blockPos.down()).getBlock();
            if ((block == Blocks.grass || block == Blocks.dirt) && blockPos.getY() < 256 - n - 1) {
                this.func_175921_a(world, blockPos.down());
                n7 = 0;
                int n9 = blockPos.getY() + n;
                while (n9 >= blockPos.getY() + n2) {
                    n6 = blockPos.getX() - n7;
                    while (n6 <= blockPos.getX() + n7) {
                        n5 = n6 - blockPos.getX();
                        int n10 = blockPos.getZ() - n7;
                        while (n10 <= blockPos.getZ() + n7) {
                            BlockPos blockPos2;
                            int n11 = n10 - blockPos.getZ();
                            if (!(Math.abs(n5) == n7 && Math.abs(n11) == n7 && n7 > 0 || world.getBlockState(blockPos2 = new BlockPos(n6, n9, n10)).getBlock().isFullBlock())) {
                                this.setBlockAndNotifyAdequately(world, blockPos2, field_181637_b);
                            }
                            ++n10;
                        }
                        ++n6;
                    }
                    if (n7 >= 1 && n9 == blockPos.getY() + n2 + 1) {
                        --n7;
                    } else if (n7 < n4) {
                        ++n7;
                    }
                    --n9;
                }
                n9 = 0;
                while (n9 < n - 1) {
                    Block block2 = world.getBlockState(blockPos.up(n9)).getBlock();
                    if (block2.getMaterial() == Material.air || block2.getMaterial() == Material.leaves) {
                        this.setBlockAndNotifyAdequately(world, blockPos.up(n9), field_181636_a);
                    }
                    ++n9;
                }
                return true;
            }
            return false;
        }
        return false;
    }
}

