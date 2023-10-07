// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;

public class WorldGenTallGrass extends WorldGenerator
{
    private final IBlockState field_175907_a;
    private static final String __OBFID = "CL_00000437";
    
    public WorldGenTallGrass(final BlockTallGrass.EnumType p_i45629_1_) {
        this.field_175907_a = Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.field_176497_a, p_i45629_1_);
    }
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, BlockPos p_180709_3_) {
        Block var4;
        while (((var4 = worldIn.getBlockState(p_180709_3_).getBlock()).getMaterial() == Material.air || var4.getMaterial() == Material.leaves) && p_180709_3_.getY() > 0) {
            p_180709_3_ = p_180709_3_.offsetDown();
        }
        for (int var5 = 0; var5 < 128; ++var5) {
            final BlockPos var6 = p_180709_3_.add(p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8), p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8));
            if (worldIn.isAirBlock(var6) && Blocks.tallgrass.canBlockStay(worldIn, var6, this.field_175907_a)) {
                worldIn.setBlockState(var6, this.field_175907_a, 2);
            }
        }
        return true;
    }
}
