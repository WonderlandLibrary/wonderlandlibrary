package io.github.nevalackin.client.event.packet;

import io.github.nevalackin.client.event.CancellableEvent;
import net.minecraft.network.Packet;

class PacketEvent extends CancellableEvent {

    private Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }
}
