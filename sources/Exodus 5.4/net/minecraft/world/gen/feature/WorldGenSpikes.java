/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenSpikes
extends WorldGenerator {
    private Block baseBlockRequired;

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        if (world.isAirBlock(blockPos) && world.getBlockState(blockPos.down()).getBlock() == this.baseBlockRequired) {
            int n;
            int n2;
            int n3;
            int n4 = random.nextInt(32) + 6;
            int n5 = random.nextInt(4) + 1;
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            int n6 = blockPos.getX() - n5;
            while (n6 <= blockPos.getX() + n5) {
                n3 = blockPos.getZ() - n5;
                while (n3 <= blockPos.getZ() + n5) {
                    n2 = n6 - blockPos.getX();
                    if (n2 * n2 + (n = n3 - blockPos.getZ()) * n <= n5 * n5 + 1 && world.getBlockState(mutableBlockPos.func_181079_c(n6, blockPos.getY() - 1, n3)).getBlock() != this.baseBlockRequired) {
                        return false;
                    }
                    ++n3;
                }
                ++n6;
            }
            n6 = blockPos.getY();
            while (n6 < blockPos.getY() + n4 && n6 < 256) {
                n3 = blockPos.getX() - n5;
                while (n3 <= blockPos.getX() + n5) {
                    n2 = blockPos.getZ() - n5;
                    while (n2 <= blockPos.getZ() + n5) {
                        int n7;
                        n = n3 - blockPos.getX();
                        if (n * n + (n7 = n2 - blockPos.getZ()) * n7 <= n5 * n5 + 1) {
                            world.setBlockState(new BlockPos(n3, n6, n2), Blocks.obsidian.getDefaultState(), 2);
                        }
                        ++n2;
                    }
                    ++n3;
                }
                ++n6;
            }
            EntityEnderCrystal entityEnderCrystal = new EntityEnderCrystal(world);
            entityEnderCrystal.setLocationAndAngles((float)blockPos.getX() + 0.5f, blockPos.getY() + n4, (float)blockPos.getZ() + 0.5f, random.nextFloat() * 360.0f, 0.0f);
            world.spawnEntityInWorld(entityEnderCrystal);
            world.setBlockState(blockPos.up(n4), Blocks.bedrock.getDefaultState(), 2);
            return true;
        }
        return false;
    }

    public WorldGenSpikes(Block block) {
        this.baseBlockRequired = block;
    }
}

