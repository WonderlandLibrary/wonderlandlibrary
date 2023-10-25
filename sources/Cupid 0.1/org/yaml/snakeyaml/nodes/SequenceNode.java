package org.yaml.snakeyaml.nodes;

import java.util.List;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.error.Mark;

public class SequenceNode extends CollectionNode<Node> {
  private final List<Node> value;
  
  public SequenceNode(Tag tag, boolean resolved, List<Node> value, Mark startMark, Mark endMark, DumperOptions.FlowStyle flowStyle) {
    super(tag, startMark, endMark, flowStyle);
    if (value == null)
      throw new NullPointerException("value in a Node is required."); 
    this.value = value;
    this.resolved = resolved;
  }
  
  public SequenceNode(Tag tag, List<Node> value, DumperOptions.FlowStyle flowStyle) {
    this(tag, true, value, (Mark)null, (Mark)null, flowStyle);
  }
  
  @Deprecated
  public SequenceNode(Tag tag, List<Node> value, Boolean style) {
    this(tag, value, DumperOptions.FlowStyle.fromBoolean(style));
  }
  
  @Deprecated
  public SequenceNode(Tag tag, boolean resolved, List<Node> value, Mark startMark, Mark endMark, Boolean style) {
    this(tag, resolved, value, startMark, endMark, DumperOptions.FlowStyle.fromBoolean(style));
  }
  
  public NodeId getNodeId() {
    return NodeId.sequence;
  }
  
  public List<Node> getValue() {
    return this.value;
  }
  
  public void setListType(Class<? extends Object> listType) {
    for (Node node : this.value)
      node.setType(listType); 
  }
  
  public String toString() {
    return "<" + getClass().getName() + " (tag=" + getTag() + ", value=" + getValue() + ")>";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\nodes\SequenceNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */