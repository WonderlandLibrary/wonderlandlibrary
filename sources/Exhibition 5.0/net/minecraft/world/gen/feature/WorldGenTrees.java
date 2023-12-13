// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockVine;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class WorldGenTrees extends WorldGenAbstractTree
{
    private final int minTreeHeight;
    private final boolean vinesGrow;
    private final int metaWood;
    private final int metaLeaves;
    private static final String __OBFID = "CL_00000438";
    
    public WorldGenTrees(final boolean p_i2027_1_) {
        this(p_i2027_1_, 4, 0, 0, false);
    }
    
    public WorldGenTrees(final boolean p_i2028_1_, final int p_i2028_2_, final int p_i2028_3_, final int p_i2028_4_, final boolean p_i2028_5_) {
        super(p_i2028_1_);
        this.minTreeHeight = p_i2028_2_;
        this.metaWood = p_i2028_3_;
        this.metaLeaves = p_i2028_4_;
        this.vinesGrow = p_i2028_5_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, final BlockPos p_180709_3_) {
        final int var4 = p_180709_2_.nextInt(3) + this.minTreeHeight;
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
            final byte var7 = 3;
            final byte var11 = 0;
            for (int var9 = p_180709_3_.getY() - var7 + var4; var9 <= p_180709_3_.getY() + var4; ++var9) {
                final int var12 = var9 - (p_180709_3_.getY() + var4);
                for (int var13 = var11 + 1 - var12 / 2, var14 = p_180709_3_.getX() - var13; var14 <= p_180709_3_.getX() + var13; ++var14) {
                    final int var15 = var14 - p_180709_3_.getX();
                    for (int var16 = p_180709_3_.getZ() - var13; var16 <= p_180709_3_.getZ() + var13; ++var16) {
                        final int var17 = var16 - p_180709_3_.getZ();
                        if (Math.abs(var15) != var13 || Math.abs(var17) != var13 || (p_180709_2_.nextInt(2) != 0 && var12 != 0)) {
                            final BlockPos var18 = new BlockPos(var14, var9, var16);
                            final Block var19 = worldIn.getBlockState(var18).getBlock();
                            if (var19.getMaterial() == Material.air || var19.getMaterial() == Material.leaves || var19.getMaterial() == Material.vine) {
                                this.func_175905_a(worldIn, var18, Blocks.leaves, this.metaLeaves);
                            }
                        }
                    }
                }
            }
            for (int var9 = 0; var9 < var4; ++var9) {
                final Block var20 = worldIn.getBlockState(p_180709_3_.offsetUp(var9)).getBlock();
                if (var20.getMaterial() == Material.air || var20.getMaterial() == Material.leaves || var20.getMaterial() == Material.vine) {
                    this.func_175905_a(worldIn, p_180709_3_.offsetUp(var9), Blocks.log, this.metaWood);
                    if (this.vinesGrow && var9 > 0) {
                        if (p_180709_2_.nextInt(3) > 0 && worldIn.isAirBlock(p_180709_3_.add(-1, var9, 0))) {
                            this.func_175905_a(worldIn, p_180709_3_.add(-1, var9, 0), Blocks.vine, BlockVine.field_176275_S);
                        }
                        if (p_180709_2_.nextInt(3) > 0 && worldIn.isAirBlock(p_180709_3_.add(1, var9, 0))) {
                            this.func_175905_a(worldIn, p_180709_3_.add(1, var9, 0), Blocks.vine, BlockVine.field_176271_T);
                        }
                        if (p_180709_2_.nextInt(3) > 0 && worldIn.isAirBlock(p_180709_3_.add(0, var9, -1))) {
                            this.func_175905_a(worldIn, p_180709_3_.add(0, var9, -1), Blocks.vine, BlockVine.field_176272_Q);
                        }
                        if (p_180709_2_.nextInt(3) > 0 && worldIn.isAirBlock(p_180709_3_.add(0, var9, 1))) {
                            this.func_175905_a(worldIn, p_180709_3_.add(0, var9, 1), Blocks.vine, BlockVine.field_176276_R);
                        }
                    }
                }
            }
            if (this.vinesGrow) {
                for (int var9 = p_180709_3_.getY() - 3 + var4; var9 <= p_180709_3_.getY() + var4; ++var9) {
                    final int var12 = var9 - (p_180709_3_.getY() + var4);
                    for (int var13 = 2 - var12 / 2, var14 = p_180709_3_.getX() - var13; var14 <= p_180709_3_.getX() + var13; ++var14) {
                        for (int var15 = p_180709_3_.getZ() - var13; var15 <= p_180709_3_.getZ() + var13; ++var15) {
                            final BlockPos var21 = new BlockPos(var14, var9, var15);
                            if (worldIn.getBlockState(var21).getBlock().getMaterial() == Material.leaves) {
                                final BlockPos var22 = var21.offsetWest();
                                final BlockPos var18 = var21.offsetEast();
                                final BlockPos var23 = var21.offsetNorth();
                                final BlockPos var24 = var21.offsetSouth();
                                if (p_180709_2_.nextInt(4) == 0 && worldIn.getBlockState(var22).getBlock().getMaterial() == Material.air) {
                                    this.func_175923_a(worldIn, var22, BlockVine.field_176275_S);
                                }
                                if (p_180709_2_.nextInt(4) == 0 && worldIn.getBlockState(var18).getBlock().getMaterial() == Material.air) {
                                    this.func_175923_a(worldIn, var18, BlockVine.field_176271_T);
                                }
                                if (p_180709_2_.nextInt(4) == 0 && worldIn.getBlockState(var23).getBlock().getMaterial() == Material.air) {
                                    this.func_175923_a(worldIn, var23, BlockVine.field_176272_Q);
                                }
                                if (p_180709_2_.nextInt(4) == 0 && worldIn.getBlockState(var24).getBlock().getMaterial() == Material.air) {
                                    this.func_175923_a(worldIn, var24, BlockVine.field_176276_R);
                                }
                            }
                        }
                    }
                }
                if (p_180709_2_.nextInt(5) == 0 && var4 > 5) {
                    for (int var9 = 0; var9 < 2; ++var9) {
                        for (int var12 = 0; var12 < 4; ++var12) {
                            if (p_180709_2_.nextInt(4 - var9) == 0) {
                                final int var13 = p_180709_2_.nextInt(3);
                                final EnumFacing var25 = EnumFacing.getHorizontal(var12).getOpposite();
                                this.func_175905_a(worldIn, p_180709_3_.add(var25.getFrontOffsetX(), var4 - 5 + var9, var25.getFrontOffsetZ()), Blocks.cocoa, var13 << 2 | EnumFacing.getHorizontal(var12).getHorizontalIndex());
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    private void func_175923_a(final World worldIn, BlockPos p_175923_2_, final int p_175923_3_) {
        this.func_175905_a(worldIn, p_175923_2_, Blocks.vine, p_175923_3_);
        int var4;
        for (var4 = 4, p_175923_2_ = p_175923_2_.offsetDown(); worldIn.getBlockState(p_175923_2_).getBlock().getMaterial() == Material.air && var4 > 0; p_175923_2_ = p_175923_2_.offsetDown(), --var4) {
            this.func_175905_a(worldIn, p_175923_2_, Blocks.vine, p_175923_3_);
        }
    }
}
