package com.enjoytheban.api.events.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.enjoytheban.api.Event;

import net.minecraft.network.Packet;

public class EventPacketSend extends Event {

	/**
	 * @author Purity
	 * @desc An event for handling packet related things
	 * @called NetHandlerPlayClient addToSendQueue
	 */
	
	//variable that holds the packet
	private Packet packet;
	
	//conducktor
	public EventPacketSend(Packet packet) {
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