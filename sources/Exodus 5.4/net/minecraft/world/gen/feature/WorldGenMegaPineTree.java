/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenHugeTrees;

public class WorldGenMegaPineTree
extends WorldGenHugeTrees {
    private static final IBlockState field_181634_f;
    private static final IBlockState field_181635_g;
    private static final IBlockState field_181633_e;
    private boolean useBaseHeight;

    private void func_175934_c(World world, BlockPos blockPos) {
        int n = 2;
        while (n >= -3) {
            BlockPos blockPos2 = blockPos.up(n);
            Block block = world.getBlockState(blockPos2).getBlock();
            if (block == Blocks.grass || block == Blocks.dirt) {
                this.setBlockAndNotifyAdequately(world, blockPos2, field_181635_g);
                break;
            }
            if (block.getMaterial() != Material.air && n < 0) break;
            --n;
        }
    }

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        int n = this.func_150533_a(random);
        if (!this.func_175929_a(world, random, blockPos, n)) {
            return false;
        }
        this.func_150541_c(world, blockPos.getX(), blockPos.getZ(), blockPos.getY() + n, 0, random);
        int n2 = 0;
        while (n2 < n) {
            Block block = world.getBlockState(blockPos.up(n2)).getBlock();
            if (block.getMaterial() == Material.air || block.getMaterial() == Material.leaves) {
                this.setBlockAndNotifyAdequately(world, blockPos.up(n2), this.woodMetadata);
            }
            if (n2 < n - 1) {
                block = world.getBlockState(blockPos.add(1, n2, 0)).getBlock();
                if (block.getMaterial() == Material.air || block.getMaterial() == Material.leaves) {
                    this.setBlockAndNotifyAdequately(world, blockPos.add(1, n2, 0), this.woodMetadata);
                }
                if ((block = world.getBlockState(blockPos.add(1, n2, 1)).getBlock()).getMaterial() == Material.air || block.getMaterial() == Material.leaves) {
                    this.setBlockAndNotifyAdequately(world, blockPos.add(1, n2, 1), this.woodMetadata);
                }
                if ((block = world.getBlockState(blockPos.add(0, n2, 1)).getBlock()).getMaterial() == Material.air || block.getMaterial() == Material.leaves) {
                    this.setBlockAndNotifyAdequately(world, blockPos.add(0, n2, 1), this.woodMetadata);
                }
            }
            ++n2;
        }
        return true;
    }

    private void func_175933_b(World world, BlockPos blockPos) {
        int n = -2;
        while (n <= 2) {
            int n2 = -2;
            while (n2 <= 2) {
                if (Math.abs(n) != 2 || Math.abs(n2) != 2) {
                    this.func_175934_c(world, blockPos.add(n, 0, n2));
                }
                ++n2;
            }
            ++n;
        }
    }

    static {
        field_181633_e = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
        field_181634_f = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, false);
        field_181635_g = Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
    }

    @Override
    public void func_180711_a(World world, Random random, BlockPos blockPos) {
        this.func_175933_b(world, blockPos.west().north());
        this.func_175933_b(world, blockPos.east(2).north());
        this.func_175933_b(world, blockPos.west().south(2));
        this.func_175933_b(world, blockPos.east(2).south(2));
        int n = 0;
        while (n < 5) {
            int n2 = random.nextInt(64);
            int n3 = n2 % 8;
            int n4 = n2 / 8;
            if (n3 == 0 || n3 == 7 || n4 == 0 || n4 == 7) {
                this.func_175933_b(world, blockPos.add(-3 + n3, 0, -3 + n4));
            }
            ++n;
        }
    }

    public WorldGenMegaPineTree(boolean bl, boolean bl2) {
        super(bl, 13, 15, field_181633_e, field_181634_f);
        this.useBaseHeight = bl2;
    }

    private void func_150541_c(World world, int n, int n2, int n3, int n4, Random random) {
        int n5 = random.nextInt(5) + (this.useBaseHeight ? this.baseHeight : 3);
        int n6 = 0;
        int n7 = n3 - n5;
        while (n7 <= n3) {
            int n8 = n3 - n7;
            int n9 = n4 + MathHelper.floor_float((float)n8 / (float)n5 * 3.5f);
            this.func_175925_a(world, new BlockPos(n, n7, n2), n9 + (n8 > 0 && n9 == n6 && (n7 & 1) == 0 ? 1 : 0));
            n6 = n9;
            ++n7;
        }
    }
}

