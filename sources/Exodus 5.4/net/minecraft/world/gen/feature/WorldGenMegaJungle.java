/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenHugeTrees;

public class WorldGenMegaJungle
extends WorldGenHugeTrees {
    private void func_175930_c(World world, BlockPos blockPos, int n) {
        int n2 = 2;
        int n3 = -n2;
        while (n3 <= 0) {
            this.func_175925_a(world, blockPos.up(n3), n + 1 - n3);
            ++n3;
        }
    }

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        int n = this.func_150533_a(random);
        if (!this.func_175929_a(world, random, blockPos, n)) {
            return false;
        }
        this.func_175930_c(world, blockPos.up(n), 2);
        int n2 = blockPos.getY() + n - 2 - random.nextInt(4);
        while (n2 > blockPos.getY() + n / 2) {
            float f = random.nextFloat() * (float)Math.PI * 2.0f;
            int n3 = blockPos.getX() + (int)(0.5f + MathHelper.cos(f) * 4.0f);
            int n4 = blockPos.getZ() + (int)(0.5f + MathHelper.sin(f) * 4.0f);
            int n5 = 0;
            while (n5 < 5) {
                n3 = blockPos.getX() + (int)(1.5f + MathHelper.cos(f) * (float)n5);
                n4 = blockPos.getZ() + (int)(1.5f + MathHelper.sin(f) * (float)n5);
                this.setBlockAndNotifyAdequately(world, new BlockPos(n3, n2 - 3 + n5 / 2, n4), this.woodMetadata);
                ++n5;
            }
            n5 = 1 + random.nextInt(2);
            int n6 = n2;
            int n7 = n2 - n5;
            while (n7 <= n6) {
                int n8 = n7 - n6;
                this.func_175928_b(world, new BlockPos(n3, n7, n4), 1 - n8);
                ++n7;
            }
            n2 -= 2 + random.nextInt(4);
        }
        n2 = 0;
        while (n2 < n) {
            BlockPos blockPos2 = blockPos.up(n2);
            if (this.func_150523_a(world.getBlockState(blockPos2).getBlock())) {
                this.setBlockAndNotifyAdequately(world, blockPos2, this.woodMetadata);
                if (n2 > 0) {
                    this.func_181632_a(world, random, blockPos2.west(), BlockVine.EAST);
                    this.func_181632_a(world, random, blockPos2.north(), BlockVine.SOUTH);
                }
            }
            if (n2 < n - 1) {
                BlockPos blockPos3;
                BlockPos blockPos4;
                BlockPos blockPos5 = blockPos2.east();
                if (this.func_150523_a(world.getBlockState(blockPos5).getBlock())) {
                    this.setBlockAndNotifyAdequately(world, blockPos5, this.woodMetadata);
                    if (n2 > 0) {
                        this.func_181632_a(world, random, blockPos5.east(), BlockVine.WEST);
                        this.func_181632_a(world, random, blockPos5.north(), BlockVine.SOUTH);
                    }
                }
                if (this.func_150523_a(world.getBlockState(blockPos4 = blockPos2.south().east()).getBlock())) {
                    this.setBlockAndNotifyAdequately(world, blockPos4, this.woodMetadata);
                    if (n2 > 0) {
                        this.func_181632_a(world, random, blockPos4.east(), BlockVine.WEST);
                        this.func_181632_a(world, random, blockPos4.south(), BlockVine.NORTH);
                    }
                }
                if (this.func_150523_a(world.getBlockState(blockPos3 = blockPos2.south()).getBlock())) {
                    this.setBlockAndNotifyAdequately(world, blockPos3, this.woodMetadata);
                    if (n2 > 0) {
                        this.func_181632_a(world, random, blockPos3.west(), BlockVine.EAST);
                        this.func_181632_a(world, random, blockPos3.south(), BlockVine.NORTH);
                    }
                }
            }
            ++n2;
        }
        return true;
    }

    public WorldGenMegaJungle(boolean bl, int n, int n2, IBlockState iBlockState, IBlockState iBlockState2) {
        super(bl, n, n2, iBlockState, iBlockState2);
    }

    private void func_181632_a(World world, Random random, BlockPos blockPos, PropertyBool propertyBool) {
        if (random.nextInt(3) > 0 && world.isAirBlock(blockPos)) {
            this.setBlockAndNotifyAdequately(world, blockPos, Blocks.vine.getDefaultState().withProperty(propertyBool, true));
        }
    }
}

