/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenSwamp
extends WorldGenAbstractTree {
    private static final IBlockState field_181649_b;
    private static final IBlockState field_181648_a;

    static {
        field_181648_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
        field_181649_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockOldLeaf.CHECK_DECAY, false);
    }

    public WorldGenSwamp() {
        super(false);
    }

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        int n = random.nextInt(4) + 5;
        while (world.getBlockState(blockPos.down()).getBlock().getMaterial() == Material.water) {
            blockPos = blockPos.down();
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
                    n4 = 3;
                }
                object = new BlockPos.MutableBlockPos();
                n3 = blockPos.getX() - n4;
                while (n3 <= blockPos.getX() + n4 && bl) {
                    n2 = blockPos.getZ() - n4;
                    while (n2 <= blockPos.getZ() + n4 && bl) {
                        if (n5 >= 0 && n5 < 256) {
                            Block block = world.getBlockState(((BlockPos.MutableBlockPos)object).func_181079_c(n3, n5, n2)).getBlock();
                            if (block.getMaterial() != Material.air && block.getMaterial() != Material.leaves) {
                                if (block != Blocks.water && block != Blocks.flowing_water) {
                                    bl = false;
                                } else if (n5 > blockPos.getY()) {
                                    bl = false;
                                }
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
            if ((block == Blocks.grass || block == Blocks.dirt) && blockPos.getY() < 256 - n - 1) {
                BlockPos blockPos2;
                int n6;
                this.func_175921_a(world, blockPos.down());
                n4 = blockPos.getY() - 3 + n;
                while (n4 <= blockPos.getY() + n) {
                    int n7 = n4 - (blockPos.getY() + n);
                    n3 = 2 - n7 / 2;
                    n2 = blockPos.getX() - n3;
                    while (n2 <= blockPos.getX() + n3) {
                        int n8 = n2 - blockPos.getX();
                        n6 = blockPos.getZ() - n3;
                        while (n6 <= blockPos.getZ() + n3) {
                            int n9 = n6 - blockPos.getZ();
                            if ((Math.abs(n8) != n3 || Math.abs(n9) != n3 || random.nextInt(2) != 0 && n7 != 0) && !world.getBlockState(blockPos2 = new BlockPos(n2, n4, n6)).getBlock().isFullBlock()) {
                                this.setBlockAndNotifyAdequately(world, blockPos2, field_181649_b);
                            }
                            ++n6;
                        }
                        ++n2;
                    }
                    ++n4;
                }
                n4 = 0;
                while (n4 < n) {
                    object = world.getBlockState(blockPos.up(n4)).getBlock();
                    if (((Block)object).getMaterial() == Material.air || ((Block)object).getMaterial() == Material.leaves || object == Blocks.flowing_water || object == Blocks.water) {
                        this.setBlockAndNotifyAdequately(world, blockPos.up(n4), field_181648_a);
                    }
                    ++n4;
                }
                n4 = blockPos.getY() - 3 + n;
                while (n4 <= blockPos.getY() + n) {
                    int n10 = n4 - (blockPos.getY() + n);
                    n3 = 2 - n10 / 2;
                    BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                    int n11 = blockPos.getX() - n3;
                    while (n11 <= blockPos.getX() + n3) {
                        n6 = blockPos.getZ() - n3;
                        while (n6 <= blockPos.getZ() + n3) {
                            mutableBlockPos.func_181079_c(n11, n4, n6);
                            if (world.getBlockState(mutableBlockPos).getBlock().getMaterial() == Material.leaves) {
                                BlockPos blockPos3 = mutableBlockPos.west();
                                blockPos2 = mutableBlockPos.east();
                                BlockPos blockPos4 = mutableBlockPos.north();
                                BlockPos blockPos5 = mutableBlockPos.south();
                                if (random.nextInt(4) == 0 && world.getBlockState(blockPos3).getBlock().getMaterial() == Material.air) {
                                    this.func_181647_a(world, blockPos3, BlockVine.EAST);
                                }
                                if (random.nextInt(4) == 0 && world.getBlockState(blockPos2).getBlock().getMaterial() == Material.air) {
                                    this.func_181647_a(world, blockPos2, BlockVine.WEST);
                                }
                                if (random.nextInt(4) == 0 && world.getBlockState(blockPos4).getBlock().getMaterial() == Material.air) {
                                    this.func_181647_a(world, blockPos4, BlockVine.SOUTH);
                                }
                                if (random.nextInt(4) == 0 && world.getBlockState(blockPos5).getBlock().getMaterial() == Material.air) {
                                    this.func_181647_a(world, blockPos5, BlockVine.NORTH);
                                }
                            }
                            ++n6;
                        }
                        ++n11;
                    }
                    ++n4;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    private void func_181647_a(World world, BlockPos blockPos, PropertyBool propertyBool) {
        IBlockState iBlockState = Blocks.vine.getDefaultState().withProperty(propertyBool, true);
        this.setBlockAndNotifyAdequately(world, blockPos, iBlockState);
        int n = 4;
        blockPos = blockPos.down();
        while (world.getBlockState(blockPos).getBlock().getMaterial() == Material.air && n > 0) {
            this.setBlockAndNotifyAdequately(world, blockPos, iBlockState);
            blockPos = blockPos.down();
            --n;
        }
    }
}

