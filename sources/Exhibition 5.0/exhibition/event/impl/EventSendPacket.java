// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.event.impl;

import net.minecraft.network.Packet;
import exhibition.event.Event;

public class EventSendPacket extends Event
{
    private Packet packet;
    private boolean pre;
    
    public void fire(final boolean state, final Packet packet) {
        this.pre = state;
        this.packet = packet;
        super.fire();
    }
    
    public Packet getPacket() {
        return this.packet;
    }
    
    public boolean isPre() {
        return this.pre;
    }
    
    public void setPacket(final Packet packet) {
        this.packet = packet;
    }
    
    public void setState(final boolean state) {
        this.pre = state;
    }
}
