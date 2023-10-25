/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block.state;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlockBehaviors {
    public boolean onBlockEventReceived(World var1, BlockPos var2, int var3, int var4);

    public void neighborChanged(World var1, BlockPos var2, Block var3, BlockPos var4);
}

