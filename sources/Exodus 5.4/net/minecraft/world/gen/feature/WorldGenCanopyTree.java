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

public class WorldGenCanopyTree
extends WorldGenAbstractTree {
    private static final IBlockState field_181640_a = Blocks.log2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK);
    private static final IBlockState field_181641_b = Blocks.leaves2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty(BlockLeaves.CHECK_DECAY, false);

    private void func_150526_a(World world, int n, int n2, int n3) {
        BlockPos blockPos = new BlockPos(n, n2, n3);
        Block block = world.getBlockState(blockPos).getBlock();
        if (block.getMaterial() == Material.air) {
            this.setBlockAndNotifyAdequately(world, blockPos, field_181641_b);
        }
    }

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        int n = random.nextInt(3) + random.nextInt(2) + 6;
        int n2 = blockPos.getX();
        int n3 = blockPos.getY();
        int n4 = blockPos.getZ();
        if (n3 >= 1 && n3 + n + 1 < 256) {
            int n5;
            BlockPos blockPos2 = blockPos.down();
            Block block = world.getBlockState(blockPos2).getBlock();
            if (block != Blocks.grass && block != Blocks.dirt) {
                return false;
            }
            if (!this.func_181638_a(world, blockPos, n)) {
                return false;
            }
            this.func_175921_a(world, blockPos2);
            this.func_175921_a(world, blockPos2.east());
            this.func_175921_a(world, blockPos2.south());
            this.func_175921_a(world, blockPos2.south().east());
            EnumFacing enumFacing = EnumFacing.Plane.HORIZONTAL.random(random);
            int n6 = n - random.nextInt(4);
            int n7 = 2 - random.nextInt(3);
            int n8 = n2;
            int n9 = n4;
            int n10 = n3 + n - 1;
            int n11 = 0;
            while (n11 < n) {
                BlockPos blockPos3;
                Material material;
                if (n11 >= n6 && n7 > 0) {
                    n8 += enumFacing.getFrontOffsetX();
                    n9 += enumFacing.getFrontOffsetZ();
                    --n7;
                }
                if ((material = world.getBlockState(blockPos3 = new BlockPos(n8, n5 = n3 + n11, n9)).getBlock().getMaterial()) == Material.air || material == Material.leaves) {
                    this.func_181639_b(world, blockPos3);
                    this.func_181639_b(world, blockPos3.east());
                    this.func_181639_b(world, blockPos3.south());
                    this.func_181639_b(world, blockPos3.east().south());
                }
                ++n11;
            }
            n11 = -2;
            while (n11 <= 0) {
                n5 = -2;
                while (n5 <= 0) {
                    int n12 = -1;
                    this.func_150526_a(world, n8 + n11, n10 + n12, n9 + n5);
                    this.func_150526_a(world, 1 + n8 - n11, n10 + n12, n9 + n5);
                    this.func_150526_a(world, n8 + n11, n10 + n12, 1 + n9 - n5);
                    this.func_150526_a(world, 1 + n8 - n11, n10 + n12, 1 + n9 - n5);
                    if (!(n11 <= -2 && n5 <= -1 || n11 == -1 && n5 == -2)) {
                        n12 = 1;
                        this.func_150526_a(world, n8 + n11, n10 + n12, n9 + n5);
                        this.func_150526_a(world, 1 + n8 - n11, n10 + n12, n9 + n5);
                        this.func_150526_a(world, n8 + n11, n10 + n12, 1 + n9 - n5);
                        this.func_150526_a(world, 1 + n8 - n11, n10 + n12, 1 + n9 - n5);
                    }
                    ++n5;
                }
                ++n11;
            }
            if (random.nextBoolean()) {
                this.func_150526_a(world, n8, n10 + 2, n9);
                this.func_150526_a(world, n8 + 1, n10 + 2, n9);
                this.func_150526_a(world, n8 + 1, n10 + 2, n9 + 1);
                this.func_150526_a(world, n8, n10 + 2, n9 + 1);
            }
            n11 = -3;
            while (n11 <= 4) {
                n5 = -3;
                while (n5 <= 4) {
                    if (!(n11 == -3 && n5 == -3 || n11 == -3 && n5 == 4 || n11 == 4 && n5 == -3 || n11 == 4 && n5 == 4 || Math.abs(n11) >= 3 && Math.abs(n5) >= 3)) {
                        this.func_150526_a(world, n8 + n11, n10, n9 + n5);
                    }
                    ++n5;
                }
                ++n11;
            }
            n11 = -1;
            while (n11 <= 2) {
                n5 = -1;
                while (n5 <= 2) {
                    if ((n11 < 0 || n11 > 1 || n5 < 0 || n5 > 1) && random.nextInt(3) <= 0) {
                        int n13;
                        int n14 = random.nextInt(3) + 2;
                        int n15 = 0;
                        while (n15 < n14) {
                            this.func_181639_b(world, new BlockPos(n2 + n11, n10 - n15 - 1, n4 + n5));
                            ++n15;
                        }
                        n15 = -1;
                        while (n15 <= 1) {
                            n13 = -1;
                            while (n13 <= 1) {
                                this.func_150526_a(world, n8 + n11 + n15, n10, n9 + n5 + n13);
                                ++n13;
                            }
                            ++n15;
                        }
                        n15 = -2;
                        while (n15 <= 2) {
                            n13 = -2;
                            while (n13 <= 2) {
                                if (Math.abs(n15) != 2 || Math.abs(n13) != 2) {
                                    this.func_150526_a(world, n8 + n11 + n15, n10 - 1, n9 + n5 + n13);
                                }
                                ++n13;
                            }
                            ++n15;
                        }
                    }
                    ++n5;
                }
                ++n11;
            }
            return true;
        }
        return false;
    }

    private void func_181639_b(World world, BlockPos blockPos) {
        if (this.func_150523_a(world.getBlockState(blockPos).getBlock())) {
            this.setBlockAndNotifyAdequately(world, blockPos, field_181640_a);
        }
    }

    private boolean func_181638_a(World world, BlockPos blockPos, int n) {
        int n2 = blockPos.getX();
        int n3 = blockPos.getY();
        int n4 = blockPos.getZ();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int n5 = 0;
        while (n5 <= n + 1) {
            int n6 = 1;
            if (n5 == 0) {
                n6 = 0;
            }
            if (n5 >= n - 1) {
                n6 = 2;
            }
            int n7 = -n6;
            while (n7 <= n6) {
                int n8 = -n6;
                while (n8 <= n6) {
                    if (!this.func_150523_a(world.getBlockState(mutableBlockPos.func_181079_c(n2 + n7, n3 + n5, n4 + n8)).getBlock())) {
                        return false;
                    }
                    ++n8;
                }
                ++n7;
            }
            ++n5;
        }
        return true;
    }

    public WorldGenCanopyTree(boolean bl) {
        super(bl);
    }
}

