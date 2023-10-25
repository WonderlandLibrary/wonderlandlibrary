package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public final class DocumentEndEvent extends Event {
  private final boolean explicit;
  
  public DocumentEndEvent(Mark startMark, Mark endMark, boolean explicit) {
    super(startMark, endMark);
    this.explicit = explicit;
  }
  
  public boolean getExplicit() {
    return this.explicit;
  }
  
  public Event.ID getEventId() {
    return Event.ID.DocumentEnd;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\events\DocumentEndEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */