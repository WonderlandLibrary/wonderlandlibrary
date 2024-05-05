package space.lunaclient.luna.impl.events;

import space.lunaclient.luna.api.event.Event;
import space.lunaclient.luna.api.event.Event.Type;

public class EventRender2D
  extends Event
{
  private int width;
  private int height;
  
  public EventRender2D(int width, int height)
  {
    super(Event.Type.SINGLE);
    this.width = width;
    this.height = height;
  }
  
  public int getHeight()
  {
    return this.height;
  }
  
  public int getWidth()
  {
    return this.width;
  }
}
