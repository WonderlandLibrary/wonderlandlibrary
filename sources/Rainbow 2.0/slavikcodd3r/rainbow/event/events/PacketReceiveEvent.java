// 
// Decompiled by Procyon v0.5.30
// 

package slavikcodd3r.rainbow.event.events;

import net.minecraft.network.Packet;
import slavikcodd3r.rainbow.event.Event;

public class PacketReceiveEvent extends Event
{
    private Packet packet;
    
    public Packet getPacket() {
        return this.packet;
    }
    
    public void setPacket(final Packet packet) {
        this.packet = packet;
    }
    
    public PacketReceiveEvent(final Packet packet) {
        this.packet = packet;
    }
}
