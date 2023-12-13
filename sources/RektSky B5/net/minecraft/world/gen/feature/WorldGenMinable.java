/*
 * Decompiled with CFR 0.152.
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
    private final IBlockState oreBlock;
    private final int numberOfBlocks;
    private final Predicate<IBlockState> predicate;

    public WorldGenMinable(IBlockState state, int blockCount) {
        this(state, blockCount, BlockHelper.forBlock(Blocks.stone));
    }

    public WorldGenMinable(IBlockState state, int blockCount, Predicate<IBlockState> p_i45631_3_) {
        this.oreBlock = state;
        this.numberOfBlocks = blockCount;
        this.predicate = p_i45631_3_;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        float f2 = rand.nextFloat() * (float)Math.PI;
        double d0 = (float)(position.getX() + 8) + MathHelper.sin(f2) * (float)this.numberOfBlocks / 8.0f;
        double d1 = (float)(position.getX() + 8) - MathHelper.sin(f2) * (float)this.numberOfBlocks / 8.0f;
        double d2 = (float)(position.getZ() + 8) + MathHelper.cos(f2) * (float)this.numberOfBlocks / 8.0f;
        double d3 = (float)(position.getZ() + 8) - MathHelper.cos(f2) * (float)this.numberOfBlocks / 8.0f;
        double d4 = position.getY() + rand.nextInt(3) - 2;
        double d5 = position.getY() + rand.nextInt(3) - 2;
        for (int i2 = 0; i2 < this.numberOfBlocks; ++i2) {
            float f1 = (float)i2 / (float)this.numberOfBlocks;
            double d6 = d0 + (d1 - d0) * (double)f1;
            double d7 = d4 + (d5 - d4) * (double)f1;
            double d8 = d2 + (d3 - d2) * (double)f1;
            double d9 = rand.nextDouble() * (double)this.numberOfBlocks / 16.0;
            double d10 = (double)(MathHelper.sin((float)Math.PI * f1) + 1.0f) * d9 + 1.0;
            double d11 = (double)(MathHelper.sin((float)Math.PI * f1) + 1.0f) * d9 + 1.0;
            int j2 = MathHelper.floor_double(d6 - d10 / 2.0);
            int k2 = MathHelper.floor_double(d7 - d11 / 2.0);
            int l2 = MathHelper.floor_double(d8 - d10 / 2.0);
            int i1 = MathHelper.floor_double(d6 + d10 / 2.0);
            int j1 = MathHelper.floor_double(d7 + d11 / 2.0);
            int k1 = MathHelper.floor_double(d8 + d10 / 2.0);
            for (int l1 = j2; l1 <= i1; ++l1) {
                double d12 = ((double)l1 + 0.5 - d6) / (d10 / 2.0);
                if (!(d12 * d12 < 1.0)) continue;
                for (int i22 = k2; i22 <= j1; ++i22) {
                    double d13 = ((double)i22 + 0.5 - d7) / (d11 / 2.0);
                    if (!(d12 * d12 + d13 * d13 < 1.0)) continue;
                    for (int j22 = l2; j22 <= k1; ++j22) {
                        BlockPos blockpos;
                        double d14 = ((double)j22 + 0.5 - d8) / (d10 / 2.0);
                        if (!(d12 * d12 + d13 * d13 + d14 * d14 < 1.0) || !this.predicate.apply(worldIn.getBlockState(blockpos = new BlockPos(l1, i22, j22)))) continue;
                        worldIn.setBlockState(blockpos, this.oreBlock, 2);
                    }
                }
            }
        }
        return true;
    }
}

