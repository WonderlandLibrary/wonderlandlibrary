/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenReed
extends WorldGenerator {
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        for (int i = 0; i < 20; ++i) {
            BlockPos blockpos1;
            BlockPos blockpos = position.add(rand.nextInt(4) - rand.nextInt(4), 0, rand.nextInt(4) - rand.nextInt(4));
            if (!worldIn.isAirBlock(blockpos) || worldIn.getBlockState((blockpos1 = blockpos.down()).west()).getMaterial() != Material.WATER && worldIn.getBlockState(blockpos1.east()).getMaterial() != Material.WATER && worldIn.getBlockState(blockpos1.north()).getMaterial() != Material.WATER && worldIn.getBlockState(blockpos1.south()).getMaterial() != Material.WATER) continue;
            int j = 2 + rand.nextInt(rand.nextInt(3) + 1);
            for (int k = 0; k < j; ++k) {
                if (!Blocks.REEDS.canBlockStay(worldIn, blockpos)) continue;
                worldIn.setBlockState(blockpos.up(k), Blocks.REEDS.getDefaultState(), 2);
            }
        }
        return true;
    }
}

