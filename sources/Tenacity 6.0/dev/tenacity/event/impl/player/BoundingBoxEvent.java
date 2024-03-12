// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.player;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import dev.tenacity.event.Event;

public class BoundingBoxEvent extends Event
{
    private final Block block;
    private final BlockPos blockPos;
    private AxisAlignedBB boundingBox;
    
    public BoundingBoxEvent(final Block block, final BlockPos pos, final AxisAlignedBB boundingBox) {
        this.block = block;
        this.blockPos = pos;
        this.boundingBox = boundingBox;
    }
    
    public final Block getBlock() {
        return this.block;
    }
    
    public final BlockPos getBlockPos() {
        return this.blockPos;
    }
    
    public final AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }
    
    public final void setBoundingBox(final AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }
}
