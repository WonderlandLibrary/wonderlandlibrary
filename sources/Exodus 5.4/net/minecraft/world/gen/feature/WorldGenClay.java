/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenClay
extends WorldGenerator {
    private Block field_150546_a = Blocks.clay;
    private int numberOfBlocks;

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        if (world.getBlockState(blockPos).getBlock().getMaterial() != Material.water) {
            return false;
        }
        int n = random.nextInt(this.numberOfBlocks - 2) + 2;
        int n2 = 1;
        int n3 = blockPos.getX() - n;
        while (n3 <= blockPos.getX() + n) {
            int n4 = blockPos.getZ() - n;
            while (n4 <= blockPos.getZ() + n) {
                int n5;
                int n6 = n3 - blockPos.getX();
                if (n6 * n6 + (n5 = n4 - blockPos.getZ()) * n5 <= n * n) {
                    int n7 = blockPos.getY() - n2;
                    while (n7 <= blockPos.getY() + n2) {
                        BlockPos blockPos2 = new BlockPos(n3, n7, n4);
                        Block block = world.getBlockState(blockPos2).getBlock();
                        if (block == Blocks.dirt || block == Blocks.clay) {
                            world.setBlockState(blockPos2, this.field_150546_a.getDefaultState(), 2);
                        }
                        ++n7;
                    }
                }
                ++n4;
            }
            ++n3;
        }
        return true;
    }

    public WorldGenClay(int n) {
        this.numberOfBlocks = n;
    }
}

