package de.theBest.MythicX.events;

import eventapi.events.Event;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;


public class EventPacket implements Event{

    public static Packet packet;

    private boolean cancel;

    private Action eventAction;

    private INetHandler netHandler;

    private EnumPacketDirection packetDirection;

    public enum Action {
        SEND, RECEIVE;
    }
    public EventPacket(Action action, Packet packet, INetHandler netHandler, EnumPacketDirection direction) {
        this.eventAction = action;
        EventPacket.packet = packet;
        this.netHandler = netHandler;
        this.packetDirection = direction;
    }

    public static Packet getPacket() {
        return packet;
    }

    public static void setPacket(Packet packet) {
        EventPacket.packet = packet;
    }

    public boolean isCanceled() {
        return cancel;
    }

    public void setCanceled(boolean cancel) {
        this.cancel = cancel;
    }

    public Action getEventAction() {
        return eventAction;
    }

    public void setEventAction(Action eventAction) {
        this.eventAction = eventAction;
    }

    public INetHandler getNetHandler() {
        return netHandler;
    }

    public void setNetHandler(INetHandler netHandler) {
        this.netHandler = netHandler;
    }

    public EnumPacketDirection getPacketDirection() {
        return packetDirection;
    }

    public void setPacketDirection(EnumPacketDirection packetDirection) {
        this.packetDirection = packetDirection;
    }


}
