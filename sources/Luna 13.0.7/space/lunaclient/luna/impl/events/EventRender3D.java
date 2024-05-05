package space.lunaclient.luna.impl.events;

import space.lunaclient.luna.api.event.Event;
import space.lunaclient.luna.api.event.Event.Type;

public class EventRender3D
  extends Event
{
  private float partialTicks;
  
  public EventRender3D(float partialTicks)
  {
    super(Event.Type.SINGLE);
    this.partialTicks = partialTicks;
  }
  
  public float getPartialTicks()
  {
    return this.partialTicks;
  }
}
