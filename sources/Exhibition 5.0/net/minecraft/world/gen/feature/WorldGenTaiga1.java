// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class WorldGenTaiga1 extends WorldGenAbstractTree
{
    private static final String __OBFID = "CL_00000427";
    
    public WorldGenTaiga1() {
        super(false);
    }
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, final BlockPos p_180709_3_) {
        final int var4 = p_180709_2_.nextInt(5) + 7;
        final int var5 = var4 - p_180709_2_.nextInt(2) - 3;
        final int var6 = var4 - var5;
        final int var7 = 1 + p_180709_2_.nextInt(var6 + 1);
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
                        if (!this.func_150523_a(worldIn.getBlockState(new BlockPos(var12, var9, var13)).getBlock())) {
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
        final Block var14 = worldIn.getBlockState(p_180709_3_.offsetDown()).getBlock();
        if ((var14 == Blocks.grass || var14 == Blocks.dirt) && p_180709_3_.getY() < 256 - var4 - 1) {
            this.func_175921_a(worldIn, p_180709_3_.offsetDown());
            int var11 = 0;
            for (int var12 = p_180709_3_.getY() + var4; var12 >= p_180709_3_.getY() + var5; --var12) {
                for (int var13 = p_180709_3_.getX() - var11; var13 <= p_180709_3_.getX() + var11; ++var13) {
                    final int var15 = var13 - p_180709_3_.getX();
                    for (int var16 = p_180709_3_.getZ() - var11; var16 <= p_180709_3_.getZ() + var11; ++var16) {
                        final int var17 = var16 - p_180709_3_.getZ();
                        if (Math.abs(var15) != var11 || Math.abs(var17) != var11 || var11 <= 0) {
                            final BlockPos var18 = new BlockPos(var13, var12, var16);
                            if (!worldIn.getBlockState(var18).getBlock().isFullBlock()) {
                                this.func_175905_a(worldIn, var18, Blocks.leaves, BlockPlanks.EnumType.SPRUCE.func_176839_a());
                            }
                        }
                    }
                }
                if (var11 >= 1 && var12 == p_180709_3_.getY() + var5 + 1) {
                    --var11;
                }
                else if (var11 < var7) {
                    ++var11;
                }
            }
            for (int var12 = 0; var12 < var4 - 1; ++var12) {
                final Block var19 = worldIn.getBlockState(p_180709_3_.offsetUp(var12)).getBlock();
                if (var19.getMaterial() == Material.air || var19.getMaterial() == Material.leaves) {
                    this.func_175905_a(worldIn, p_180709_3_.offsetUp(var12), Blocks.log, BlockPlanks.EnumType.SPRUCE.func_176839_a());
                }
            }
            return true;
        }
        return false;
    }
}
