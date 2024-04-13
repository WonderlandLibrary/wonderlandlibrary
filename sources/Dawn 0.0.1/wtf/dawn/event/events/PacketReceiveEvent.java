package wtf.dawn.event.events;

import net.minecraft.network.Packet;
import wtf.dawn.event.Event;

public class PacketReceiveEvent extends Event<Event> {

    private Packet<?> packet;
    public PacketReceiveEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return this.packet;
    }

    public void setPacket(Packet<?> packet) {
     this.packet = packet;
    }
}
