package org.yaml.snakeyaml.nodes;

public final class NodeTuple {
  private Node keyNode;
  
  private Node valueNode;
  
  public NodeTuple(Node keyNode, Node valueNode) {
    if (keyNode == null || valueNode == null)
      throw new NullPointerException("Nodes must be provided."); 
    this.keyNode = keyNode;
    this.valueNode = valueNode;
  }
  
  public Node getKeyNode() {
    return this.keyNode;
  }
  
  public Node getValueNode() {
    return this.valueNode;
  }
  
  public String toString() {
    return "<NodeTuple keyNode=" + this.keyNode.toString() + "; valueNode=" + this.valueNode.toString() + ">";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\nodes\NodeTuple.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */