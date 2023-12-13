// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class WorldGenPumpkin extends WorldGenerator
{
    private static final String __OBFID = "CL_00000428";
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, final BlockPos p_180709_3_) {
        for (int var4 = 0; var4 < 64; ++var4) {
            final BlockPos var5 = p_180709_3_.add(p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8), p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8));
            if (worldIn.isAirBlock(var5) && worldIn.getBlockState(var5.offsetDown()).getBlock() == Blocks.grass && Blocks.pumpkin.canPlaceBlockAt(worldIn, var5)) {
                worldIn.setBlockState(var5, Blocks.pumpkin.getDefaultState().withProperty(BlockPumpkin.AGE, EnumFacing.Plane.HORIZONTAL.random(p_180709_2_)), 2);
            }
        }
        return true;
    }
}
