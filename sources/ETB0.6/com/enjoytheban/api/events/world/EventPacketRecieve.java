package com.enjoytheban.api.events.world;

import com.enjoytheban.api.Event;

import net.minecraft.network.Packet;

public class EventPacketRecieve extends Event {

	/**
	 * @author Purity
	 * @desc An event for handling packet related things
	 * @called NetworkManager channelRead0
	 */

	//variable that holds the packet
	private Packet packet;

	//conducktor
	public EventPacketRecieve(Packet packet) {
		this.packet = packet;
	}

	//gets the packet
	public Packet getPacket() {
		return packet;
	}

	//sets the packet
	public void setPacket(Packet packet) {
		this.packet = packet;
	}
}