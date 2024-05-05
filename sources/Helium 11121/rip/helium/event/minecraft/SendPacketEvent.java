package rip.helium.event.minecraft;

import net.minecraft.network.Packet;
import rip.helium.event.CancellableEvent;

/**
 * @author antja03
 */
public class SendPacketEvent extends CancellableEvent {
    private Packet packet;

    public SendPacketEvent(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
