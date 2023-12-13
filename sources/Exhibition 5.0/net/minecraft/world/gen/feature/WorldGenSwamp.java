// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.BlockVine;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class WorldGenSwamp extends WorldGenAbstractTree
{
    private static final String __OBFID = "CL_00000436";
    
    public WorldGenSwamp() {
        super(false);
    }
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, BlockPos p_180709_3_) {
        final int var4 = p_180709_2_.nextInt(4) + 5;
        while (worldIn.getBlockState(p_180709_3_.offsetDown()).getBlock().getMaterial() == Material.water) {
            p_180709_3_ = p_180709_3_.offsetDown();
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
                var7 = 3;
            }
            for (int var8 = p_180709_3_.getX() - var7; var8 <= p_180709_3_.getX() + var7 && var5; ++var8) {
                for (int var9 = p_180709_3_.getZ() - var7; var9 <= p_180709_3_.getZ() + var7 && var5; ++var9) {
                    if (var6 >= 0 && var6 < 256) {
                        final Block var10 = worldIn.getBlockState(new BlockPos(var8, var6, var9)).getBlock();
                        if (var10.getMaterial() != Material.air && var10.getMaterial() != Material.leaves) {
                            if (var10 != Blocks.water && var10 != Blocks.flowing_water) {
                                var5 = false;
                            }
                            else if (var6 > p_180709_3_.getY()) {
                                var5 = false;
                            }
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
        final Block var11 = worldIn.getBlockState(p_180709_3_.offsetDown()).getBlock();
        if ((var11 == Blocks.grass || var11 == Blocks.dirt) && p_180709_3_.getY() < 256 - var4 - 1) {
            this.func_175921_a(worldIn, p_180709_3_.offsetDown());
            for (int var12 = p_180709_3_.getY() - 3 + var4; var12 <= p_180709_3_.getY() + var4; ++var12) {
                final int var8 = var12 - (p_180709_3_.getY() + var4);
                for (int var9 = 2 - var8 / 2, var13 = p_180709_3_.getX() - var9; var13 <= p_180709_3_.getX() + var9; ++var13) {
                    final int var14 = var13 - p_180709_3_.getX();
                    for (int var15 = p_180709_3_.getZ() - var9; var15 <= p_180709_3_.getZ() + var9; ++var15) {
                        final int var16 = var15 - p_180709_3_.getZ();
                        if (Math.abs(var14) != var9 || Math.abs(var16) != var9 || (p_180709_2_.nextInt(2) != 0 && var8 != 0)) {
                            final BlockPos var17 = new BlockPos(var13, var12, var15);
                            if (!worldIn.getBlockState(var17).getBlock().isFullBlock()) {
                                this.func_175906_a(worldIn, var17, Blocks.leaves);
                            }
                        }
                    }
                }
            }
            for (int var12 = 0; var12 < var4; ++var12) {
                final Block var18 = worldIn.getBlockState(p_180709_3_.offsetUp(var12)).getBlock();
                if (var18.getMaterial() == Material.air || var18.getMaterial() == Material.leaves || var18 == Blocks.flowing_water || var18 == Blocks.water) {
                    this.func_175906_a(worldIn, p_180709_3_.offsetUp(var12), Blocks.log);
                }
            }
            for (int var12 = p_180709_3_.getY() - 3 + var4; var12 <= p_180709_3_.getY() + var4; ++var12) {
                final int var8 = var12 - (p_180709_3_.getY() + var4);
                for (int var9 = 2 - var8 / 2, var13 = p_180709_3_.getX() - var9; var13 <= p_180709_3_.getX() + var9; ++var13) {
                    for (int var14 = p_180709_3_.getZ() - var9; var14 <= p_180709_3_.getZ() + var9; ++var14) {
                        final BlockPos var19 = new BlockPos(var13, var12, var14);
                        if (worldIn.getBlockState(var19).getBlock().getMaterial() == Material.leaves) {
                            final BlockPos var20 = var19.offsetWest();
                            final BlockPos var17 = var19.offsetEast();
                            final BlockPos var21 = var19.offsetNorth();
                            final BlockPos var22 = var19.offsetSouth();
                            if (p_180709_2_.nextInt(4) == 0 && worldIn.getBlockState(var20).getBlock().getMaterial() == Material.air) {
                                this.func_175922_a(worldIn, var20, BlockVine.field_176275_S);
                            }
                            if (p_180709_2_.nextInt(4) == 0 && worldIn.getBlockState(var17).getBlock().getMaterial() == Material.air) {
                                this.func_175922_a(worldIn, var17, BlockVine.field_176271_T);
                            }
                            if (p_180709_2_.nextInt(4) == 0 && worldIn.getBlockState(var21).getBlock().getMaterial() == Material.air) {
                                this.func_175922_a(worldIn, var21, BlockVine.field_176272_Q);
                            }
                            if (p_180709_2_.nextInt(4) == 0 && worldIn.getBlockState(var22).getBlock().getMaterial() == Material.air) {
                                this.func_175922_a(worldIn, var22, BlockVine.field_176276_R);
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    private void func_175922_a(final World worldIn, BlockPos p_175922_2_, final int p_175922_3_) {
        this.func_175905_a(worldIn, p_175922_2_, Blocks.vine, p_175922_3_);
        int var4;
        for (var4 = 4, p_175922_2_ = p_175922_2_.offsetDown(); worldIn.getBlockState(p_175922_2_).getBlock().getMaterial() == Material.air && var4 > 0; p_175922_2_ = p_175922_2_.offsetDown(), --var4) {
            this.func_175905_a(worldIn, p_175922_2_, Blocks.vine, p_175922_3_);
        }
    }
}
