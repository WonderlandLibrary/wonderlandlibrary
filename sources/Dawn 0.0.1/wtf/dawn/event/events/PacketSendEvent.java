package wtf.dawn.event.events;

import net.minecraft.network.Packet;
import wtf.dawn.event.Event;

public class PacketSendEvent extends Event<Event> {

    private Packet<?> packet;
    public PacketSendEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return this.packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }
}
