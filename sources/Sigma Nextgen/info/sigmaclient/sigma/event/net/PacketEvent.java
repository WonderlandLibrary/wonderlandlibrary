package info.sigmaclient.sigma.event.net;

import info.sigmaclient.sigma.event.Event;
import lombok.Getter;
import net.minecraft.network.IPacket;

public class PacketEvent extends Event {
    @Getter
    boolean isSend;
    public boolean isRevive(){
        return !isSend;
    }
    public PacketEvent setRev(){
        isSend = false;
        return this;
    }
    public PacketEvent setSend(){
        isSend = true;
        return this;
    }
    @Getter
    public IPacket<?> packet;
    public PacketEvent(IPacket<?> packet){
        this.eventID = 5;
        this.packet = packet;
    }
}
