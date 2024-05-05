package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenVines
extends WorldGenerator {
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        while (position.getY() < 128) {
            if (worldIn.isAirBlock(position)) {
                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL.facings()) {
                    if (!Blocks.vine.canPlaceBlockOnSide(worldIn, position, enumfacing)) continue;
                    IBlockState iblockstate = Blocks.vine.getDefaultState().withProperty((IProperty)BlockVine.NORTH, (Comparable)Boolean.valueOf((enumfacing == EnumFacing.NORTH ? 1 : 0) != 0)).withProperty((IProperty)BlockVine.EAST, (Comparable)Boolean.valueOf((enumfacing == EnumFacing.EAST ? 1 : 0) != 0)).withProperty((IProperty)BlockVine.SOUTH, (Comparable)Boolean.valueOf((enumfacing == EnumFacing.SOUTH ? 1 : 0) != 0)).withProperty((IProperty)BlockVine.WEST, (Comparable)Boolean.valueOf((enumfacing == EnumFacing.WEST ? 1 : 0) != 0));
                    worldIn.setBlockState(position, iblockstate, 2);
                    break;
                }
            } else {
                position = position.add(rand.nextInt(4) - rand.nextInt(4), 0, rand.nextInt(4) - rand.nextInt(4));
            }
            position = position.up();
        }
        return true;
    }
}
