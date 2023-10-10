package ravenweave.client.event.impl;

import net.minecraft.network.Packet;
import net.weavemc.loader.api.event.CancellableEvent;
import ravenweave.client.event.ext.EventDirection;
import ravenweave.client.event.ext.IEventDirection;

public class PacketEvent extends CancellableEvent implements IEventDirection {

    private Packet<?> packet;
    private final EventDirection direction;

    public PacketEvent(Packet<?> packet, EventDirection direction) {
        this.packet = packet;
        this.direction = direction;
    }

    public <T extends Packet<?>> T getPacket() {
        return (T) this.packet;
    }

    public <T extends Packet<?>> void setPacket(T newPacket) {
        this.packet = newPacket;
    }

    @Override
    public EventDirection getDirection() {
        return direction;
    }

}