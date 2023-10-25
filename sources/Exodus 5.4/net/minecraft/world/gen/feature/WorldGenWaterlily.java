/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenWaterlily
extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        int n = 0;
        while (n < 10) {
            int n2;
            int n3;
            int n4 = blockPos.getX() + random.nextInt(8) - random.nextInt(8);
            if (world.isAirBlock(new BlockPos(n4, n3 = blockPos.getY() + random.nextInt(4) - random.nextInt(4), n2 = blockPos.getZ() + random.nextInt(8) - random.nextInt(8))) && Blocks.waterlily.canPlaceBlockAt(world, new BlockPos(n4, n3, n2))) {
                world.setBlockState(new BlockPos(n4, n3, n2), Blocks.waterlily.getDefaultState(), 2);
            }
            ++n;
        }
        return true;
    }
}

