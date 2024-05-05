package xyz.cucumber.base.events.ext;

import net.minecraft.network.Packet;
import xyz.cucumber.base.events.Event;

public class EventSendPacket extends Event {
	
	private Packet packet;

	public Packet getPacket() {
		return packet;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}

	public EventSendPacket(Packet packet) {
		this.packet = packet;
	}
	
}
