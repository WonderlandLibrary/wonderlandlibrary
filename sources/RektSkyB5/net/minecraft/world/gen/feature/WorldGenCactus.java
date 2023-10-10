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
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        for (int i2 = 0; i2 < 10; ++i2) {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (!worldIn.isAirBlock(blockpos)) continue;
            int j2 = 1 + rand.nextInt(rand.nextInt(3) + 1);
            for (int k2 = 0; k2 < j2; ++k2) {
                if (!Blocks.cactus.canBlockStay(worldIn, blockpos)) continue;
                worldIn.setBlockState(blockpos.up(k2), Blocks.cactus.getDefaultState(), 2);
            }
        }
        return true;
    }
}

