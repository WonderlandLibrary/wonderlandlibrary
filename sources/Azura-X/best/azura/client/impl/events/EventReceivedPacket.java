package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;
import net.minecraft.network.Packet;

public class EventReceivedPacket implements NamedEvent {
	private Packet<?> packet;
	private boolean cancelled;
	
	public EventReceivedPacket(Packet<?> packet) {
		this.packet = packet;
	}
	
	public <T extends Packet<?>> T getPacket() {
		return (T) packet;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setPacket(Packet<?> packet) {
		this.packet = packet;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public String name() {
		return "receivedPacket";
	}
}