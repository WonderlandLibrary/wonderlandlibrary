/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenMelon
extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        int n = 0;
        while (n < 64) {
            BlockPos blockPos2 = blockPos.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
            if (Blocks.melon_block.canPlaceBlockAt(world, blockPos2) && world.getBlockState(blockPos2.down()).getBlock() == Blocks.grass) {
                world.setBlockState(blockPos2, Blocks.melon_block.getDefaultState(), 2);
            }
            ++n;
        }
        return true;
    }
}

