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
    public boolean generate(World world, Random random, BlockPos blockPos) {
        int n = 0;
        while (n < 20) {
            BlockPos blockPos2;
            BlockPos blockPos3 = blockPos.add(random.nextInt(4) - random.nextInt(4), 0, random.nextInt(4) - random.nextInt(4));
            if (world.isAirBlock(blockPos3) && (world.getBlockState((blockPos2 = blockPos3.down()).west()).getBlock().getMaterial() == Material.water || world.getBlockState(blockPos2.east()).getBlock().getMaterial() == Material.water || world.getBlockState(blockPos2.north()).getBlock().getMaterial() == Material.water || world.getBlockState(blockPos2.south()).getBlock().getMaterial() == Material.water)) {
                int n2 = 2 + random.nextInt(random.nextInt(3) + 1);
                int n3 = 0;
                while (n3 < n2) {
                    if (Blocks.reeds.canBlockStay(world, blockPos3)) {
                        world.setBlockState(blockPos3.up(n3), Blocks.reeds.getDefaultState(), 2);
                    }
                    ++n3;
                }
            }
            ++n;
        }
        return true;
    }
}

