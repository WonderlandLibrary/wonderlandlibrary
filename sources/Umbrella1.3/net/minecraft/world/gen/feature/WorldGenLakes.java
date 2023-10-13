/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenLakes
extends WorldGenerator {
    private Block field_150556_a;
    private static final String __OBFID = "CL_00000418";

    public WorldGenLakes(Block p_i45455_1_) {
        this.field_150556_a = p_i45455_1_;
    }

    @Override
    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_) {
        int var30;
        int var6;
        p_180709_3_ = p_180709_3_.add(-8, 0, -8);
        while (p_180709_3_.getY() > 5 && worldIn.isAirBlock(p_180709_3_)) {
            p_180709_3_ = p_180709_3_.offsetDown();
        }
        if (p_180709_3_.getY() <= 4) {
            return false;
        }
        p_180709_3_ = p_180709_3_.offsetDown(4);
        boolean[] var4 = new boolean[2048];
        int var5 = p_180709_2_.nextInt(4) + 4;
        for (var6 = 0; var6 < var5; ++var6) {
            double var7 = p_180709_2_.nextDouble() * 6.0 + 3.0;
            double var9 = p_180709_2_.nextDouble() * 4.0 + 2.0;
            double var11 = p_180709_2_.nextDouble() * 6.0 + 3.0;
            double var13 = p_180709_2_.nextDouble() * (16.0 - var7 - 2.0) + 1.0 + var7 / 2.0;
            double var15 = p_180709_2_.nextDouble() * (8.0 - var9 - 4.0) + 2.0 + var9 / 2.0;
            double var17 = p_180709_2_.nextDouble() * (16.0 - var11 - 2.0) + 1.0 + var11 / 2.0;
            for (int var19 = 1; var19 < 15; ++var19) {
                for (int var20 = 1; var20 < 15; ++var20) {
                    for (int var21 = 1; var21 < 7; ++var21) {
                        double var22 = ((double)var19 - var13) / (var7 / 2.0);
                        double var24 = ((double)var21 - var15) / (var9 / 2.0);
                        double var26 = ((double)var20 - var17) / (var11 / 2.0);
                        double var28 = var22 * var22 + var24 * var24 + var26 * var26;
                        if (!(var28 < 1.0)) continue;
                        var4[(var19 * 16 + var20) * 8 + var21] = true;
                    }
                }
            }
        }
        for (var6 = 0; var6 < 16; ++var6) {
            for (var30 = 0; var30 < 16; ++var30) {
                for (int var8 = 0; var8 < 8; ++var8) {
                    boolean var32;
                    boolean bl = var32 = !var4[(var6 * 16 + var30) * 8 + var8] && (var6 < 15 && var4[((var6 + 1) * 16 + var30) * 8 + var8] || var6 > 0 && var4[((var6 - 1) * 16 + var30) * 8 + var8] || var30 < 15 && var4[(var6 * 16 + var30 + 1) * 8 + var8] || var30 > 0 && var4[(var6 * 16 + (var30 - 1)) * 8 + var8] || var8 < 7 && var4[(var6 * 16 + var30) * 8 + var8 + 1] || var8 > 0 && var4[(var6 * 16 + var30) * 8 + (var8 - 1)]);
                    if (!var32) continue;
                    Material var10 = worldIn.getBlockState(p_180709_3_.add(var6, var8, var30)).getBlock().getMaterial();
                    if (var8 >= 4 && var10.isLiquid()) {
                        return false;
                    }
                    if (var8 >= 4 || var10.isSolid() || worldIn.getBlockState(p_180709_3_.add(var6, var8, var30)).getBlock() == this.field_150556_a) continue;
                    return false;
                }
            }
        }
        for (var6 = 0; var6 < 16; ++var6) {
            for (var30 = 0; var30 < 16; ++var30) {
                for (int var8 = 0; var8 < 8; ++var8) {
                    if (!var4[(var6 * 16 + var30) * 8 + var8]) continue;
                    worldIn.setBlockState(p_180709_3_.add(var6, var8, var30), var8 >= 4 ? Blocks.air.getDefaultState() : this.field_150556_a.getDefaultState(), 2);
                }
            }
        }
        for (var6 = 0; var6 < 16; ++var6) {
            for (var30 = 0; var30 < 16; ++var30) {
                for (int var8 = 4; var8 < 8; ++var8) {
                    BlockPos var33;
                    if (!var4[(var6 * 16 + var30) * 8 + var8] || worldIn.getBlockState(var33 = p_180709_3_.add(var6, var8 - 1, var30)).getBlock() != Blocks.dirt || worldIn.getLightFor(EnumSkyBlock.SKY, p_180709_3_.add(var6, var8, var30)) <= 0) continue;
                    BiomeGenBase var34 = worldIn.getBiomeGenForCoords(var33);
                    if (var34.topBlock.getBlock() == Blocks.mycelium) {
                        worldIn.setBlockState(var33, Blocks.mycelium.getDefaultState(), 2);
                        continue;
                    }
                    worldIn.setBlockState(var33, Blocks.grass.getDefaultState(), 2);
                }
            }
        }
        if (this.field_150556_a.getMaterial() == Material.lava) {
            for (var6 = 0; var6 < 16; ++var6) {
                for (var30 = 0; var30 < 16; ++var30) {
                    for (int var8 = 0; var8 < 8; ++var8) {
                        boolean var32;
                        boolean bl = var32 = !var4[(var6 * 16 + var30) * 8 + var8] && (var6 < 15 && var4[((var6 + 1) * 16 + var30) * 8 + var8] || var6 > 0 && var4[((var6 - 1) * 16 + var30) * 8 + var8] || var30 < 15 && var4[(var6 * 16 + var30 + 1) * 8 + var8] || var30 > 0 && var4[(var6 * 16 + (var30 - 1)) * 8 + var8] || var8 < 7 && var4[(var6 * 16 + var30) * 8 + var8 + 1] || var8 > 0 && var4[(var6 * 16 + var30) * 8 + (var8 - 1)]);
                        if (!var32 || var8 >= 4 && p_180709_2_.nextInt(2) == 0 || !worldIn.getBlockState(p_180709_3_.add(var6, var8, var30)).getBlock().getMaterial().isSolid()) continue;
                        worldIn.setBlockState(p_180709_3_.add(var6, var8, var30), Blocks.stone.getDefaultState(), 2);
                    }
                }
            }
        }
        if (this.field_150556_a.getMaterial() == Material.water) {
            for (var6 = 0; var6 < 16; ++var6) {
                for (var30 = 0; var30 < 16; ++var30) {
                    int var31 = 4;
                    if (!worldIn.func_175675_v(p_180709_3_.add(var6, var31, var30))) continue;
                    worldIn.setBlockState(p_180709_3_.add(var6, var31, var30), Blocks.ice.getDefaultState(), 2);
                }
            }
        }
        return true;
    }
}

