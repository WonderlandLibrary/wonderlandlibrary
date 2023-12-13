// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.events;

import net.minecraft.network.Packet;

public class EventSendPacket extends Event
{
    private Packet<?> packet;
    
    public EventSendPacket(final Packet<?> packet) {
        this.packet = packet;
    }
    
    public Packet<?> getPacket() {
        return this.packet;
    }
    
    public void setPacket(final Packet<?> packet) {
        this.packet = packet;
    }
}
