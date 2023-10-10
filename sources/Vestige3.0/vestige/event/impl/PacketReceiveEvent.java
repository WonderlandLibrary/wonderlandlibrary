package vestige.event.impl;

import lombok.AllArgsConstructor;
import net.minecraft.network.Packet;
import vestige.event.type.CancellableEvent;

@AllArgsConstructor
public class PacketReceiveEvent extends CancellableEvent {

    private Packet packet;

    public <T extends Packet> T getPacket() {
        return (T) packet;
    }

}