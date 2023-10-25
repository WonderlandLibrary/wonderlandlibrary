package club.cloverhook.event.minecraft;

import club.cloverhook.event.CancellableEvent;
import net.minecraft.network.Packet;

/**
 * @author antja03
 */
public class ProcessPacketEvent extends CancellableEvent {
    private Packet packet;

    public ProcessPacketEvent(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
