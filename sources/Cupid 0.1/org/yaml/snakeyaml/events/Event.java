package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public abstract class Event {
  private final Mark startMark;
  
  private final Mark endMark;
  
  public enum ID {
    Alias, DocumentEnd, DocumentStart, MappingEnd, MappingStart, Scalar, SequenceEnd, SequenceStart, StreamEnd, StreamStart;
  }
  
  public Event(Mark startMark, Mark endMark) {
    this.startMark = startMark;
    this.endMark = endMark;
  }
  
  public String toString() {
    return "<" + getClass().getName() + "(" + getArguments() + ")>";
  }
  
  public Mark getStartMark() {
    return this.startMark;
  }
  
  public Mark getEndMark() {
    return this.endMark;
  }
  
  protected String getArguments() {
    return "";
  }
  
  public boolean is(ID id) {
    return (getEventId() == id);
  }
  
  public abstract ID getEventId();
  
  public boolean equals(Object obj) {
    if (obj instanceof Event)
      return toString().equals(obj.toString()); 
    return false;
  }
  
  public int hashCode() {
    return toString().hashCode();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\events\Event.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */