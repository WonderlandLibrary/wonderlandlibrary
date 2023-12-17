// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events.impl;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.Packet;
import ru.tuskevich.event.events.callables.EventCancellable;

public class EventSendPacket extends EventCancellable
{
    private final Packet<?> packet;
    
    public EventSendPacket(final Packet packet) {
        this.packet = (Packet<?>)packet;
    }
    
    public Packet getPacket(final CPacketPlayer cPacketPlayer) {
        return this.packet;
    }
    
    public void isCanceled(final boolean b) {
    }
}
