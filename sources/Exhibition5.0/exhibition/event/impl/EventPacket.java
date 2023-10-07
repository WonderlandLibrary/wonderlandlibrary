// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.event.impl;

import net.minecraft.network.Packet;
import exhibition.event.Event;

public class EventPacket extends Event
{
    private Packet packet;
    private boolean outgoing;
    
    public void fire(final Packet packet, final boolean outgoing) {
        this.packet = packet;
        this.outgoing = outgoing;
        super.fire();
    }
    
    public Packet getPacket() {
        return this.packet;
    }
    
    public void setPacket(final Packet packet) {
        this.packet = packet;
    }
    
    public boolean isOutgoing() {
        return this.outgoing;
    }
    
    public boolean isIncoming() {
        return !this.outgoing;
    }
}
