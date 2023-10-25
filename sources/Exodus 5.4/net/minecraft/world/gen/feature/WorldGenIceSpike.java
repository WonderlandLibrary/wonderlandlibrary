/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenIceSpike
extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        int n;
        while (world.isAirBlock(blockPos) && blockPos.getY() > 2) {
            blockPos = blockPos.down();
        }
        if (world.getBlockState(blockPos).getBlock() != Blocks.snow) {
            return false;
        }
        blockPos = blockPos.up(random.nextInt(4));
        int n2 = random.nextInt(4) + 7;
        int n3 = n2 / 4 + random.nextInt(2);
        if (n3 > 1 && random.nextInt(60) == 0) {
            blockPos = blockPos.up(10 + random.nextInt(30));
        }
        int n4 = 0;
        while (n4 < n2) {
            float f = (1.0f - (float)n4 / (float)n2) * (float)n3;
            n = MathHelper.ceiling_float_int(f);
            int n5 = -n;
            while (n5 <= n) {
                float f2 = (float)MathHelper.abs_int(n5) - 0.25f;
                int n6 = -n;
                while (n6 <= n) {
                    float f3 = (float)MathHelper.abs_int(n6) - 0.25f;
                    if ((n5 == 0 && n6 == 0 || f2 * f2 + f3 * f3 <= f * f) && (n5 != -n && n5 != n && n6 != -n && n6 != n || random.nextFloat() <= 0.75f)) {
                        Block block = world.getBlockState(blockPos.add(n5, n4, n6)).getBlock();
                        if (block.getMaterial() == Material.air || block == Blocks.dirt || block == Blocks.snow || block == Blocks.ice) {
                            this.setBlockAndNotifyAdequately(world, blockPos.add(n5, n4, n6), Blocks.packed_ice.getDefaultState());
                        }
                        if (n4 != 0 && n > 1 && ((block = world.getBlockState(blockPos.add(n5, -n4, n6)).getBlock()).getMaterial() == Material.air || block == Blocks.dirt || block == Blocks.snow || block == Blocks.ice)) {
                            this.setBlockAndNotifyAdequately(world, blockPos.add(n5, -n4, n6), Blocks.packed_ice.getDefaultState());
                        }
                    }
                    ++n6;
                }
                ++n5;
            }
            ++n4;
        }
        n4 = n3 - 1;
        if (n4 < 0) {
            n4 = 0;
        } else if (n4 > 1) {
            n4 = 1;
        }
        int n7 = -n4;
        while (n7 <= n4) {
            n = -n4;
            while (n <= n4) {
                BlockPos blockPos2 = blockPos.add(n7, -1, n);
                int n8 = 50;
                if (Math.abs(n7) == 1 && Math.abs(n) == 1) {
                    n8 = random.nextInt(5);
                }
                while (blockPos2.getY() > 50) {
                    Block block = world.getBlockState(blockPos2).getBlock();
                    if (block.getMaterial() != Material.air && block != Blocks.dirt && block != Blocks.snow && block != Blocks.ice && block != Blocks.packed_ice) break;
                    this.setBlockAndNotifyAdequately(world, blockPos2, Blocks.packed_ice.getDefaultState());
                    blockPos2 = blockPos2.down();
                    if (--n8 > 0) continue;
                    blockPos2 = blockPos2.down(random.nextInt(5) + 1);
                    n8 = random.nextInt(5);
                }
                ++n;
            }
            ++n7;
        }
        return true;
    }
}

