// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

public class EventRenderChunk extends Event
{
    private final BlockPos blockPos;
    private final IBlockState iBlockState;
    
    public EventRenderChunk(final BlockPos blockPos, final IBlockState iBlockState) {
        this.blockPos = blockPos;
        this.iBlockState = iBlockState;
    }
    
    public BlockPos getBlockPos() {
        return this.blockPos;
    }
    
    public IBlockState getiBlockState() {
        return this.iBlockState;
    }
}
