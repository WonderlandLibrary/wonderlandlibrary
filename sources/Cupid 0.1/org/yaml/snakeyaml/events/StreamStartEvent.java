package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public final class StreamStartEvent extends Event {
  public StreamStartEvent(Mark startMark, Mark endMark) {
    super(startMark, endMark);
  }
  
  public Event.ID getEventId() {
    return Event.ID.StreamStart;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\events\StreamStartEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */