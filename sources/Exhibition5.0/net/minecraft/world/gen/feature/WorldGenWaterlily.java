// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class WorldGenWaterlily extends WorldGenerator
{
    private static final String __OBFID = "CL_00000189";
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, final BlockPos p_180709_3_) {
        for (int var4 = 0; var4 < 10; ++var4) {
            final int var5 = p_180709_3_.getX() + p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8);
            final int var6 = p_180709_3_.getY() + p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4);
            final int var7 = p_180709_3_.getZ() + p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8);
            if (worldIn.isAirBlock(new BlockPos(var5, var6, var7)) && Blocks.waterlily.canPlaceBlockAt(worldIn, new BlockPos(var5, var6, var7))) {
                worldIn.setBlockState(new BlockPos(var5, var6, var7), Blocks.waterlily.getDefaultState(), 2);
            }
        }
        return true;
    }
}
