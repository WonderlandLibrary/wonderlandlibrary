package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public final class MappingEndEvent extends CollectionEndEvent {
  public MappingEndEvent(Mark startMark, Mark endMark) {
    super(startMark, endMark);
  }
  
  public Event.ID getEventId() {
    return Event.ID.MappingEnd;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\events\MappingEndEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */