// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;

public class WorldGenClay extends WorldGenerator
{
    private Block field_150546_a;
    private int numberOfBlocks;
    private static final String __OBFID = "CL_00000405";
    
    public WorldGenClay(final int p_i2011_1_) {
        this.field_150546_a = Blocks.clay;
        this.numberOfBlocks = p_i2011_1_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, final BlockPos p_180709_3_) {
        if (worldIn.getBlockState(p_180709_3_).getBlock().getMaterial() != Material.water) {
            return false;
        }
        final int var4 = p_180709_2_.nextInt(this.numberOfBlocks - 2) + 2;
        final byte var5 = 1;
        for (int var6 = p_180709_3_.getX() - var4; var6 <= p_180709_3_.getX() + var4; ++var6) {
            for (int var7 = p_180709_3_.getZ() - var4; var7 <= p_180709_3_.getZ() + var4; ++var7) {
                final int var8 = var6 - p_180709_3_.getX();
                final int var9 = var7 - p_180709_3_.getZ();
                if (var8 * var8 + var9 * var9 <= var4 * var4) {
                    for (int var10 = p_180709_3_.getY() - var5; var10 <= p_180709_3_.getY() + var5; ++var10) {
                        final BlockPos var11 = new BlockPos(var6, var10, var7);
                        final Block var12 = worldIn.getBlockState(var11).getBlock();
                        if (var12 == Blocks.dirt || var12 == Blocks.clay) {
                            worldIn.setBlockState(var11, this.field_150546_a.getDefaultState(), 2);
                        }
                    }
                }
            }
        }
        return true;
    }
}
