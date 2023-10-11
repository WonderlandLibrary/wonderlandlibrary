/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.network.Packet;

public class EventPacketReceive
extends EventCancellable {
    private boolean cancel;
    public Packet packet;
    private String string;

    public EventPacketReceive(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean state) {
        this.cancel = state;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}

