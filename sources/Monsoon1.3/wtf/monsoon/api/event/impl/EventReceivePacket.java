package wtf.monsoon.api.event.impl;

import wtf.monsoon.api.event.Event;
import net.minecraft.network.Packet;

public class EventReceivePacket extends Event {
	
	public static Packet packet;
    public boolean cancelled;
    
    public EventReceivePacket(Packet packet) {
        this.packet = packet;
    }

    public static Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

}
