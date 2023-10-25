package org.yaml.snakeyaml.events;

import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.error.Mark;

public final class DocumentStartEvent extends Event {
  private final boolean explicit;
  
  private final DumperOptions.Version version;
  
  private final Map<String, String> tags;
  
  public DocumentStartEvent(Mark startMark, Mark endMark, boolean explicit, DumperOptions.Version version, Map<String, String> tags) {
    super(startMark, endMark);
    this.explicit = explicit;
    this.version = version;
    this.tags = tags;
  }
  
  public boolean getExplicit() {
    return this.explicit;
  }
  
  public DumperOptions.Version getVersion() {
    return this.version;
  }
  
  public Map<String, String> getTags() {
    return this.tags;
  }
  
  public Event.ID getEventId() {
    return Event.ID.DocumentStart;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\events\DocumentStartEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */