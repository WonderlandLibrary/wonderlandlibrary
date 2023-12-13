package me.dev.legacy.event.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class Packet extends Event
{
    private Object packet;
    private Type type;

    public Packet(final Object packet, final Type type) {
        this.packet = packet;
        this.type = type;
    }

    public void setPacket(final Object packet) {
        this.packet = packet;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public Object getPacket() {
        return this.packet;
    }

    public Type getType() {
        return this.type;
    }

    public enum Type
    {
        INCOMING,
        OUTGOING;
    }
}
