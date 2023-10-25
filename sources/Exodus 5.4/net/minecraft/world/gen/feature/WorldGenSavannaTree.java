/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenSavannaTree
extends WorldGenAbstractTree {
    private static final IBlockState field_181644_b;
    private static final IBlockState field_181643_a;

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        int n = random.nextInt(3) + random.nextInt(3) + 5;
        boolean bl = true;
        if (blockPos.getY() >= 1 && blockPos.getY() + n + 1 <= 256) {
            int n2;
            int n3;
            int n4 = blockPos.getY();
            while (n4 <= blockPos.getY() + 1 + n) {
                int n5 = 1;
                if (n4 == blockPos.getY()) {
                    n5 = 0;
                }
                if (n4 >= blockPos.getY() + 1 + n - 2) {
                    n5 = 2;
                }
                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                n3 = blockPos.getX() - n5;
                while (n3 <= blockPos.getX() + n5 && bl) {
                    n2 = blockPos.getZ() - n5;
                    while (n2 <= blockPos.getZ() + n5 && bl) {
                        if (n4 >= 0 && n4 < 256) {
                            if (!this.func_150523_a(world.getBlockState(mutableBlockPos.func_181079_c(n3, n4, n2)).getBlock())) {
                                bl = false;
                            }
                        } else {
                            bl = false;
                        }
                        ++n2;
                    }
                    ++n3;
                }
                ++n4;
            }
            if (!bl) {
                return false;
            }
            Block block = world.getBlockState(blockPos.down()).getBlock();
            if ((block == Blocks.grass || block == Blocks.dirt) && blockPos.getY() < 256 - n - 1) {
                int n6;
                this.func_175921_a(world, blockPos.down());
                EnumFacing enumFacing = EnumFacing.Plane.HORIZONTAL.random(random);
                int n7 = n - random.nextInt(4) - 1;
                n3 = 3 - random.nextInt(3);
                n2 = blockPos.getX();
                int n8 = blockPos.getZ();
                int n9 = 0;
                int n10 = 0;
                while (n10 < n) {
                    BlockPos blockPos2;
                    Material material;
                    n6 = blockPos.getY() + n10;
                    if (n10 >= n7 && n3 > 0) {
                        n2 += enumFacing.getFrontOffsetX();
                        n8 += enumFacing.getFrontOffsetZ();
                        --n3;
                    }
                    if ((material = world.getBlockState(blockPos2 = new BlockPos(n2, n6, n8)).getBlock().getMaterial()) == Material.air || material == Material.leaves) {
                        this.func_181642_b(world, blockPos2);
                        n9 = n6;
                    }
                    ++n10;
                }
                BlockPos blockPos3 = new BlockPos(n2, n9, n8);
                n6 = -3;
                while (n6 <= 3) {
                    int n11 = -3;
                    while (n11 <= 3) {
                        if (Math.abs(n6) != 3 || Math.abs(n11) != 3) {
                            this.func_175924_b(world, blockPos3.add(n6, 0, n11));
                        }
                        ++n11;
                    }
                    ++n6;
                }
                blockPos3 = blockPos3.up();
                n6 = -1;
                while (n6 <= 1) {
                    int n12 = -1;
                    while (n12 <= 1) {
                        this.func_175924_b(world, blockPos3.add(n6, 0, n12));
                        ++n12;
                    }
                    ++n6;
                }
                this.func_175924_b(world, blockPos3.east(2));
                this.func_175924_b(world, blockPos3.west(2));
                this.func_175924_b(world, blockPos3.south(2));
                this.func_175924_b(world, blockPos3.north(2));
                n2 = blockPos.getX();
                n8 = blockPos.getZ();
                EnumFacing enumFacing2 = EnumFacing.Plane.HORIZONTAL.random(random);
                if (enumFacing2 != enumFacing) {
                    int n13;
                    int n14 = n7 - random.nextInt(2) - 1;
                    int n15 = 1 + random.nextInt(3);
                    n9 = 0;
                    int n16 = n14;
                    while (n16 < n && n15 > 0) {
                        if (n16 >= 1) {
                            n13 = blockPos.getY() + n16;
                            BlockPos blockPos4 = new BlockPos(n2 += enumFacing2.getFrontOffsetX(), n13, n8 += enumFacing2.getFrontOffsetZ());
                            Material material = world.getBlockState(blockPos4).getBlock().getMaterial();
                            if (material == Material.air || material == Material.leaves) {
                                this.func_181642_b(world, blockPos4);
                                n9 = n13;
                            }
                        }
                        ++n16;
                        --n15;
                    }
                    if (n9 > 0) {
                        BlockPos blockPos5 = new BlockPos(n2, n9, n8);
                        n13 = -2;
                        while (n13 <= 2) {
                            int n17 = -2;
                            while (n17 <= 2) {
                                if (Math.abs(n13) != 2 || Math.abs(n17) != 2) {
                                    this.func_175924_b(world, blockPos5.add(n13, 0, n17));
                                }
                                ++n17;
                            }
                            ++n13;
                        }
                        blockPos5 = blockPos5.up();
                        n13 = -1;
                        while (n13 <= 1) {
                            int n18 = -1;
                            while (n18 <= 1) {
                                this.func_175924_b(world, blockPos5.add(n13, 0, n18));
                                ++n18;
                            }
                            ++n13;
                        }
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    private void func_181642_b(World world, BlockPos blockPos) {
        this.setBlockAndNotifyAdequately(world, blockPos, field_181643_a);
    }

    private void func_175924_b(World world, BlockPos blockPos) {
        Material material = world.getBlockState(blockPos).getBlock().getMaterial();
        if (material == Material.air || material == Material.leaves) {
            this.setBlockAndNotifyAdequately(world, blockPos, field_181644_b);
        }
    }

    public WorldGenSavannaTree(boolean bl) {
        super(bl);
    }

    static {
        field_181643_a = Blocks.log2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA);
        field_181644_b = Blocks.leaves2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLeaves.CHECK_DECAY, false);
    }
}

