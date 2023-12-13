// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockLiquid;
import vip.Resolute.events.Event;

public class EventLiquidBB extends Event<EventLiquidBB>
{
    BlockLiquid blockLiquid;
    BlockPos pos;
    AxisAlignedBB axisAlignedBB;
    
    public EventLiquidBB(final BlockLiquid blockLiquid, final BlockPos pos, final AxisAlignedBB axisAlignedBB) {
        this.blockLiquid = blockLiquid;
        this.pos = pos;
        this.axisAlignedBB = axisAlignedBB;
    }
    
    public AxisAlignedBB getAxisAlignedBB() {
        return this.axisAlignedBB;
    }
    
    public void setAxisAlignedBB(final AxisAlignedBB axisAlignedBB) {
        this.axisAlignedBB = axisAlignedBB;
    }
    
    public BlockLiquid getBlock() {
        return this.blockLiquid;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
}
