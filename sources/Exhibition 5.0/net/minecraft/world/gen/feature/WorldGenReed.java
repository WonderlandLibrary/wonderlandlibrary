// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class WorldGenReed extends WorldGenerator
{
    private static final String __OBFID = "CL_00000429";
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, final BlockPos p_180709_3_) {
        for (int var4 = 0; var4 < 20; ++var4) {
            final BlockPos var5 = p_180709_3_.add(p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), 0, p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4));
            if (worldIn.isAirBlock(var5)) {
                final BlockPos var6 = var5.offsetDown();
                if (worldIn.getBlockState(var6.offsetWest()).getBlock().getMaterial() == Material.water || worldIn.getBlockState(var6.offsetEast()).getBlock().getMaterial() == Material.water || worldIn.getBlockState(var6.offsetNorth()).getBlock().getMaterial() == Material.water || worldIn.getBlockState(var6.offsetSouth()).getBlock().getMaterial() == Material.water) {
                    for (int var7 = 2 + p_180709_2_.nextInt(p_180709_2_.nextInt(3) + 1), var8 = 0; var8 < var7; ++var8) {
                        if (Blocks.reeds.func_176354_d(worldIn, var5)) {
                            worldIn.setBlockState(var5.offsetUp(var8), Blocks.reeds.getDefaultState(), 2);
                        }
                    }
                }
            }
        }
        return true;
    }
}
