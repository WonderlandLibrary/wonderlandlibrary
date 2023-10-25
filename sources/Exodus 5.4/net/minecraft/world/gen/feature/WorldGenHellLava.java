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

public class WorldGenHellLava
extends WorldGenerator {
    private final Block field_150553_a;
    private final boolean field_94524_b;

    public WorldGenHellLava(Block block, boolean bl) {
        this.field_150553_a = block;
        this.field_94524_b = bl;
    }

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        if (world.getBlockState(blockPos.up()).getBlock() != Blocks.netherrack) {
            return false;
        }
        if (world.getBlockState(blockPos).getBlock().getMaterial() != Material.air && world.getBlockState(blockPos).getBlock() != Blocks.netherrack) {
            return false;
        }
        int n = 0;
        if (world.getBlockState(blockPos.west()).getBlock() == Blocks.netherrack) {
            ++n;
        }
        if (world.getBlockState(blockPos.east()).getBlock() == Blocks.netherrack) {
            ++n;
        }
        if (world.getBlockState(blockPos.north()).getBlock() == Blocks.netherrack) {
            ++n;
        }
        if (world.getBlockState(blockPos.south()).getBlock() == Blocks.netherrack) {
            ++n;
        }
        if (world.getBlockState(blockPos.down()).getBlock() == Blocks.netherrack) {
            ++n;
        }
        int n2 = 0;
        if (world.isAirBlock(blockPos.west())) {
            ++n2;
        }
        if (world.isAirBlock(blockPos.east())) {
            ++n2;
        }
        if (world.isAirBlock(blockPos.north())) {
            ++n2;
        }
        if (world.isAirBlock(blockPos.south())) {
            ++n2;
        }
        if (world.isAirBlock(blockPos.down())) {
            ++n2;
        }
        if (!this.field_94524_b && n == 4 && n2 == 1 || n == 5) {
            world.setBlockState(blockPos, this.field_150553_a.getDefaultState(), 2);
            world.forceBlockUpdateTick(this.field_150553_a, blockPos, random);
        }
        return true;
    }
}

