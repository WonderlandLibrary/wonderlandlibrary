// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.network;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import net.minecraft.network.Packet;
import dev.tenacity.event.Event;

public class PacketEvent extends Event
{
    private Packet<?> packet;
    
    public PacketEvent(final Packet<?> packet) {
        this.packet = packet;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public Packet<?> getPacket() {
        return this.packet;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void setPacket(final Packet<?> packet) {
        this.packet = packet;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public int getPacketID() {
        return this.getPacket().getID();
    }
}
