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

public class WorldGenSand
extends WorldGenerator {
    private Block block;
    private int radius;

    public WorldGenSand(Block p_i45462_1_, int p_i45462_2_) {
        this.block = p_i45462_1_;
        this.radius = p_i45462_2_;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        if (worldIn.getBlockState(position).getBlock().getMaterial() != Material.water) {
            return false;
        }
        int i2 = rand.nextInt(this.radius - 2) + 2;
        int j2 = 2;
        for (int k2 = position.getX() - i2; k2 <= position.getX() + i2; ++k2) {
            for (int l2 = position.getZ() - i2; l2 <= position.getZ() + i2; ++l2) {
                int j1;
                int i1 = k2 - position.getX();
                if (i1 * i1 + (j1 = l2 - position.getZ()) * j1 > i2 * i2) continue;
                for (int k1 = position.getY() - j2; k1 <= position.getY() + j2; ++k1) {
                    BlockPos blockpos = new BlockPos(k2, k1, l2);
                    Block block = worldIn.getBlockState(blockpos).getBlock();
                    if (block != Blocks.dirt && block != Blocks.grass) continue;
                    worldIn.setBlockState(blockpos, this.block.getDefaultState(), 2);
                }
            }
        }
        return true;
    }
}

