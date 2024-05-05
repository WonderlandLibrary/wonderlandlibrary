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
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int i = 0;
        while (i < 10) {
            int l;
            int k;
            int j = position.getX() + rand.nextInt(8) - rand.nextInt(8);
            if (worldIn.isAirBlock(new BlockPos(j, k = position.getY() + rand.nextInt(4) - rand.nextInt(4), l = position.getZ() + rand.nextInt(8) - rand.nextInt(8))) && Blocks.waterlily.canPlaceBlockAt(worldIn, new BlockPos(j, k, l))) {
                worldIn.setBlockState(new BlockPos(j, k, l), Blocks.waterlily.getDefaultState(), 2);
            }
            ++i;
        }
        return true;
    }
}

