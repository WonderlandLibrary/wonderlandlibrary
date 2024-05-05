package space.lunaclient.luna.impl.events;

import space.lunaclient.luna.api.event.Event;
import space.lunaclient.luna.api.event.Event.Type;

public class EventUpdate
  extends Event
{
  public EventUpdate()
  {
    super(Event.Type.SINGLE);
  }
}
