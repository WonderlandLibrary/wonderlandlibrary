// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.BlockFlower;

public class WorldGenFlowers extends WorldGenerator
{
    private BlockFlower flower;
    private IBlockState field_175915_b;
    private static final String __OBFID = "CL_00000410";
    
    public WorldGenFlowers(final BlockFlower p_i45632_1_, final BlockFlower.EnumFlowerType p_i45632_2_) {
        this.setGeneratedBlock(p_i45632_1_, p_i45632_2_);
    }
    
    public void setGeneratedBlock(final BlockFlower p_175914_1_, final BlockFlower.EnumFlowerType p_175914_2_) {
        this.flower = p_175914_1_;
        this.field_175915_b = p_175914_1_.getDefaultState().withProperty(p_175914_1_.func_176494_l(), p_175914_2_);
    }
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, final BlockPos p_180709_3_) {
        for (int var4 = 0; var4 < 64; ++var4) {
            final BlockPos var5 = p_180709_3_.add(p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8), p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8));
            if (worldIn.isAirBlock(var5) && (!worldIn.provider.getHasNoSky() || var5.getY() < 255) && this.flower.canBlockStay(worldIn, var5, this.field_175915_b)) {
                worldIn.setBlockState(var5, this.field_175915_b, 2);
            }
        }
        return true;
    }
}
