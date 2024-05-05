package space.lunaclient.luna.impl.events;

import space.lunaclient.luna.api.event.Event;
import space.lunaclient.luna.api.event.Event.Type;

public class EventKeyBoard
  extends Event
{
  private int key;
  
  public EventKeyBoard(int key)
  {
    super(Event.Type.SINGLE);
    this.key = key;
  }
  
  public int getKey()
  {
    return this.key;
  }
}
