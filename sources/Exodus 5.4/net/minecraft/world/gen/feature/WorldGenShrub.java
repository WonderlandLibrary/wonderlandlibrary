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
import net.minecraft.world.gen.feature.WorldGenTrees;

public class WorldGenShrub
extends WorldGenTrees {
    private final IBlockState leavesMetadata;
    private final IBlockState woodMetadata;

    public WorldGenShrub(IBlockState iBlockState, IBlockState iBlockState2) {
        super(false);
        this.woodMetadata = iBlockState;
        this.leavesMetadata = iBlockState2;
    }

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        Block block;
        while (((block = world.getBlockState(blockPos).getBlock()).getMaterial() == Material.air || block.getMaterial() == Material.leaves) && blockPos.getY() > 0) {
            blockPos = blockPos.down();
        }
        Block block2 = world.getBlockState(blockPos).getBlock();
        if (block2 == Blocks.dirt || block2 == Blocks.grass) {
            blockPos = blockPos.up();
            this.setBlockAndNotifyAdequately(world, blockPos, this.woodMetadata);
            int n = blockPos.getY();
            while (n <= blockPos.getY() + 2) {
                int n2 = n - blockPos.getY();
                int n3 = 2 - n2;
                int n4 = blockPos.getX() - n3;
                while (n4 <= blockPos.getX() + n3) {
                    int n5 = n4 - blockPos.getX();
                    int n6 = blockPos.getZ() - n3;
                    while (n6 <= blockPos.getZ() + n3) {
                        BlockPos blockPos2;
                        int n7 = n6 - blockPos.getZ();
                        if (!(Math.abs(n5) == n3 && Math.abs(n7) == n3 && random.nextInt(2) == 0 || world.getBlockState(blockPos2 = new BlockPos(n4, n, n6)).getBlock().isFullBlock())) {
                            this.setBlockAndNotifyAdequately(world, blockPos2, this.leavesMetadata);
                        }
                        ++n6;
                    }
                    ++n4;
                }
                ++n;
            }
        }
        return true;
    }
}

