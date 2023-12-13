// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import net.minecraft.util.BlockPos;
import vip.Resolute.events.Event;

public class EventBlockDamaged extends Event<EventBlockDamaged>
{
    private final BlockPos blockPos;
    
    public EventBlockDamaged(final BlockPos blockPos) {
        this.blockPos = blockPos;
    }
    
    public BlockPos getBlockPos() {
        return this.blockPos;
    }
}
