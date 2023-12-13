// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.event.impl;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import exhibition.event.Event;

public class EventBlockBounds extends Event
{
    private Block block;
    private BlockPos pos;
    private AxisAlignedBB bounds;
    
    public void fire(final Block block, final BlockPos pos, final AxisAlignedBB bounds) {
        this.block = block;
        this.pos = pos;
        this.bounds = bounds;
        super.fire();
    }
    
    public AxisAlignedBB getBounds() {
        return this.bounds;
    }
    
    public void setBounds(final AxisAlignedBB bounds) {
        this.bounds = bounds;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public Block getBlock() {
        return this.block;
    }
}
