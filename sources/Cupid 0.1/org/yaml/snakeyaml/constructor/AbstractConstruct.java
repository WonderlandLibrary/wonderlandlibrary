package org.yaml.snakeyaml.constructor;

import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;

public abstract class AbstractConstruct implements Construct {
  public void construct2ndStep(Node node, Object data) {
    if (node.isTwoStepsConstruction())
      throw new IllegalStateException("Not Implemented in " + getClass().getName()); 
    throw new YAMLException("Unexpected recursive structure for Node: " + node);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\constructor\AbstractConstruct.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */