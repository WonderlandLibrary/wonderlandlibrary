package com.initial.events.impl;

import com.initial.events.*;
import net.minecraft.network.*;

public class EventSendPacket extends Event
{
    public Packet packet;
    
    public EventSendPacket(final Packet packet) {
        this.packet = null;
        this.setPacket(packet);
    }
    
    public Packet getPacket() {
        return this.packet;
    }
    
    public void setPacket(final Packet packet) {
        this.packet = packet;
    }
}
