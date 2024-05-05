/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public abstract class WorldGenAbstractTree
extends WorldGenerator {
    public WorldGenAbstractTree(boolean p_i45448_1_) {
        super(p_i45448_1_);
    }

    protected boolean func_150523_a(Block p_150523_1_) {
        Material material = p_150523_1_.getMaterial();
        if (material == Material.air) return true;
        if (material == Material.leaves) return true;
        if (p_150523_1_ == Blocks.grass) return true;
        if (p_150523_1_ == Blocks.dirt) return true;
        if (p_150523_1_ == Blocks.log) return true;
        if (p_150523_1_ == Blocks.log2) return true;
        if (p_150523_1_ == Blocks.sapling) return true;
        if (p_150523_1_ == Blocks.vine) return true;
        return false;
    }

    public void func_180711_a(World worldIn, Random p_180711_2_, BlockPos p_180711_3_) {
    }

    protected void func_175921_a(World worldIn, BlockPos p_175921_2_) {
        if (worldIn.getBlockState(p_175921_2_).getBlock() == Blocks.dirt) return;
        this.setBlockAndNotifyAdequately(worldIn, p_175921_2_, Blocks.dirt.getDefaultState());
    }
}

