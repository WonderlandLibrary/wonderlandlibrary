/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenForest
extends WorldGenAbstractTree {
    private static final IBlockState field_181629_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH);
    private boolean useExtraRandomHeight;
    private static final IBlockState field_181630_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH).withProperty(BlockOldLeaf.CHECK_DECAY, false);

    public WorldGenForest(boolean bl, boolean bl2) {
        super(bl);
        this.useExtraRandomHeight = bl2;
    }

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        int n = random.nextInt(3) + 5;
        if (this.useExtraRandomHeight) {
            n += random.nextInt(7);
        }
        boolean bl = true;
        if (blockPos.getY() >= 1 && blockPos.getY() + n + 1 <= 256) {
            int n2;
            int n3;
            Object object;
            int n4;
            int n5 = blockPos.getY();
            while (n5 <= blockPos.getY() + 1 + n) {
                n4 = 1;
                if (n5 == blockPos.getY()) {
                    n4 = 0;
                }
                if (n5 >= blockPos.getY() + 1 + n - 2) {
                    n4 = 2;
                }
                object = new BlockPos.MutableBlockPos();
                n3 = blockPos.getX() - n4;
                while (n3 <= blockPos.getX() + n4 && bl) {
                    n2 = blockPos.getZ() - n4;
                    while (n2 <= blockPos.getZ() + n4 && bl) {
                        if (n5 >= 0 && n5 < 256) {
                            if (!this.func_150523_a(world.getBlockState(((BlockPos.MutableBlockPos)object).func_181079_c(n3, n5, n2)).getBlock())) {
                                bl = false;
                            }
                        } else {
                            bl = false;
                        }
                        ++n2;
                    }
                    ++n3;
                }
                ++n5;
            }
            if (!bl) {
                return false;
            }
            Block block = world.getBlockState(blockPos.down()).getBlock();
            if ((block == Blocks.grass || block == Blocks.dirt || block == Blocks.farmland) && blockPos.getY() < 256 - n - 1) {
                this.func_175921_a(world, blockPos.down());
                n4 = blockPos.getY() - 3 + n;
                while (n4 <= blockPos.getY() + n) {
                    int n6 = n4 - (blockPos.getY() + n);
                    n3 = 1 - n6 / 2;
                    n2 = blockPos.getX() - n3;
                    while (n2 <= blockPos.getX() + n3) {
                        int n7 = n2 - blockPos.getX();
                        int n8 = blockPos.getZ() - n3;
                        while (n8 <= blockPos.getZ() + n3) {
                            BlockPos blockPos2;
                            Block block2;
                            int n9 = n8 - blockPos.getZ();
                            if ((Math.abs(n7) != n3 || Math.abs(n9) != n3 || random.nextInt(2) != 0 && n6 != 0) && ((block2 = world.getBlockState(blockPos2 = new BlockPos(n2, n4, n8)).getBlock()).getMaterial() == Material.air || block2.getMaterial() == Material.leaves)) {
                                this.setBlockAndNotifyAdequately(world, blockPos2, field_181630_b);
                            }
                            ++n8;
                        }
                        ++n2;
                    }
                    ++n4;
                }
                n4 = 0;
                while (n4 < n) {
                    object = world.getBlockState(blockPos.up(n4)).getBlock();
                    if (((Block)object).getMaterial() == Material.air || ((Block)object).getMaterial() == Material.leaves) {
                        this.setBlockAndNotifyAdequately(world, blockPos.up(n4), field_181629_a);
                    }
                    ++n4;
                }
                return true;
            }
            return false;
        }
        return false;
    }
}

