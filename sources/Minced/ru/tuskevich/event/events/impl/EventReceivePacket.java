// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events.impl;

import net.minecraft.network.Packet;
import ru.tuskevich.event.events.callables.EventCancellable;

public class EventReceivePacket extends EventCancellable
{
    private Packet<?> packet;
    
    public EventReceivePacket(final Packet<?> packet) {
        this.packet = packet;
    }
    
    public Packet<?> getPacket() {
        return this.packet;
    }
    
    public void setPacket(final Packet<?> packet) {
        this.packet = packet;
    }
}
