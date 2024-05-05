package space.lunaclient.luna.impl.events;

import space.lunaclient.luna.api.event.Event;
import space.lunaclient.luna.api.event.Event.Type;

public class EventSlowDown
  extends Event
{
  public EventSlowDown()
  {
    super(Event.Type.SINGLE);
  }
}
