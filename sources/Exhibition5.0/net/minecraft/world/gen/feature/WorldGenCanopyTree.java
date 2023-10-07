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

public class WorldGenCanopyTree extends WorldGenAbstractTree
{
    private static final String __OBFID = "CL_00000430";
    
    public WorldGenCanopyTree(final boolean p_i45461_1_) {
        super(p_i45461_1_);
    }
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, final BlockPos p_180709_3_) {
        final int var4 = p_180709_2_.nextInt(3) + p_180709_2_.nextInt(2) + 6;
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
            this.func_175921_a(worldIn, p_180709_3_.add(1, -1, 0));
            this.func_175921_a(worldIn, p_180709_3_.add(1, -1, 1));
            this.func_175921_a(worldIn, p_180709_3_.add(0, -1, 1));
            final EnumFacing var11 = EnumFacing.Plane.HORIZONTAL.random(p_180709_2_);
            final int var8 = var4 - p_180709_2_.nextInt(4);
            int var9 = 2 - p_180709_2_.nextInt(3);
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
                    this.func_175905_a(worldIn, var17, Blocks.log2, BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4);
                    this.func_175905_a(worldIn, var17.offsetEast(), Blocks.log2, BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4);
                    this.func_175905_a(worldIn, var17.offsetSouth(), Blocks.log2, BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4);
                    this.func_175905_a(worldIn, var17.offsetEast().offsetSouth(), Blocks.log2, BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4);
                    var14 = var16;
                }
            }
            for (int var15 = -2; var15 <= 0; ++var15) {
                for (int var16 = -2; var16 <= 0; ++var16) {
                    final byte var19 = -1;
                    this.func_150526_a(worldIn, var12 + var15, var14 + var19, var13 + var16);
                    this.func_150526_a(worldIn, 1 + var12 - var15, var14 + var19, var13 + var16);
                    this.func_150526_a(worldIn, var12 + var15, var14 + var19, 1 + var13 - var16);
                    this.func_150526_a(worldIn, 1 + var12 - var15, var14 + var19, 1 + var13 - var16);
                    if ((var15 > -2 || var16 > -1) && (var15 != -1 || var16 != -2)) {
                        final byte var20 = 1;
                        this.func_150526_a(worldIn, var12 + var15, var14 + var20, var13 + var16);
                        this.func_150526_a(worldIn, 1 + var12 - var15, var14 + var20, var13 + var16);
                        this.func_150526_a(worldIn, var12 + var15, var14 + var20, 1 + var13 - var16);
                        this.func_150526_a(worldIn, 1 + var12 - var15, var14 + var20, 1 + var13 - var16);
                    }
                }
            }
            if (p_180709_2_.nextBoolean()) {
                this.func_150526_a(worldIn, var12, var14 + 2, var13);
                this.func_150526_a(worldIn, var12 + 1, var14 + 2, var13);
                this.func_150526_a(worldIn, var12 + 1, var14 + 2, var13 + 1);
                this.func_150526_a(worldIn, var12, var14 + 2, var13 + 1);
            }
            for (int var15 = -3; var15 <= 4; ++var15) {
                for (int var16 = -3; var16 <= 4; ++var16) {
                    if ((var15 != -3 || var16 != -3) && (var15 != -3 || var16 != 4) && (var15 != 4 || var16 != -3) && (var15 != 4 || var16 != 4) && (Math.abs(var15) < 3 || Math.abs(var16) < 3)) {
                        this.func_150526_a(worldIn, var12 + var15, var14, var13 + var16);
                    }
                }
            }
            for (int var15 = -1; var15 <= 2; ++var15) {
                for (int var16 = -1; var16 <= 2; ++var16) {
                    if ((var15 < 0 || var15 > 1 || var16 < 0 || var16 > 1) && p_180709_2_.nextInt(3) <= 0) {
                        for (int var21 = p_180709_2_.nextInt(3) + 2, var22 = 0; var22 < var21; ++var22) {
                            this.func_175905_a(worldIn, new BlockPos(p_180709_3_.getX() + var15, var14 - var22 - 1, p_180709_3_.getZ() + var16), Blocks.log2, BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4);
                        }
                        for (int var22 = -1; var22 <= 1; ++var22) {
                            for (int var23 = -1; var23 <= 1; ++var23) {
                                this.func_150526_a(worldIn, var12 + var15 + var22, var14 - 0, var13 + var16 + var23);
                            }
                        }
                        for (int var22 = -2; var22 <= 2; ++var22) {
                            for (int var23 = -2; var23 <= 2; ++var23) {
                                if (Math.abs(var22) != 2 || Math.abs(var23) != 2) {
                                    this.func_150526_a(worldIn, var12 + var15 + var22, var14 - 1, var13 + var16 + var23);
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    private void func_150526_a(final World worldIn, final int p_150526_2_, final int p_150526_3_, final int p_150526_4_) {
        final Block var5 = worldIn.getBlockState(new BlockPos(p_150526_2_, p_150526_3_, p_150526_4_)).getBlock();
        if (var5.getMaterial() == Material.air) {
            this.func_175905_a(worldIn, new BlockPos(p_150526_2_, p_150526_3_, p_150526_4_), Blocks.leaves2, 1);
        }
    }
}
