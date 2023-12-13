/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00000439";

    @Override
    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_) {
        while (p_180709_3_.getY() < 128) {
            if (worldIn.isAirBlock(p_180709_3_)) {
                for (EnumFacing var7 : EnumFacing.Plane.HORIZONTAL.facings()) {
                    if (!Blocks.vine.canPlaceBlockOnSide(worldIn, p_180709_3_, var7)) continue;
                    IBlockState var8 = Blocks.vine.getDefaultState().withProperty(BlockVine.field_176273_b, Boolean.valueOf(var7 == EnumFacing.NORTH)).withProperty(BlockVine.field_176278_M, Boolean.valueOf(var7 == EnumFacing.EAST)).withProperty(BlockVine.field_176279_N, Boolean.valueOf(var7 == EnumFacing.SOUTH)).withProperty(BlockVine.field_176280_O, Boolean.valueOf(var7 == EnumFacing.WEST));
                    worldIn.setBlockState(p_180709_3_, var8, 2);
                    break;
                }
            } else {
                p_180709_3_ = p_180709_3_.add(p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), 0, p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4));
            }
            p_180709_3_ = p_180709_3_.offsetUp();
        }
        return true;
    }
}

