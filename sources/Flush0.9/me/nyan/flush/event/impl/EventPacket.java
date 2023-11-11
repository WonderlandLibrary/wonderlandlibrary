package me.nyan.flush.event.impl;

import me.nyan.flush.event.Event;
import net.minecraft.network.Packet;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EventPacket extends Event {
    private Packet packet;
    private final Direction direction;

    public EventPacket(Packet packet, Direction direction) {
        this.packet = packet;
        this.direction = direction;
    }

    public <T extends Packet> T getPacket() {
        return (T) packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public boolean isIncoming() {
        return direction == Direction.INCOMING;
    }

    public boolean isOutgoing() {
        return direction == Direction.OUTGOING;
    }

    public enum Direction {
        INCOMING,
        OUTGOING
    }
}