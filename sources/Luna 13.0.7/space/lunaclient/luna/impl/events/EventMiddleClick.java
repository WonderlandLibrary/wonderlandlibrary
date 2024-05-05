package space.lunaclient.luna.impl.events;

import space.lunaclient.luna.api.event.Event;
import space.lunaclient.luna.api.event.Event.Type;

public class EventMiddleClick
  extends Event
{
  public EventMiddleClick()
  {
    super(Event.Type.SINGLE);
  }
}
