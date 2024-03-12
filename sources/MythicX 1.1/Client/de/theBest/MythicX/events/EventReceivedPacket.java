package de.theBest.MythicX.events;


import eventapi.events.Event;
import net.minecraft.network.Packet;



import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.INetHandler;

public class EventReceivedPacket implements Event {
    public static Packet packet;
    private boolean cancel;
    private Action eventAction;
    private INetHandler netHandler;
    private EnumPacketDirection packetDirection;

    public EventReceivedPacket(Action action, Packet packet, INetHandler netHandler, EnumPacketDirection direction) {
        this.eventAction = action;
        EventReceivedPacket.packet = packet;
        this.netHandler = netHandler;
        this.packetDirection = direction;
    }

    public INetHandler getNetHandler() {
        return netHandler;
    }

    public void setNetHandler(INetHandler netHandler) {
        this.netHandler = netHandler;
    }

    public EnumPacketDirection getDirection() {
        return packetDirection;
    }

    public static Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        EventReceivedPacket.packet = packet;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Action getEventAction() {
        return eventAction;
    }

    public boolean isSend() {
        return getEventAction() == Action.SEND;
    }

    public boolean isReceive() {
        return getEventAction() == Action.RECEIVE;
    }

    public enum Action {
        SEND, RECEIVE
    }
}