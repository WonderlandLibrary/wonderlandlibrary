/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenReed
extends WorldGenerator {
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        for (int i2 = 0; i2 < 20; ++i2) {
            BlockPos blockpos1;
            BlockPos blockpos = position.add(rand.nextInt(4) - rand.nextInt(4), 0, rand.nextInt(4) - rand.nextInt(4));
            if (!worldIn.isAirBlock(blockpos) || worldIn.getBlockState((blockpos1 = blockpos.down()).west()).getBlock().getMaterial() != Material.water && worldIn.getBlockState(blockpos1.east()).getBlock().getMaterial() != Material.water && worldIn.getBlockState(blockpos1.north()).getBlock().getMaterial() != Material.water && worldIn.getBlockState(blockpos1.south()).getBlock().getMaterial() != Material.water) continue;
            int j2 = 2 + rand.nextInt(rand.nextInt(3) + 1);
            for (int k2 = 0; k2 < j2; ++k2) {
                if (!Blocks.reeds.canBlockStay(worldIn, blockpos)) continue;
                worldIn.setBlockState(blockpos.up(k2), Blocks.reeds.getDefaultState(), 2);
            }
        }
        return true;
    }
}

