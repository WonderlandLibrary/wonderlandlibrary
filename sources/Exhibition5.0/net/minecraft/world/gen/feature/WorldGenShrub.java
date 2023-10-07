// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class WorldGenShrub extends WorldGenTrees
{
    private int field_150528_a;
    private int field_150527_b;
    private static final String __OBFID = "CL_00000411";
    
    public WorldGenShrub(final int p_i2015_1_, final int p_i2015_2_) {
        super(false);
        this.field_150527_b = p_i2015_1_;
        this.field_150528_a = p_i2015_2_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, BlockPos p_180709_3_) {
        Block var4;
        while (((var4 = worldIn.getBlockState(p_180709_3_).getBlock()).getMaterial() == Material.air || var4.getMaterial() == Material.leaves) && p_180709_3_.getY() > 0) {
            p_180709_3_ = p_180709_3_.offsetDown();
        }
        final Block var5 = worldIn.getBlockState(p_180709_3_).getBlock();
        if (var5 == Blocks.dirt || var5 == Blocks.grass) {
            p_180709_3_ = p_180709_3_.offsetUp();
            this.func_175905_a(worldIn, p_180709_3_, Blocks.log, this.field_150527_b);
            for (int var6 = p_180709_3_.getY(); var6 <= p_180709_3_.getY() + 2; ++var6) {
                final int var7 = var6 - p_180709_3_.getY();
                for (int var8 = 2 - var7, var9 = p_180709_3_.getX() - var8; var9 <= p_180709_3_.getX() + var8; ++var9) {
                    final int var10 = var9 - p_180709_3_.getX();
                    for (int var11 = p_180709_3_.getZ() - var8; var11 <= p_180709_3_.getZ() + var8; ++var11) {
                        final int var12 = var11 - p_180709_3_.getZ();
                        if (Math.abs(var10) != var8 || Math.abs(var12) != var8 || p_180709_2_.nextInt(2) != 0) {
                            final BlockPos var13 = new BlockPos(var9, var6, var11);
                            if (!worldIn.getBlockState(var13).getBlock().isFullBlock()) {
                                this.func_175905_a(worldIn, var13, Blocks.leaves, this.field_150528_a);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
