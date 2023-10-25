package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public abstract class NodeEvent extends Event {
  private final String anchor;
  
  public NodeEvent(String anchor, Mark startMark, Mark endMark) {
    super(startMark, endMark);
    this.anchor = anchor;
  }
  
  public String getAnchor() {
    return this.anchor;
  }
  
  protected String getArguments() {
    return "anchor=" + this.anchor;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\events\NodeEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */