// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events.impl;

import net.minecraft.network.Packet;
import ru.tuskevich.event.events.Event;
import ru.tuskevich.event.events.callables.EventCancellable;

public class EventPacket extends EventCancellable implements Event
{
    private Packet packet;
    
    public EventPacket(final Packet packet) {
        this.packet = packet;
    }
    
    public Packet getPacket() {
        return this.packet;
    }
}
