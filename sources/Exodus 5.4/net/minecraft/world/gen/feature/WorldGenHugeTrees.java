/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public abstract class WorldGenHugeTrees
extends WorldGenAbstractTree {
    protected final IBlockState woodMetadata;
    protected final int baseHeight;
    protected int extraRandomHeight;
    protected final IBlockState leavesMetadata;

    protected int func_150533_a(Random random) {
        int n = random.nextInt(3) + this.baseHeight;
        if (this.extraRandomHeight > 1) {
            n += random.nextInt(this.extraRandomHeight);
        }
        return n;
    }

    private boolean func_175926_c(World world, BlockPos blockPos, int n) {
        boolean bl = true;
        if (blockPos.getY() >= 1 && blockPos.getY() + n + 1 <= 256) {
            int n2 = 0;
            while (n2 <= 1 + n) {
                int n3 = 2;
                if (n2 == 0) {
                    n3 = 1;
                } else if (n2 >= 1 + n - 2) {
                    n3 = 2;
                }
                int n4 = -n3;
                while (n4 <= n3 && bl) {
                    int n5 = -n3;
                    while (n5 <= n3 && bl) {
                        if (blockPos.getY() + n2 < 0 || blockPos.getY() + n2 >= 256 || !this.func_150523_a(world.getBlockState(blockPos.add(n4, n2, n5)).getBlock())) {
                            bl = false;
                        }
                        ++n5;
                    }
                    ++n4;
                }
                ++n2;
            }
            return bl;
        }
        return false;
    }

    private boolean func_175927_a(BlockPos blockPos, World world) {
        BlockPos blockPos2 = blockPos.down();
        Block block = world.getBlockState(blockPos2).getBlock();
        if ((block == Blocks.grass || block == Blocks.dirt) && blockPos.getY() >= 2) {
            this.func_175921_a(world, blockPos2);
            this.func_175921_a(world, blockPos2.east());
            this.func_175921_a(world, blockPos2.south());
            this.func_175921_a(world, blockPos2.south().east());
            return true;
        }
        return false;
    }

    public WorldGenHugeTrees(boolean bl, int n, int n2, IBlockState iBlockState, IBlockState iBlockState2) {
        super(bl);
        this.baseHeight = n;
        this.extraRandomHeight = n2;
        this.woodMetadata = iBlockState;
        this.leavesMetadata = iBlockState2;
    }

    protected void func_175928_b(World world, BlockPos blockPos, int n) {
        int n2 = n * n;
        int n3 = -n;
        while (n3 <= n) {
            int n4 = -n;
            while (n4 <= n) {
                BlockPos blockPos2;
                Material material;
                if (n3 * n3 + n4 * n4 <= n2 && ((material = world.getBlockState(blockPos2 = blockPos.add(n3, 0, n4)).getBlock().getMaterial()) == Material.air || material == Material.leaves)) {
                    this.setBlockAndNotifyAdequately(world, blockPos2, this.leavesMetadata);
                }
                ++n4;
            }
            ++n3;
        }
    }

    protected boolean func_175929_a(World world, Random random, BlockPos blockPos, int n) {
        return this.func_175926_c(world, blockPos, n) && this.func_175927_a(blockPos, world);
    }

    protected void func_175925_a(World world, BlockPos blockPos, int n) {
        int n2 = n * n;
        int n3 = -n;
        while (n3 <= n + 1) {
            int n4 = -n;
            while (n4 <= n + 1) {
                BlockPos blockPos2;
                Material material;
                int n5 = n3 - 1;
                int n6 = n4 - 1;
                if (!(n3 * n3 + n4 * n4 > n2 && n5 * n5 + n6 * n6 > n2 && n3 * n3 + n6 * n6 > n2 && n5 * n5 + n4 * n4 > n2 || (material = world.getBlockState(blockPos2 = blockPos.add(n3, 0, n4)).getBlock().getMaterial()) != Material.air && material != Material.leaves)) {
                    this.setBlockAndNotifyAdequately(world, blockPos2, this.leavesMetadata);
                }
                ++n4;
            }
            ++n3;
        }
    }
}

