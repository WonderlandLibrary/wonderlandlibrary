package org.yaml.snakeyaml.nodes;

public class AnchorNode extends Node {
  private Node realNode;
  
  public AnchorNode(Node realNode) {
    super(realNode.getTag(), realNode.getStartMark(), realNode.getEndMark());
    this.realNode = realNode;
  }
  
  public NodeId getNodeId() {
    return NodeId.anchor;
  }
  
  public Node getRealNode() {
    return this.realNode;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\nodes\AnchorNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */