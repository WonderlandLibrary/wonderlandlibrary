package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public final class AliasEvent extends NodeEvent {
  public AliasEvent(String anchor, Mark startMark, Mark endMark) {
    super(anchor, startMark, endMark);
    if (anchor == null)
      throw new NullPointerException("anchor is not specified for alias"); 
  }
  
  public Event.ID getEventId() {
    return Event.ID.Alias;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\events\AliasEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */