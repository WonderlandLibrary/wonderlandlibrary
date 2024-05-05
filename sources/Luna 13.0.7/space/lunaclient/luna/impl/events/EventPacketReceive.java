package space.lunaclient.luna.impl.events;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import space.lunaclient.luna.api.event.Event;
import space.lunaclient.luna.api.event.Event.Type;

public class EventPacketReceive
  extends Event
{
  private Packet<INetHandler> packet;
  
  public EventPacketReceive(Packet<INetHandler> packet)
  {
    super(Event.Type.SINGLE);
    this.packet = packet;
  }
  
  public Packet<INetHandler> getPacket()
  {
    return this.packet;
  }
}
