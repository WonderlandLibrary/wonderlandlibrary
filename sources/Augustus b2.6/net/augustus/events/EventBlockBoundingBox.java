// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.events;

import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;

public class EventBlockBoundingBox extends Event
{
    private AxisAlignedBB axisAlignedBB;
    private Block block;
    private BlockPos blockPos;
    
    public EventBlockBoundingBox(final AxisAlignedBB axisAlignedBB, final Block block, final BlockPos blockPos) {
        this.axisAlignedBB = axisAlignedBB;
        this.block = block;
        this.blockPos = blockPos;
    }
    
    public AxisAlignedBB getAxisAlignedBB() {
        return this.axisAlignedBB;
    }
    
    public void setAxisAlignedBB(final AxisAlignedBB axisAlignedBB) {
        this.axisAlignedBB = axisAlignedBB;
    }
    
    public Block getBlock() {
        return this.block;
    }
    
    public void setBlock(final Block block) {
        this.block = block;
    }
    
    public BlockPos getBlockPos() {
        return this.blockPos;
    }
    
    public void setBlockPos(final BlockPos blockPos) {
        this.blockPos = blockPos;
    }
}
