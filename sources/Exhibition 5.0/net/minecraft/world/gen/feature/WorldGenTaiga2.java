// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class WorldGenTaiga2 extends WorldGenAbstractTree
{
    private static final String __OBFID = "CL_00000435";
    
    public WorldGenTaiga2(final boolean p_i2025_1_) {
        super(p_i2025_1_);
    }
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, final BlockPos p_180709_3_) {
        final int var4 = p_180709_2_.nextInt(4) + 6;
        final int var5 = 1 + p_180709_2_.nextInt(2);
        final int var6 = var4 - var5;
        final int var7 = 2 + p_180709_2_.nextInt(2);
        boolean var8 = true;
        if (p_180709_3_.getY() < 1 || p_180709_3_.getY() + var4 + 1 > 256) {
            return false;
        }
        for (int var9 = p_180709_3_.getY(); var9 <= p_180709_3_.getY() + 1 + var4 && var8; ++var9) {
            final boolean var10 = true;
            int var11;
            if (var9 - p_180709_3_.getY() < var5) {
                var11 = 0;
            }
            else {
                var11 = var7;
            }
            for (int var12 = p_180709_3_.getX() - var11; var12 <= p_180709_3_.getX() + var11 && var8; ++var12) {
                for (int var13 = p_180709_3_.getZ() - var11; var13 <= p_180709_3_.getZ() + var11 && var8; ++var13) {
                    if (var9 >= 0 && var9 < 256) {
                        final Block var14 = worldIn.getBlockState(new BlockPos(var12, var9, var13)).getBlock();
                        if (var14.getMaterial() != Material.air && var14.getMaterial() != Material.leaves) {
                            var8 = false;
                        }
                    }
                    else {
                        var8 = false;
                    }
                }
            }
        }
        if (!var8) {
            return false;
        }
        final Block var15 = worldIn.getBlockState(p_180709_3_.offsetDown()).getBlock();
        if ((var15 == Blocks.grass || var15 == Blocks.dirt || var15 == Blocks.farmland) && p_180709_3_.getY() < 256 - var4 - 1) {
            this.func_175921_a(worldIn, p_180709_3_.offsetDown());
            int var11 = p_180709_2_.nextInt(2);
            int var12 = 1;
            byte var16 = 0;
            for (int var17 = 0; var17 <= var6; ++var17) {
                final int var18 = p_180709_3_.getY() + var4 - var17;
                for (int var19 = p_180709_3_.getX() - var11; var19 <= p_180709_3_.getX() + var11; ++var19) {
                    final int var20 = var19 - p_180709_3_.getX();
                    for (int var21 = p_180709_3_.getZ() - var11; var21 <= p_180709_3_.getZ() + var11; ++var21) {
                        final int var22 = var21 - p_180709_3_.getZ();
                        if (Math.abs(var20) != var11 || Math.abs(var22) != var11 || var11 <= 0) {
                            final BlockPos var23 = new BlockPos(var19, var18, var21);
                            if (!worldIn.getBlockState(var23).getBlock().isFullBlock()) {
                                this.func_175905_a(worldIn, var23, Blocks.leaves, BlockPlanks.EnumType.SPRUCE.func_176839_a());
                            }
                        }
                    }
                }
                if (var11 >= var12) {
                    var11 = var16;
                    var16 = 1;
                    if (++var12 > var7) {
                        var12 = var7;
                    }
                }
                else {
                    ++var11;
                }
            }
            int var17;
            for (var17 = p_180709_2_.nextInt(3), int var18 = 0; var18 < var4 - var17; ++var18) {
                final Block var24 = worldIn.getBlockState(p_180709_3_.offsetUp(var18)).getBlock();
                if (var24.getMaterial() == Material.air || var24.getMaterial() == Material.leaves) {
                    this.func_175905_a(worldIn, p_180709_3_.offsetUp(var18), Blocks.log, BlockPlanks.EnumType.SPRUCE.func_176839_a());
                }
            }
            return true;
        }
        return false;
    }
}
