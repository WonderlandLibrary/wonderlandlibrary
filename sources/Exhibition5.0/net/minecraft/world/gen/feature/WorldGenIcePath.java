// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;

public class WorldGenIcePath extends WorldGenerator
{
    private Block field_150555_a;
    private int field_150554_b;
    private static final String __OBFID = "CL_00000416";
    
    public WorldGenIcePath(final int p_i45454_1_) {
        this.field_150555_a = Blocks.packed_ice;
        this.field_150554_b = p_i45454_1_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, BlockPos p_180709_3_) {
        while (worldIn.isAirBlock(p_180709_3_) && p_180709_3_.getY() > 2) {
            p_180709_3_ = p_180709_3_.offsetDown();
        }
        if (worldIn.getBlockState(p_180709_3_).getBlock() != Blocks.snow) {
            return false;
        }
        final int var4 = p_180709_2_.nextInt(this.field_150554_b - 2) + 2;
        final byte var5 = 1;
        for (int var6 = p_180709_3_.getX() - var4; var6 <= p_180709_3_.getX() + var4; ++var6) {
            for (int var7 = p_180709_3_.getZ() - var4; var7 <= p_180709_3_.getZ() + var4; ++var7) {
                final int var8 = var6 - p_180709_3_.getX();
                final int var9 = var7 - p_180709_3_.getZ();
                if (var8 * var8 + var9 * var9 <= var4 * var4) {
                    for (int var10 = p_180709_3_.getY() - var5; var10 <= p_180709_3_.getY() + var5; ++var10) {
                        final BlockPos var11 = new BlockPos(var6, var10, var7);
                        final Block var12 = worldIn.getBlockState(var11).getBlock();
                        if (var12 == Blocks.dirt || var12 == Blocks.snow || var12 == Blocks.ice) {
                            worldIn.setBlockState(var11, this.field_150555_a.getDefaultState(), 2);
                        }
                    }
                }
            }
        }
        return true;
    }
}
