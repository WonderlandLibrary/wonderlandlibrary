package com.initial.events.impl;

import com.initial.events.*;
import net.minecraft.network.*;

public class EventReceivePacket extends Event
{
    private Packet packet;
    
    public EventReceivePacket(final Packet packet) {
        this.packet = packet;
    }
    
    public Packet getPacket() {
        return this.packet;
    }
    
    public void setPacket(final Packet packet) {
        this.packet = packet;
    }
}
