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

    public WorldGenSpikes(Block p_i45464_1_) {
        this.baseBlockRequired = p_i45464_1_;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        if (worldIn.isAirBlock(position) && worldIn.getBlockState(position.down()).getBlock() == this.baseBlockRequired) {
            int i2 = rand.nextInt(32) + 6;
            int j2 = rand.nextInt(4) + 1;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int k2 = position.getX() - j2; k2 <= position.getX() + j2; ++k2) {
                for (int l2 = position.getZ() - j2; l2 <= position.getZ() + j2; ++l2) {
                    int j1;
                    int i1 = k2 - position.getX();
                    if (i1 * i1 + (j1 = l2 - position.getZ()) * j1 > j2 * j2 + 1 || worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(k2, position.getY() - 1, l2)).getBlock() == this.baseBlockRequired) continue;
                    return false;
                }
            }
            for (int l1 = position.getY(); l1 < position.getY() + i2 && l1 < 256; ++l1) {
                for (int i22 = position.getX() - j2; i22 <= position.getX() + j2; ++i22) {
                    for (int j22 = position.getZ() - j2; j22 <= position.getZ() + j2; ++j22) {
                        int k1;
                        int k2 = i22 - position.getX();
                        if (k2 * k2 + (k1 = j22 - position.getZ()) * k1 > j2 * j2 + 1) continue;
                        worldIn.setBlockState(new BlockPos(i22, l1, j22), Blocks.obsidian.getDefaultState(), 2);
                    }
                }
            }
            EntityEnderCrystal entity = new EntityEnderCrystal(worldIn);
            entity.setLocationAndAngles((float)position.getX() + 0.5f, position.getY() + i2, (float)position.getZ() + 0.5f, rand.nextFloat() * 360.0f, 0.0f);
            worldIn.spawnEntityInWorld(entity);
            worldIn.setBlockState(position.up(i2), Blocks.bedrock.getDefaultState(), 2);
            return true;
        }
        return false;
    }
}

