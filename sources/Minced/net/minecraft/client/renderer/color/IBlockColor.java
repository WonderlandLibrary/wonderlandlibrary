// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.color;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;

public interface IBlockColor
{
    int colorMultiplier(final IBlockState p0, final IBlockAccess p1, final BlockPos p2, final int p3);
}
