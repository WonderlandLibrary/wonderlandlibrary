// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class WorldGenForest extends WorldGenAbstractTree
{
    private boolean field_150531_a;
    private static final String __OBFID = "CL_00000401";
    
    public WorldGenForest(final boolean p_i45449_1_, final boolean p_i45449_2_) {
        super(p_i45449_1_);
        this.field_150531_a = p_i45449_2_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, final BlockPos p_180709_3_) {
        int var4 = p_180709_2_.nextInt(3) + 5;
        if (this.field_150531_a) {
            var4 += p_180709_2_.nextInt(7);
        }
        boolean var5 = true;
        if (p_180709_3_.getY() < 1 || p_180709_3_.getY() + var4 + 1 > 256) {
            return false;
        }
        for (int var6 = p_180709_3_.getY(); var6 <= p_180709_3_.getY() + 1 + var4; ++var6) {
            byte var7 = 1;
            if (var6 == p_180709_3_.getY()) {
                var7 = 0;
            }
            if (var6 >= p_180709_3_.getY() + 1 + var4 - 2) {
                var7 = 2;
            }
            for (int var8 = p_180709_3_.getX() - var7; var8 <= p_180709_3_.getX() + var7 && var5; ++var8) {
                for (int var9 = p_180709_3_.getZ() - var7; var9 <= p_180709_3_.getZ() + var7 && var5; ++var9) {
                    if (var6 >= 0 && var6 < 256) {
                        if (!this.func_150523_a(worldIn.getBlockState(new BlockPos(var8, var6, var9)).getBlock())) {
                            var5 = false;
                        }
                    }
                    else {
                        var5 = false;
                    }
                }
            }
        }
        if (!var5) {
            return false;
        }
        final Block var10 = worldIn.getBlockState(p_180709_3_.offsetDown()).getBlock();
        if ((var10 == Blocks.grass || var10 == Blocks.dirt || var10 == Blocks.farmland) && p_180709_3_.getY() < 256 - var4 - 1) {
            this.func_175921_a(worldIn, p_180709_3_.offsetDown());
            for (int var11 = p_180709_3_.getY() - 3 + var4; var11 <= p_180709_3_.getY() + var4; ++var11) {
                final int var8 = var11 - (p_180709_3_.getY() + var4);
                for (int var9 = 1 - var8 / 2, var12 = p_180709_3_.getX() - var9; var12 <= p_180709_3_.getX() + var9; ++var12) {
                    final int var13 = var12 - p_180709_3_.getX();
                    for (int var14 = p_180709_3_.getZ() - var9; var14 <= p_180709_3_.getZ() + var9; ++var14) {
                        final int var15 = var14 - p_180709_3_.getZ();
                        if (Math.abs(var13) != var9 || Math.abs(var15) != var9 || (p_180709_2_.nextInt(2) != 0 && var8 != 0)) {
                            final BlockPos var16 = new BlockPos(var12, var11, var14);
                            final Block var17 = worldIn.getBlockState(var16).getBlock();
                            if (var17.getMaterial() == Material.air || var17.getMaterial() == Material.leaves) {
                                this.func_175905_a(worldIn, var16, Blocks.leaves, BlockPlanks.EnumType.BIRCH.func_176839_a());
                            }
                        }
                    }
                }
            }
            for (int var11 = 0; var11 < var4; ++var11) {
                final Block var18 = worldIn.getBlockState(p_180709_3_.offsetUp(var11)).getBlock();
                if (var18.getMaterial() == Material.air || var18.getMaterial() == Material.leaves) {
                    this.func_175905_a(worldIn, p_180709_3_.offsetUp(var11), Blocks.log, BlockPlanks.EnumType.BIRCH.func_176839_a());
                }
            }
            return true;
        }
        return false;
    }
}
