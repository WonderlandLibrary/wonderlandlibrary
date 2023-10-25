package club.cloverhook.event.minecraft;

import club.cloverhook.event.CancellableEvent;
import net.minecraft.network.Packet;

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
