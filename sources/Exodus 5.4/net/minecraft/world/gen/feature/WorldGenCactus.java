/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenCactus
extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        int n = 0;
        while (n < 10) {
            BlockPos blockPos2 = blockPos.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
            if (world.isAirBlock(blockPos2)) {
                int n2 = 1 + random.nextInt(random.nextInt(3) + 1);
                int n3 = 0;
                while (n3 < n2) {
                    if (Blocks.cactus.canBlockStay(world, blockPos2)) {
                        world.setBlockState(blockPos2.up(n3), Blocks.cactus.getDefaultState(), 2);
                    }
                    ++n3;
                }
            }
            ++n;
        }
        return true;
    }
}

