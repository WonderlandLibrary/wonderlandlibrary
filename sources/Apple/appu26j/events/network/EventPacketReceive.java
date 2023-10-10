package appu26j.events.network;

import appu26j.events.Event;
import net.minecraft.network.Packet;

public class EventPacketReceive extends Event
{
	private final Packet packet;
	
	public EventPacketReceive(Packet packet)
	{
		this.packet = packet;
	}
	
	public Packet getPacket()
	{
		return this.packet;
	}
}
