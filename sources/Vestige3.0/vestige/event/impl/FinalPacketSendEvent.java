package vestige.event.impl;

import lombok.AllArgsConstructor;
import lombok.Setter;
import net.minecraft.network.Packet;
import vestige.event.type.CancellableEvent;

@AllArgsConstructor
public class FinalPacketSendEvent extends CancellableEvent {

    @Setter
    private Packet packet;

    public <T extends Packet> T getPacket() {
        return (T) packet;
    }

}