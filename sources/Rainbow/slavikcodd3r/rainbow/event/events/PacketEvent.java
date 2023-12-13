package slavikcodd3r.rainbow.event.events;

import java.net.Socket;
import net.minecraft.network.Packet;
import slavikcodd3r.rainbow.event.Event;

public class PacketEvent extends Event
{
    private static Packet packet;
    private Socket parentSocket;
    
    public PacketEvent(final Packet packet, final Socket parentSocket) {
        this.packet = packet;
        this.parentSocket = parentSocket;
    }
    
    public Socket getParentSocket() {
        return this.parentSocket;
    }
    
    public Packet getPacket() {
        return packet;
    }
    
    public void setPacket(final Packet packet) {
        this.packet = packet;
    }
}
