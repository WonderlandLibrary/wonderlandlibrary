/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenTrees
extends WorldGenAbstractTree {
    private final boolean vinesGrow;
    private final IBlockState metaLeaves;
    private static final IBlockState field_181653_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
    private final int minTreeHeight;
    private static final IBlockState field_181654_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, false);
    private final IBlockState metaWood;

    private void func_181652_a(World world, int n, BlockPos blockPos, EnumFacing enumFacing) {
        this.setBlockAndNotifyAdequately(world, blockPos, Blocks.cocoa.getDefaultState().withProperty(BlockCocoa.AGE, n).withProperty(BlockCocoa.FACING, enumFacing));
    }

    public WorldGenTrees(boolean bl) {
        this(bl, 4, field_181653_a, field_181654_b, false);
    }

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        int n = random.nextInt(3) + this.minTreeHeight;
        boolean bl = true;
        if (blockPos.getY() >= 1 && blockPos.getY() + n + 1 <= 256) {
            int n2;
            int n3;
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
                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                n3 = blockPos.getX() - n4;
                while (n3 <= blockPos.getX() + n4 && bl) {
                    n2 = blockPos.getZ() - n4;
                    while (n2 <= blockPos.getZ() + n4 && bl) {
                        if (n5 >= 0 && n5 < 256) {
                            if (!this.func_150523_a(world.getBlockState(mutableBlockPos.func_181079_c(n3, n5, n2)).getBlock())) {
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
                BlockPos blockPos2;
                Object object;
                int n6;
                int n7;
                int n8;
                this.func_175921_a(world, blockPos.down());
                n4 = 3;
                int n9 = 0;
                n3 = blockPos.getY() - n4 + n;
                while (n3 <= blockPos.getY() + n) {
                    n2 = n3 - (blockPos.getY() + n);
                    n8 = n9 + 1 - n2 / 2;
                    int n10 = blockPos.getX() - n8;
                    while (n10 <= blockPos.getX() + n8) {
                        n7 = n10 - blockPos.getX();
                        n6 = blockPos.getZ() - n8;
                        while (n6 <= blockPos.getZ() + n8) {
                            int n11 = n6 - blockPos.getZ();
                            if ((Math.abs(n7) != n8 || Math.abs(n11) != n8 || random.nextInt(2) != 0 && n2 != 0) && (((Block)(object = world.getBlockState(blockPos2 = new BlockPos(n10, n3, n6)).getBlock())).getMaterial() == Material.air || ((Block)object).getMaterial() == Material.leaves || ((Block)object).getMaterial() == Material.vine)) {
                                this.setBlockAndNotifyAdequately(world, blockPos2, this.metaLeaves);
                            }
                            ++n6;
                        }
                        ++n10;
                    }
                    ++n3;
                }
                n3 = 0;
                while (n3 < n) {
                    Block block2 = world.getBlockState(blockPos.up(n3)).getBlock();
                    if (block2.getMaterial() == Material.air || block2.getMaterial() == Material.leaves || block2.getMaterial() == Material.vine) {
                        this.setBlockAndNotifyAdequately(world, blockPos.up(n3), this.metaWood);
                        if (this.vinesGrow && n3 > 0) {
                            if (random.nextInt(3) > 0 && world.isAirBlock(blockPos.add(-1, n3, 0))) {
                                this.func_181651_a(world, blockPos.add(-1, n3, 0), BlockVine.EAST);
                            }
                            if (random.nextInt(3) > 0 && world.isAirBlock(blockPos.add(1, n3, 0))) {
                                this.func_181651_a(world, blockPos.add(1, n3, 0), BlockVine.WEST);
                            }
                            if (random.nextInt(3) > 0 && world.isAirBlock(blockPos.add(0, n3, -1))) {
                                this.func_181651_a(world, blockPos.add(0, n3, -1), BlockVine.SOUTH);
                            }
                            if (random.nextInt(3) > 0 && world.isAirBlock(blockPos.add(0, n3, 1))) {
                                this.func_181651_a(world, blockPos.add(0, n3, 1), BlockVine.NORTH);
                            }
                        }
                    }
                    ++n3;
                }
                if (this.vinesGrow) {
                    n3 = blockPos.getY() - 3 + n;
                    while (n3 <= blockPos.getY() + n) {
                        int n12 = n3 - (blockPos.getY() + n);
                        n8 = 2 - n12 / 2;
                        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                        n7 = blockPos.getX() - n8;
                        while (n7 <= blockPos.getX() + n8) {
                            n6 = blockPos.getZ() - n8;
                            while (n6 <= blockPos.getZ() + n8) {
                                mutableBlockPos.func_181079_c(n7, n3, n6);
                                if (world.getBlockState(mutableBlockPos).getBlock().getMaterial() == Material.leaves) {
                                    BlockPos blockPos3 = mutableBlockPos.west();
                                    blockPos2 = mutableBlockPos.east();
                                    object = mutableBlockPos.north();
                                    BlockPos blockPos4 = mutableBlockPos.south();
                                    if (random.nextInt(4) == 0 && world.getBlockState(blockPos3).getBlock().getMaterial() == Material.air) {
                                        this.func_181650_b(world, blockPos3, BlockVine.EAST);
                                    }
                                    if (random.nextInt(4) == 0 && world.getBlockState(blockPos2).getBlock().getMaterial() == Material.air) {
                                        this.func_181650_b(world, blockPos2, BlockVine.WEST);
                                    }
                                    if (random.nextInt(4) == 0 && world.getBlockState((BlockPos)object).getBlock().getMaterial() == Material.air) {
                                        this.func_181650_b(world, (BlockPos)object, BlockVine.SOUTH);
                                    }
                                    if (random.nextInt(4) == 0 && world.getBlockState(blockPos4).getBlock().getMaterial() == Material.air) {
                                        this.func_181650_b(world, blockPos4, BlockVine.NORTH);
                                    }
                                }
                                ++n6;
                            }
                            ++n7;
                        }
                        ++n3;
                    }
                    if (random.nextInt(5) == 0 && n > 5) {
                        n3 = 0;
                        while (n3 < 2) {
                            for (EnumFacing enumFacing : EnumFacing.Plane.HORIZONTAL) {
                                if (random.nextInt(4 - n3) != 0) continue;
                                EnumFacing enumFacing2 = enumFacing.getOpposite();
                                this.func_181652_a(world, random.nextInt(3), blockPos.add(enumFacing2.getFrontOffsetX(), n - 5 + n3, enumFacing2.getFrontOffsetZ()), enumFacing);
                            }
                            ++n3;
                        }
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    public WorldGenTrees(boolean bl, int n, IBlockState iBlockState, IBlockState iBlockState2, boolean bl2) {
        super(bl);
        this.minTreeHeight = n;
        this.metaWood = iBlockState;
        this.metaLeaves = iBlockState2;
        this.vinesGrow = bl2;
    }

    private void func_181651_a(World world, BlockPos blockPos, PropertyBool propertyBool) {
        this.setBlockAndNotifyAdequately(world, blockPos, Blocks.vine.getDefaultState().withProperty(propertyBool, true));
    }

    private void func_181650_b(World world, BlockPos blockPos, PropertyBool propertyBool) {
        this.func_181651_a(world, blockPos, propertyBool);
        int n = 4;
        blockPos = blockPos.down();
        while (world.getBlockState(blockPos).getBlock().getMaterial() == Material.air && n > 0) {
            this.func_181651_a(world, blockPos, propertyBool);
            blockPos = blockPos.down();
            --n;
        }
    }
}

