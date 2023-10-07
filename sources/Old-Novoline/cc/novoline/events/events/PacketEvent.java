package cc.novoline.events.events;

import cc.novoline.events.events.callables.CancellableEvent;
import net.minecraft.network.Packet;

public class PacketEvent extends CancellableEvent {

    private final State state;
    private final Packet<?> packet;

    public PacketEvent(Packet<?> packet, State state) {
        this.state = state;
        this.packet = packet;
    }

    public State getState() {
        return state;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public enum State {
        INCOMING,
        OUTGOING
    }
}
