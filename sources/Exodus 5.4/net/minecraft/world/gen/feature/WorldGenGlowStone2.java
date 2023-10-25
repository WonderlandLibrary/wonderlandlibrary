/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenGlowStone2
extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        if (!world.isAirBlock(blockPos)) {
            return false;
        }
        if (world.getBlockState(blockPos.up()).getBlock() != Blocks.netherrack) {
            return false;
        }
        world.setBlockState(blockPos, Blocks.glowstone.getDefaultState(), 2);
        int n = 0;
        while (n < 1500) {
            BlockPos blockPos2 = blockPos.add(random.nextInt(8) - random.nextInt(8), -random.nextInt(12), random.nextInt(8) - random.nextInt(8));
            if (world.getBlockState(blockPos2).getBlock().getMaterial() == Material.air) {
                int n2 = 0;
                EnumFacing[] enumFacingArray = EnumFacing.values();
                int n3 = enumFacingArray.length;
                int n4 = 0;
                while (n4 < n3) {
                    EnumFacing enumFacing = enumFacingArray[n4];
                    if (world.getBlockState(blockPos2.offset(enumFacing)).getBlock() == Blocks.glowstone) {
                        ++n2;
                    }
                    if (n2 > 1) break;
                    ++n4;
                }
                if (n2 == 1) {
                    world.setBlockState(blockPos2, Blocks.glowstone.getDefaultState(), 2);
                }
            }
            ++n;
        }
        return true;
    }
}

