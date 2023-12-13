// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.block.material.Material;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.Block;

public class WorldGenLakes extends WorldGenerator
{
    private Block field_150556_a;
    private static final String __OBFID = "CL_00000418";
    
    public WorldGenLakes(final Block p_i45455_1_) {
        this.field_150556_a = p_i45455_1_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, BlockPos p_180709_3_) {
        for (p_180709_3_ = p_180709_3_.add(-8, 0, -8); p_180709_3_.getY() > 5 && worldIn.isAirBlock(p_180709_3_); p_180709_3_ = p_180709_3_.offsetDown()) {}
        if (p_180709_3_.getY() <= 4) {
            return false;
        }
        p_180709_3_ = p_180709_3_.offsetDown(4);
        final boolean[] var4 = new boolean[2048];
        for (int var5 = p_180709_2_.nextInt(4) + 4, var6 = 0; var6 < var5; ++var6) {
            final double var7 = p_180709_2_.nextDouble() * 6.0 + 3.0;
            final double var8 = p_180709_2_.nextDouble() * 4.0 + 2.0;
            final double var9 = p_180709_2_.nextDouble() * 6.0 + 3.0;
            final double var10 = p_180709_2_.nextDouble() * (16.0 - var7 - 2.0) + 1.0 + var7 / 2.0;
            final double var11 = p_180709_2_.nextDouble() * (8.0 - var8 - 4.0) + 2.0 + var8 / 2.0;
            final double var12 = p_180709_2_.nextDouble() * (16.0 - var9 - 2.0) + 1.0 + var9 / 2.0;
            for (int var13 = 1; var13 < 15; ++var13) {
                for (int var14 = 1; var14 < 15; ++var14) {
                    for (int var15 = 1; var15 < 7; ++var15) {
                        final double var16 = (var13 - var10) / (var7 / 2.0);
                        final double var17 = (var15 - var11) / (var8 / 2.0);
                        final double var18 = (var14 - var12) / (var9 / 2.0);
                        final double var19 = var16 * var16 + var17 * var17 + var18 * var18;
                        if (var19 < 1.0) {
                            var4[(var13 * 16 + var14) * 8 + var15] = true;
                        }
                    }
                }
            }
        }
        for (int var6 = 0; var6 < 16; ++var6) {
            for (int var20 = 0; var20 < 16; ++var20) {
                for (int var21 = 0; var21 < 8; ++var21) {
                    final boolean var22 = !var4[(var6 * 16 + var20) * 8 + var21] && ((var6 < 15 && var4[((var6 + 1) * 16 + var20) * 8 + var21]) || (var6 > 0 && var4[((var6 - 1) * 16 + var20) * 8 + var21]) || (var20 < 15 && var4[(var6 * 16 + var20 + 1) * 8 + var21]) || (var20 > 0 && var4[(var6 * 16 + (var20 - 1)) * 8 + var21]) || (var21 < 7 && var4[(var6 * 16 + var20) * 8 + var21 + 1]) || (var21 > 0 && var4[(var6 * 16 + var20) * 8 + (var21 - 1)]));
                    if (var22) {
                        final Material var23 = worldIn.getBlockState(p_180709_3_.add(var6, var21, var20)).getBlock().getMaterial();
                        if (var21 >= 4 && var23.isLiquid()) {
                            return false;
                        }
                        if (var21 < 4 && !var23.isSolid() && worldIn.getBlockState(p_180709_3_.add(var6, var21, var20)).getBlock() != this.field_150556_a) {
                            return false;
                        }
                    }
                }
            }
        }
        for (int var6 = 0; var6 < 16; ++var6) {
            for (int var20 = 0; var20 < 16; ++var20) {
                for (int var21 = 0; var21 < 8; ++var21) {
                    if (var4[(var6 * 16 + var20) * 8 + var21]) {
                        worldIn.setBlockState(p_180709_3_.add(var6, var21, var20), (var21 >= 4) ? Blocks.air.getDefaultState() : this.field_150556_a.getDefaultState(), 2);
                    }
                }
            }
        }
        for (int var6 = 0; var6 < 16; ++var6) {
            for (int var20 = 0; var20 < 16; ++var20) {
                for (int var21 = 4; var21 < 8; ++var21) {
                    if (var4[(var6 * 16 + var20) * 8 + var21]) {
                        final BlockPos var24 = p_180709_3_.add(var6, var21 - 1, var20);
                        if (worldIn.getBlockState(var24).getBlock() == Blocks.dirt && worldIn.getLightFor(EnumSkyBlock.SKY, p_180709_3_.add(var6, var21, var20)) > 0) {
                            final BiomeGenBase var25 = worldIn.getBiomeGenForCoords(var24);
                            if (var25.topBlock.getBlock() == Blocks.mycelium) {
                                worldIn.setBlockState(var24, Blocks.mycelium.getDefaultState(), 2);
                            }
                            else {
                                worldIn.setBlockState(var24, Blocks.grass.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }
        }
        if (this.field_150556_a.getMaterial() == Material.lava) {
            for (int var6 = 0; var6 < 16; ++var6) {
                for (int var20 = 0; var20 < 16; ++var20) {
                    for (int var21 = 0; var21 < 8; ++var21) {
                        final boolean var22 = !var4[(var6 * 16 + var20) * 8 + var21] && ((var6 < 15 && var4[((var6 + 1) * 16 + var20) * 8 + var21]) || (var6 > 0 && var4[((var6 - 1) * 16 + var20) * 8 + var21]) || (var20 < 15 && var4[(var6 * 16 + var20 + 1) * 8 + var21]) || (var20 > 0 && var4[(var6 * 16 + (var20 - 1)) * 8 + var21]) || (var21 < 7 && var4[(var6 * 16 + var20) * 8 + var21 + 1]) || (var21 > 0 && var4[(var6 * 16 + var20) * 8 + (var21 - 1)]));
                        if (var22 && (var21 < 4 || p_180709_2_.nextInt(2) != 0) && worldIn.getBlockState(p_180709_3_.add(var6, var21, var20)).getBlock().getMaterial().isSolid()) {
                            worldIn.setBlockState(p_180709_3_.add(var6, var21, var20), Blocks.stone.getDefaultState(), 2);
                        }
                    }
                }
            }
        }
        if (this.field_150556_a.getMaterial() == Material.water) {
            for (int var6 = 0; var6 < 16; ++var6) {
                for (int var20 = 0; var20 < 16; ++var20) {
                    final byte var26 = 4;
                    if (worldIn.func_175675_v(p_180709_3_.add(var6, var26, var20))) {
                        worldIn.setBlockState(p_180709_3_.add(var6, var26, var20), Blocks.ice.getDefaultState(), 2);
                    }
                }
            }
        }
        return true;
    }
}
