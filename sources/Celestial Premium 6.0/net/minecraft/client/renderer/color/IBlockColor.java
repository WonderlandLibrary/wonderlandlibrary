/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.color;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IBlockColor {
    public int colorMultiplier(IBlockState var1, IBlockAccess var2, BlockPos var3, int var4);
}

