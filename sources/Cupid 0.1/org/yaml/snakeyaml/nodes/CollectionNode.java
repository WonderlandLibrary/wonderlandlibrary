package org.yaml.snakeyaml.nodes;

import java.util.List;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.error.Mark;

public abstract class CollectionNode<T> extends Node {
  private DumperOptions.FlowStyle flowStyle;
  
  public CollectionNode(Tag tag, Mark startMark, Mark endMark, DumperOptions.FlowStyle flowStyle) {
    super(tag, startMark, endMark);
    setFlowStyle(flowStyle);
  }
  
  @Deprecated
  public CollectionNode(Tag tag, Mark startMark, Mark endMark, Boolean flowStyle) {
    this(tag, startMark, endMark, DumperOptions.FlowStyle.fromBoolean(flowStyle));
  }
  
  public abstract List<T> getValue();
  
  public DumperOptions.FlowStyle getFlowStyle() {
    return this.flowStyle;
  }
  
  public void setFlowStyle(DumperOptions.FlowStyle flowStyle) {
    if (flowStyle == null)
      throw new NullPointerException("Flow style must be provided."); 
    this.flowStyle = flowStyle;
  }
  
  @Deprecated
  public void setFlowStyle(Boolean flowStyle) {
    setFlowStyle(DumperOptions.FlowStyle.fromBoolean(flowStyle));
  }
  
  public void setEndMark(Mark endMark) {
    this.endMark = endMark;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\nodes\CollectionNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */