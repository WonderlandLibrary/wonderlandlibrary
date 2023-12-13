/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenClay
extends WorldGenerator {
    private Block field_150546_a = Blocks.clay;
    private int numberOfBlocks;
    private static final String __OBFID = "CL_00000405";

    public WorldGenClay(int p_i2011_1_) {
        this.numberOfBlocks = p_i2011_1_;
    }

    @Override
    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_) {
        if (worldIn.getBlockState(p_180709_3_).getBlock().getMaterial() != Material.water) {
            return false;
        }
        int var4 = p_180709_2_.nextInt(this.numberOfBlocks - 2) + 2;
        int var5 = 1;
        for (int var6 = p_180709_3_.getX() - var4; var6 <= p_180709_3_.getX() + var4; ++var6) {
            for (int var7 = p_180709_3_.getZ() - var4; var7 <= p_180709_3_.getZ() + var4; ++var7) {
                int var9;
                int var8 = var6 - p_180709_3_.getX();
                if (var8 * var8 + (var9 = var7 - p_180709_3_.getZ()) * var9 > var4 * var4) continue;
                for (int var10 = p_180709_3_.getY() - var5; var10 <= p_180709_3_.getY() + var5; ++var10) {
                    BlockPos var11 = new BlockPos(var6, var10, var7);
                    Block var12 = worldIn.getBlockState(var11).getBlock();
                    if (var12 != Blocks.dirt && var12 != Blocks.clay) continue;
                    worldIn.setBlockState(var11, this.field_150546_a.getDefaultState(), 2);
                }
            }
        }
        return true;
    }
}

