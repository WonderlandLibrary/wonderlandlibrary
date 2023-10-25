package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.error.Mark;

public final class ScalarEvent extends NodeEvent {
  private final String tag;
  
  private final DumperOptions.ScalarStyle style;
  
  private final String value;
  
  private final ImplicitTuple implicit;
  
  public ScalarEvent(String anchor, String tag, ImplicitTuple implicit, String value, Mark startMark, Mark endMark, DumperOptions.ScalarStyle style) {
    super(anchor, startMark, endMark);
    this.tag = tag;
    this.implicit = implicit;
    if (value == null)
      throw new NullPointerException("Value must be provided."); 
    this.value = value;
    if (style == null)
      throw new NullPointerException("Style must be provided."); 
    this.style = style;
  }
  
  @Deprecated
  public ScalarEvent(String anchor, String tag, ImplicitTuple implicit, String value, Mark startMark, Mark endMark, Character style) {
    this(anchor, tag, implicit, value, startMark, endMark, DumperOptions.ScalarStyle.createStyle(style));
  }
  
  public String getTag() {
    return this.tag;
  }
  
  public DumperOptions.ScalarStyle getScalarStyle() {
    return this.style;
  }
  
  @Deprecated
  public Character getStyle() {
    return this.style.getChar();
  }
  
  public String getValue() {
    return this.value;
  }
  
  public ImplicitTuple getImplicit() {
    return this.implicit;
  }
  
  protected String getArguments() {
    return super.getArguments() + ", tag=" + this.tag + ", " + this.implicit + ", value=" + this.value;
  }
  
  public Event.ID getEventId() {
    return Event.ID.Scalar;
  }
  
  public boolean isPlain() {
    return (this.style == DumperOptions.ScalarStyle.PLAIN);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\events\ScalarEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */