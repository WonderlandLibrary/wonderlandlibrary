// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.augustus.utils.interfaces.MC;

public class EdgeCosts implements MC
{
    public static boolean hasBlockCollision(final BlockPos blockPos) {
        final IBlockState iBlockState = EdgeCosts.mc.theWorld.getBlockState(blockPos);
        final Block block = iBlockState.getBlock();
        return block.getCollisionBoundingBox(EdgeCosts.mc.theWorld, blockPos, iBlockState) != null;
    }
}
