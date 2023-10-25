package events;

import events.Event;
import net.minecraft.network.Packet;

public abstract class EventCancellable
extends Event {
    private Packet packet;
    private boolean cancelled;

    protected EventCancellable() {
    }

    public Packet getPacket() {
        return this.packet;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean state) {
        this.cancelled = state;
    }
}
