/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenMelon
extends WorldGenerator {
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        for (int i = 0; i < 64; ++i) {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (!Blocks.MELON_BLOCK.canPlaceBlockAt(worldIn, blockpos) || worldIn.getBlockState(blockpos.down()).getBlock() != Blocks.GRASS) continue;
            worldIn.setBlockState(blockpos, Blocks.MELON_BLOCK.getDefaultState(), 2);
        }
        return true;
    }
}

