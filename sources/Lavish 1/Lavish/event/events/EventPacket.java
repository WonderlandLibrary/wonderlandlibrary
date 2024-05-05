// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.event.events;

import net.minecraft.network.Packet;
import Lavish.event.Event;

public class EventPacket extends Event<EventPacket>
{
    public Packet packet;
    private boolean incoming;
    
    public EventPacket(final Packet packet, final boolean incoming) {
        this.packet = packet;
        this.incoming = incoming;
    }
    
    public boolean isSending() {
        return !this.incoming;
    }
    
    public boolean isRecieving() {
        return this.incoming;
    }
    
    public Packet getPacket() {
        return this.packet;
    }
    
    public void setPacket(final Packet packet) {
        this.packet = packet;
    }
}
