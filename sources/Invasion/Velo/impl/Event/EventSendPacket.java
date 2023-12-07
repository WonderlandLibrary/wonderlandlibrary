package Velo.impl.Event;

import Velo.api.Event.Event;
import net.minecraft.network.Packet;

public class EventSendPacket extends Event<EventSendPacket> {
	
	public Packet packet;
	
	public EventSendPacket(Packet packet) {
		this.packet = packet;
	}
	
	public Packet getPacket() {
		return packet;
	}
	
	public void setPacket(Packet packet) {
		this.packet = packet;
	}
	
}
