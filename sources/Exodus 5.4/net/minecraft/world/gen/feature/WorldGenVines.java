/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenVines
extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        while (blockPos.getY() < 128) {
            if (world.isAirBlock(blockPos)) {
                EnumFacing[] enumFacingArray = EnumFacing.Plane.HORIZONTAL.facings();
                int n = enumFacingArray.length;
                int n2 = 0;
                while (n2 < n) {
                    EnumFacing enumFacing = enumFacingArray[n2];
                    if (Blocks.vine.canPlaceBlockOnSide(world, blockPos, enumFacing)) {
                        IBlockState iBlockState = Blocks.vine.getDefaultState().withProperty(BlockVine.NORTH, enumFacing == EnumFacing.NORTH).withProperty(BlockVine.EAST, enumFacing == EnumFacing.EAST).withProperty(BlockVine.SOUTH, enumFacing == EnumFacing.SOUTH).withProperty(BlockVine.WEST, enumFacing == EnumFacing.WEST);
                        world.setBlockState(blockPos, iBlockState, 2);
                        break;
                    }
                    ++n2;
                }
            } else {
                blockPos = blockPos.add(random.nextInt(4) - random.nextInt(4), 0, random.nextInt(4) - random.nextInt(4));
            }
            blockPos = blockPos.up();
        }
        return true;
    }
}

