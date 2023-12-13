// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event.events;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import com.elementars.eclient.event.Event;

public class EventPlayerDamageBlock extends Event
{
    BlockPos pos;
    EnumFacing facing;
    
    public EventPlayerDamageBlock(final BlockPos pos, final EnumFacing facing) {
        this.pos = pos;
        this.facing = facing;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public EnumFacing getFacing() {
        return this.facing;
    }
}
