package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.error.Mark;

public abstract class CollectionStartEvent extends NodeEvent {
  private final String tag;
  
  private final boolean implicit;
  
  private final DumperOptions.FlowStyle flowStyle;
  
  public CollectionStartEvent(String anchor, String tag, boolean implicit, Mark startMark, Mark endMark, DumperOptions.FlowStyle flowStyle) {
    super(anchor, startMark, endMark);
    this.tag = tag;
    this.implicit = implicit;
    if (flowStyle == null)
      throw new NullPointerException("Flow style must be provided."); 
    this.flowStyle = flowStyle;
  }
  
  @Deprecated
  public CollectionStartEvent(String anchor, String tag, boolean implicit, Mark startMark, Mark endMark, Boolean flowStyle) {
    this(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.fromBoolean(flowStyle));
  }
  
  public String getTag() {
    return this.tag;
  }
  
  public boolean getImplicit() {
    return this.implicit;
  }
  
  public DumperOptions.FlowStyle getFlowStyle() {
    return this.flowStyle;
  }
  
  protected String getArguments() {
    return super.getArguments() + ", tag=" + this.tag + ", implicit=" + this.implicit;
  }
  
  public boolean isFlow() {
    return (DumperOptions.FlowStyle.FLOW == this.flowStyle);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\events\CollectionStartEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */