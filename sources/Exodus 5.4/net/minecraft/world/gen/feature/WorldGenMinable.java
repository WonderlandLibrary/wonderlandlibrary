/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.world.gen.feature;

import com.google.common.base.Predicate;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenMinable
extends WorldGenerator {
    private final Predicate<IBlockState> predicate;
    private final int numberOfBlocks;
    private final IBlockState oreBlock;

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        float f = random.nextFloat() * (float)Math.PI;
        double d = (float)(blockPos.getX() + 8) + MathHelper.sin(f) * (float)this.numberOfBlocks / 8.0f;
        double d2 = (float)(blockPos.getX() + 8) - MathHelper.sin(f) * (float)this.numberOfBlocks / 8.0f;
        double d3 = (float)(blockPos.getZ() + 8) + MathHelper.cos(f) * (float)this.numberOfBlocks / 8.0f;
        double d4 = (float)(blockPos.getZ() + 8) - MathHelper.cos(f) * (float)this.numberOfBlocks / 8.0f;
        double d5 = blockPos.getY() + random.nextInt(3) - 2;
        double d6 = blockPos.getY() + random.nextInt(3) - 2;
        int n = 0;
        while (n < this.numberOfBlocks) {
            float f2 = (float)n / (float)this.numberOfBlocks;
            double d7 = d + (d2 - d) * (double)f2;
            double d8 = d5 + (d6 - d5) * (double)f2;
            double d9 = d3 + (d4 - d3) * (double)f2;
            double d10 = random.nextDouble() * (double)this.numberOfBlocks / 16.0;
            double d11 = (double)(MathHelper.sin((float)Math.PI * f2) + 1.0f) * d10 + 1.0;
            double d12 = (double)(MathHelper.sin((float)Math.PI * f2) + 1.0f) * d10 + 1.0;
            int n2 = MathHelper.floor_double(d7 - d11 / 2.0);
            int n3 = MathHelper.floor_double(d8 - d12 / 2.0);
            int n4 = MathHelper.floor_double(d9 - d11 / 2.0);
            int n5 = MathHelper.floor_double(d7 + d11 / 2.0);
            int n6 = MathHelper.floor_double(d8 + d12 / 2.0);
            int n7 = MathHelper.floor_double(d9 + d11 / 2.0);
            int n8 = n2;
            while (n8 <= n5) {
                double d13 = ((double)n8 + 0.5 - d7) / (d11 / 2.0);
                if (d13 * d13 < 1.0) {
                    int n9 = n3;
                    while (n9 <= n6) {
                        double d14 = ((double)n9 + 0.5 - d8) / (d12 / 2.0);
                        if (d13 * d13 + d14 * d14 < 1.0) {
                            int n10 = n4;
                            while (n10 <= n7) {
                                BlockPos blockPos2;
                                double d15 = ((double)n10 + 0.5 - d9) / (d11 / 2.0);
                                if (d13 * d13 + d14 * d14 + d15 * d15 < 1.0 && this.predicate.apply((Object)world.getBlockState(blockPos2 = new BlockPos(n8, n9, n10)))) {
                                    world.setBlockState(blockPos2, this.oreBlock, 2);
                                }
                                ++n10;
                            }
                        }
                        ++n9;
                    }
                }
                ++n8;
            }
            ++n;
        }
        return true;
    }

    public WorldGenMinable(IBlockState iBlockState, int n, Predicate<IBlockState> predicate) {
        this.oreBlock = iBlockState;
        this.numberOfBlocks = n;
        this.predicate = predicate;
    }

    public WorldGenMinable(IBlockState iBlockState, int n) {
        this(iBlockState, n, BlockHelper.forBlock(Blocks.stone));
    }
}

