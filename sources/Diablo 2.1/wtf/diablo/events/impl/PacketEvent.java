package wtf.diablo.events.impl;

import net.minecraft.network.Packet;
import wtf.diablo.events.Event;
import wtf.diablo.events.EventType;

public class PacketEvent extends Event {
    private Packet packet;

    public PacketEvent(Packet packet){
        this.packet = packet;
    }

    public Packet getPacket(){
        return packet;
    }
    public void setPacket(Packet p){
        this.packet = p;
    }
}
