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
    private static final IBlockState field_181646_b;
    private static final IBlockState field_181645_a;

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        int n = random.nextInt(4) + 6;
        int n2 = 1 + random.nextInt(2);
        int n3 = n - n2;
        int n4 = 2 + random.nextInt(2);
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
                            Block block = world.getBlockState(mutableBlockPos.func_181079_c(n6, n8, n5)).getBlock();
                            if (block.getMaterial() != Material.air && block.getMaterial() != Material.leaves) {
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
            if ((block == Blocks.grass || block == Blocks.dirt || block == Blocks.farmland) && blockPos.getY() < 256 - n - 1) {
                this.func_175921_a(world, blockPos.down());
                n7 = random.nextInt(2);
                int n9 = 1;
                n6 = 0;
                n5 = 0;
                while (n5 <= n3) {
                    int n10 = blockPos.getY() + n - n5;
                    int n11 = blockPos.getX() - n7;
                    while (n11 <= blockPos.getX() + n7) {
                        int n12 = n11 - blockPos.getX();
                        int n13 = blockPos.getZ() - n7;
                        while (n13 <= blockPos.getZ() + n7) {
                            BlockPos blockPos2;
                            int n14 = n13 - blockPos.getZ();
                            if (!(Math.abs(n12) == n7 && Math.abs(n14) == n7 && n7 > 0 || world.getBlockState(blockPos2 = new BlockPos(n11, n10, n13)).getBlock().isFullBlock())) {
                                this.setBlockAndNotifyAdequately(world, blockPos2, field_181646_b);
                            }
                            ++n13;
                        }
                        ++n11;
                    }
                    if (n7 >= n9) {
                        n7 = n6;
                        n6 = 1;
                        if (++n9 > n4) {
                            n9 = n4;
                        }
                    } else {
                        ++n7;
                    }
                    ++n5;
                }
                n5 = random.nextInt(3);
                int n15 = 0;
                while (n15 < n - n5) {
                    Block block2 = world.getBlockState(blockPos.up(n15)).getBlock();
                    if (block2.getMaterial() == Material.air || block2.getMaterial() == Material.leaves) {
                        this.setBlockAndNotifyAdequately(world, blockPos.up(n15), field_181645_a);
                    }
                    ++n15;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    public WorldGenTaiga2(boolean bl) {
        super(bl);
    }

    static {
        field_181645_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
        field_181646_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, false);
    }
}

