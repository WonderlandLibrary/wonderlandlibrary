package space.lunaclient.luna.impl.events;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import space.lunaclient.luna.api.event.Event;
import space.lunaclient.luna.api.event.Event.Type;

public class EventPacketSend
  extends Event
{
  private Packet<INetHandler> packet;
  
  public EventPacketSend(Packet<INetHandler> packetIn)
  {
    super(Event.Type.SINGLE);
    this.packet = packetIn;
  }
  
  public Packet<INetHandler> getPacket()
  {
    return this.packet;
  }
}
