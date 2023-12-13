package vestige.api.event.impl;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;
import vestige.api.event.types.CancellableEvent;

@Getter
@Setter
public class PacketSendEvent extends CancellableEvent {

    private Packet packet;

    public PacketSendEvent(Packet p) {
        this.packet = p;
    }

}
