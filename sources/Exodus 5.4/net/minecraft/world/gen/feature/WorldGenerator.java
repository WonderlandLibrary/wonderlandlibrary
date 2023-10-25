/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class WorldGenerator {
    private final boolean doBlockNotify;

    public WorldGenerator(boolean bl) {
        this.doBlockNotify = bl;
    }

    public WorldGenerator() {
        this(false);
    }

    public void func_175904_e() {
    }

    protected void setBlockAndNotifyAdequately(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (this.doBlockNotify) {
            world.setBlockState(blockPos, iBlockState, 3);
        } else {
            world.setBlockState(blockPos, iBlockState, 2);
        }
    }

    public abstract boolean generate(World var1, Random var2, BlockPos var3);
}

