// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import vip.Resolute.events.EventDirection;
import net.minecraft.network.Packet;
import vip.Resolute.events.Event;

public class EventPacket extends Event<EventPacket>
{
    public static Packet packet;
    
    public EventPacket(final Packet packet, final EventDirection direction) {
        EventPacket.packet = packet;
        this.direction = direction;
    }
    
    public <T extends Packet> T getPacket() {
        return (T)EventPacket.packet;
    }
    
    public void setPacket(final Packet packet) {
        EventPacket.packet = packet;
    }
}
