// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import vip.Resolute.events.Event;

public class EventBoundingBox extends Event<EventBoundingBox>
{
    private BlockPos blockPos;
    private AxisAlignedBB bounds;
    
    public EventBoundingBox(final BlockPos blockPos, final AxisAlignedBB bounds) {
        this.blockPos = blockPos;
        this.bounds = bounds;
    }
    
    public BlockPos blockPos() {
        return this.blockPos;
    }
    
    public void blockPos(final BlockPos blockPos) {
        this.blockPos = blockPos;
    }
    
    public AxisAlignedBB bounds() {
        return this.bounds;
    }
    
    public void bounds(final AxisAlignedBB bounds) {
        this.bounds = bounds;
    }
}
