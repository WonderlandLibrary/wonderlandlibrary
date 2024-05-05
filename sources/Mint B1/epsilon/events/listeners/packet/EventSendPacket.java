package epsilon.events.listeners.packet;

import epsilon.events.Event;
import net.minecraft.network.Packet;

public class EventSendPacket extends Event<EventSendPacket> {
    public Packet packet;

    public EventSendPacket(Packet packet) {
        this.packet = null;
        setPacket(packet);
    }

    public Packet getPacket() {
        return packet;
    }
    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
