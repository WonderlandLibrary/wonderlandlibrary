/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenTaiga2
extends WorldGenAbstractTree {
    private static final String __OBFID = "CL_00000435";

    public WorldGenTaiga2(boolean p_i2025_1_) {
        super(p_i2025_1_);
    }

    @Override
    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_) {
        int var4 = p_180709_2_.nextInt(4) + 6;
        int var5 = 1 + p_180709_2_.nextInt(2);
        int var6 = var4 - var5;
        int var7 = 2 + p_180709_2_.nextInt(2);
        boolean var8 = true;
        if (p_180709_3_.getY() >= 1 && p_180709_3_.getY() + var4 + 1 <= 256) {
            int var11;
            int var21;
            for (int var9 = p_180709_3_.getY(); var9 <= p_180709_3_.getY() + 1 + var4 && var8; ++var9) {
                boolean var10 = true;
                var21 = var9 - p_180709_3_.getY() < var5 ? 0 : var7;
                for (var11 = p_180709_3_.getX() - var21; var11 <= p_180709_3_.getX() + var21 && var8; ++var11) {
                    for (int var12 = p_180709_3_.getZ() - var21; var12 <= p_180709_3_.getZ() + var21 && var8; ++var12) {
                        if (var9 >= 0 && var9 < 256) {
                            Block var13 = worldIn.getBlockState(new BlockPos(var11, var9, var12)).getBlock();
                            if (var13.getMaterial() == Material.air || var13.getMaterial() == Material.leaves) continue;
                            var8 = false;
                            continue;
                        }
                        var8 = false;
                    }
                }
            }
            if (!var8) {
                return false;
            }
            Block var20 = worldIn.getBlockState(p_180709_3_.offsetDown()).getBlock();
            if ((var20 == Blocks.grass || var20 == Blocks.dirt || var20 == Blocks.farmland) && p_180709_3_.getY() < 256 - var4 - 1) {
                int var14;
                int var23;
                this.func_175921_a(worldIn, p_180709_3_.offsetDown());
                var21 = p_180709_2_.nextInt(2);
                var11 = 1;
                int var22 = 0;
                for (var23 = 0; var23 <= var6; ++var23) {
                    var14 = p_180709_3_.getY() + var4 - var23;
                    for (int var15 = p_180709_3_.getX() - var21; var15 <= p_180709_3_.getX() + var21; ++var15) {
                        int var16 = var15 - p_180709_3_.getX();
                        for (int var17 = p_180709_3_.getZ() - var21; var17 <= p_180709_3_.getZ() + var21; ++var17) {
                            BlockPos var19;
                            int var18 = var17 - p_180709_3_.getZ();
                            if (Math.abs(var16) == var21 && Math.abs(var18) == var21 && var21 > 0 || worldIn.getBlockState(var19 = new BlockPos(var15, var14, var17)).getBlock().isFullBlock()) continue;
                            this.func_175905_a(worldIn, var19, Blocks.leaves, BlockPlanks.EnumType.SPRUCE.func_176839_a());
                        }
                    }
                    if (var21 >= var11) {
                        var21 = var22;
                        var22 = 1;
                        if (++var11 <= var7) continue;
                        var11 = var7;
                        continue;
                    }
                    ++var21;
                }
                var23 = p_180709_2_.nextInt(3);
                for (var14 = 0; var14 < var4 - var23; ++var14) {
                    Block var24 = worldIn.getBlockState(p_180709_3_.offsetUp(var14)).getBlock();
                    if (var24.getMaterial() != Material.air && var24.getMaterial() != Material.leaves) continue;
                    this.func_175905_a(worldIn, p_180709_3_.offsetUp(var14), Blocks.log, BlockPlanks.EnumType.SPRUCE.func_176839_a());
                }
                return true;
            }
            return false;
        }
        return false;
    }
}

