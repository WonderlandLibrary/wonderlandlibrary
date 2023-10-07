// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class WorldGenSavannaTree extends WorldGenAbstractTree
{
    private static final String __OBFID = "CL_00000432";
    
    public WorldGenSavannaTree(final boolean p_i45463_1_) {
        super(p_i45463_1_);
    }
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, final BlockPos p_180709_3_) {
        final int var4 = p_180709_2_.nextInt(3) + p_180709_2_.nextInt(3) + 5;
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
        if ((var10 == Blocks.grass || var10 == Blocks.dirt) && p_180709_3_.getY() < 256 - var4 - 1) {
            this.func_175921_a(worldIn, p_180709_3_.offsetDown());
            final EnumFacing var11 = EnumFacing.Plane.HORIZONTAL.random(p_180709_2_);
            final int var8 = var4 - p_180709_2_.nextInt(4) - 1;
            int var9 = 3 - p_180709_2_.nextInt(3);
            int var12 = p_180709_3_.getX();
            int var13 = p_180709_3_.getZ();
            int var14 = 0;
            for (int var15 = 0; var15 < var4; ++var15) {
                final int var16 = p_180709_3_.getY() + var15;
                if (var15 >= var8 && var9 > 0) {
                    var12 += var11.getFrontOffsetX();
                    var13 += var11.getFrontOffsetZ();
                    --var9;
                }
                final BlockPos var17 = new BlockPos(var12, var16, var13);
                final Material var18 = worldIn.getBlockState(var17).getBlock().getMaterial();
                if (var18 == Material.air || var18 == Material.leaves) {
                    this.func_175905_a(worldIn, var17, Blocks.log2, BlockPlanks.EnumType.ACACIA.func_176839_a() - 4);
                    var14 = var16;
                }
            }
            BlockPos var19 = new BlockPos(var12, var14, var13);
            for (int var16 = -3; var16 <= 3; ++var16) {
                for (int var20 = -3; var20 <= 3; ++var20) {
                    if (Math.abs(var16) != 3 || Math.abs(var20) != 3) {
                        this.func_175924_b(worldIn, var19.add(var16, 0, var20));
                    }
                }
            }
            var19 = var19.offsetUp();
            for (int var16 = -1; var16 <= 1; ++var16) {
                for (int var20 = -1; var20 <= 1; ++var20) {
                    this.func_175924_b(worldIn, var19.add(var16, 0, var20));
                }
            }
            this.func_175924_b(worldIn, var19.offsetEast(2));
            this.func_175924_b(worldIn, var19.offsetWest(2));
            this.func_175924_b(worldIn, var19.offsetSouth(2));
            this.func_175924_b(worldIn, var19.offsetNorth(2));
            var12 = p_180709_3_.getX();
            var13 = p_180709_3_.getZ();
            final EnumFacing var21 = EnumFacing.Plane.HORIZONTAL.random(p_180709_2_);
            if (var21 != var11) {
                final int var16 = var8 - p_180709_2_.nextInt(2) - 1;
                int var20 = 1 + p_180709_2_.nextInt(3);
                var14 = 0;
                for (int var22 = var16; var22 < var4 && var20 > 0; ++var22, --var20) {
                    if (var22 >= 1) {
                        final int var23 = p_180709_3_.getY() + var22;
                        var12 += var21.getFrontOffsetX();
                        var13 += var21.getFrontOffsetZ();
                        final BlockPos var24 = new BlockPos(var12, var23, var13);
                        final Material var25 = worldIn.getBlockState(var24).getBlock().getMaterial();
                        if (var25 == Material.air || var25 == Material.leaves) {
                            this.func_175905_a(worldIn, var24, Blocks.log2, BlockPlanks.EnumType.ACACIA.func_176839_a() - 4);
                            var14 = var23;
                        }
                    }
                }
                if (var14 > 0) {
                    BlockPos var26 = new BlockPos(var12, var14, var13);
                    for (int var23 = -2; var23 <= 2; ++var23) {
                        for (int var27 = -2; var27 <= 2; ++var27) {
                            if (Math.abs(var23) != 2 || Math.abs(var27) != 2) {
                                this.func_175924_b(worldIn, var26.add(var23, 0, var27));
                            }
                        }
                    }
                    var26 = var26.offsetUp();
                    for (int var23 = -1; var23 <= 1; ++var23) {
                        for (int var27 = -1; var27 <= 1; ++var27) {
                            this.func_175924_b(worldIn, var26.add(var23, 0, var27));
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    private void func_175924_b(final World worldIn, final BlockPos p_175924_2_) {
        final Material var3 = worldIn.getBlockState(p_175924_2_).getBlock().getMaterial();
        if (var3 == Material.air || var3 == Material.leaves) {
            this.func_175905_a(worldIn, p_175924_2_, Blocks.leaves2, 0);
        }
    }
}
