package vestige.api.event.impl;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;
import vestige.api.event.types.CancellableEvent;

@Getter
@Setter
public class PacketReceiveEvent extends CancellableEvent {

    private Packet packet;

    public PacketReceiveEvent(Packet p) {
        this.packet = p;
    }

}
