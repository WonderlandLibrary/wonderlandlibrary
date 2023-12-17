// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.state;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlockBehaviors
{
    boolean onBlockEventReceived(final World p0, final BlockPos p1, final int p2, final int p3);
    
    void neighborChanged(final World p0, final BlockPos p1, final Block p2, final BlockPos p3);
}
