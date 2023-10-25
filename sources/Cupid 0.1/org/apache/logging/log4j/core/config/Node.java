package org.apache.logging.log4j.core.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;

public class Node {
  public static final String CATEGORY = "Core";
  
  private Node parent;
  
  private final String name;
  
  private String value;
  
  private final PluginType<?> type;
  
  private final Map<String, String> attributes = new HashMap<>();
  
  private final List<Node> children = new ArrayList<>();
  
  private Object object;
  
  public Node(Node parent, String name, PluginType<?> type) {
    this.parent = parent;
    this.name = name;
    this.type = type;
  }
  
  public Node() {
    this.parent = null;
    this.name = null;
    this.type = null;
  }
  
  public Node(Node node) {
    this.parent = node.parent;
    this.name = node.name;
    this.type = node.type;
    this.attributes.putAll(node.getAttributes());
    this.value = node.getValue();
    for (Node child : node.getChildren())
      this.children.add(new Node(child)); 
    this.object = node.object;
  }
  
  public void setParent(Node parent) {
    this.parent = parent;
  }
  
  public Map<String, String> getAttributes() {
    return this.attributes;
  }
  
  public List<Node> getChildren() {
    return this.children;
  }
  
  public boolean hasChildren() {
    return !this.children.isEmpty();
  }
  
  public String getValue() {
    return this.value;
  }
  
  public void setValue(String value) {
    this.value = value;
  }
  
  public Node getParent() {
    return this.parent;
  }
  
  public String getName() {
    return this.name;
  }
  
  public boolean isRoot() {
    return (this.parent == null);
  }
  
  public void setObject(Object obj) {
    this.object = obj;
  }
  
  public <T> T getObject() {
    return (T)this.object;
  }
  
  public <T> T getObject(Class<T> clazz) {
    return clazz.cast(this.object);
  }
  
  public boolean isInstanceOf(Class<?> clazz) {
    return clazz.isInstance(this.object);
  }
  
  public PluginType<?> getType() {
    return this.type;
  }
  
  public String toString() {
    if (this.object == null)
      return "null"; 
    return this.type.isObjectPrintable() ? this.object.toString() : (this.type
      .getPluginClass().getName() + " with name " + this.name);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\Node.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */