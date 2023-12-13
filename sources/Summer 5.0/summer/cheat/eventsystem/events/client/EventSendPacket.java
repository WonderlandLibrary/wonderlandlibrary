package summer.cheat.eventsystem.events.client;

import net.minecraft.network.Packet;
import summer.cheat.eventsystem.Event;

public class EventSendPacket extends Event
{
    private Packet packet;
    
    public EventSendPacket(final Packet packet) {
        this.packet = packet;
    }
    
    public Packet getPacket() {
        return this.packet;
    }
    
    public void setPacket(final Packet packet) {
        this.packet = packet;
    }
}

