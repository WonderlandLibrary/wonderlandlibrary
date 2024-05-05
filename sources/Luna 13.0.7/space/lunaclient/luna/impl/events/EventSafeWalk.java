package space.lunaclient.luna.impl.events;

import space.lunaclient.luna.api.event.Event;
import space.lunaclient.luna.api.event.Event.Type;

public class EventSafeWalk
  extends Event
{
  public EventSafeWalk()
  {
    super(Event.Type.SINGLE);
  }
}
