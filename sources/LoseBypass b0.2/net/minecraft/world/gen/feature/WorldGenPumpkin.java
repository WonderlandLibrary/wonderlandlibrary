/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenPumpkin
extends WorldGenerator {
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int i = 0;
        while (i < 64) {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (worldIn.isAirBlock(blockpos) && worldIn.getBlockState(blockpos.down()).getBlock() == Blocks.grass && Blocks.pumpkin.canPlaceBlockAt(worldIn, blockpos)) {
                worldIn.setBlockState(blockpos, Blocks.pumpkin.getDefaultState().withProperty(BlockPumpkin.FACING, EnumFacing.Plane.HORIZONTAL.random(rand)), 2);
            }
            ++i;
        }
        return true;
    }
}

