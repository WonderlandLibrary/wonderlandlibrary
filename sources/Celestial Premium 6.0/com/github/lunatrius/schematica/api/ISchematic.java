/*
 * Decompiled with CFR 0.150.
 */
package com.github.lunatrius.schematica.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public interface ISchematic {
    public IBlockState getBlockState(BlockPos var1);

    public int getWidth();

    public int getHeight();

    public int getLength();
}

