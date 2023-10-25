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

public class WorldGenDeadBush
extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        Block block;
        while (((block = world.getBlockState(blockPos).getBlock()).getMaterial() == Material.air || block.getMaterial() == Material.leaves) && blockPos.getY() > 0) {
            blockPos = blockPos.down();
        }
        int n = 0;
        while (n < 4) {
            BlockPos blockPos2 = blockPos.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
            if (world.isAirBlock(blockPos2) && Blocks.deadbush.canBlockStay(world, blockPos2, Blocks.deadbush.getDefaultState())) {
                world.setBlockState(blockPos2, Blocks.deadbush.getDefaultState(), 2);
            }
            ++n;
        }
        return true;
    }
}

