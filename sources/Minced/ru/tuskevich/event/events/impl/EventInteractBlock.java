// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events.impl;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import ru.tuskevich.event.events.Event;
import ru.tuskevich.event.events.callables.EventCancellable;

public class EventInteractBlock extends EventCancellable implements Event
{
    private BlockPos pos;
    private EnumFacing face;
    
    public EventInteractBlock(final BlockPos pos, final EnumFacing face) {
        this.pos = pos;
        this.face = face;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public void setPos(final BlockPos pos) {
        this.pos = pos;
    }
    
    public EnumFacing getFace() {
        return this.face;
    }
    
    public void setFace(final EnumFacing face) {
        this.face = face;
    }
}
