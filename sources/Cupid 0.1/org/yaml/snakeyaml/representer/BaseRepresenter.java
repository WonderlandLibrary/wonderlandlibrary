package org.yaml.snakeyaml.representer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.AnchorNode;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;

public abstract class BaseRepresenter {
  protected final Map<Class<?>, Represent> representers = new HashMap<>();
  
  protected Represent nullRepresenter;
  
  protected final Map<Class<?>, Represent> multiRepresenters = new LinkedHashMap<>();
  
  protected DumperOptions.ScalarStyle defaultScalarStyle = null;
  
  protected DumperOptions.FlowStyle defaultFlowStyle = DumperOptions.FlowStyle.AUTO;
  
  protected final Map<Object, Node> representedObjects = new IdentityHashMap<Object, Node>() {
      private static final long serialVersionUID = -5576159264232131854L;
      
      public Node put(Object key, Node value) {
        return (Node)super.put(key, new AnchorNode(value));
      }
    };
  
  protected Object objectToRepresent;
  
  private PropertyUtils propertyUtils;
  
  private boolean explicitPropertyUtils = false;
  
  public Node represent(Object data) {
    Node node = representData(data);
    this.representedObjects.clear();
    this.objectToRepresent = null;
    return node;
  }
  
  protected final Node representData(Object data) {
    Node node;
    this.objectToRepresent = data;
    if (this.representedObjects.containsKey(this.objectToRepresent)) {
      node = this.representedObjects.get(this.objectToRepresent);
      return node;
    } 
    if (data == null) {
      node = this.nullRepresenter.representData(null);
      return node;
    } 
    Class<?> clazz = data.getClass();
    if (this.representers.containsKey(clazz)) {
      Represent representer = this.representers.get(clazz);
      node = representer.representData(data);
    } else {
      for (Class<?> repr : this.multiRepresenters.keySet()) {
        if (repr != null && repr.isInstance(data)) {
          Represent representer = this.multiRepresenters.get(repr);
          node = representer.representData(data);
          return node;
        } 
      } 
      if (this.multiRepresenters.containsKey(null)) {
        Represent representer = this.multiRepresenters.get(null);
        node = representer.representData(data);
      } else {
        Represent representer = this.representers.get(null);
        node = representer.representData(data);
      } 
    } 
    return node;
  }
  
  protected Node representScalar(Tag tag, String value, DumperOptions.ScalarStyle style) {
    if (style == null)
      style = this.defaultScalarStyle; 
    return (Node)new ScalarNode(tag, value, null, null, style);
  }
  
  protected Node representScalar(Tag tag, String value) {
    return representScalar(tag, value, null);
  }
  
  protected Node representSequence(Tag tag, Iterable<?> sequence, DumperOptions.FlowStyle flowStyle) {
    int size = 10;
    if (sequence instanceof List)
      size = ((List)sequence).size(); 
    List<Node> value = new ArrayList<>(size);
    SequenceNode node = new SequenceNode(tag, value, flowStyle);
    this.representedObjects.put(this.objectToRepresent, node);
    DumperOptions.FlowStyle bestStyle = DumperOptions.FlowStyle.FLOW;
    for (Object item : sequence) {
      Node nodeItem = representData(item);
      if (!(nodeItem instanceof ScalarNode) || !((ScalarNode)nodeItem).isPlain())
        bestStyle = DumperOptions.FlowStyle.BLOCK; 
      value.add(nodeItem);
    } 
    if (flowStyle == DumperOptions.FlowStyle.AUTO)
      if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
        node.setFlowStyle(this.defaultFlowStyle);
      } else {
        node.setFlowStyle(bestStyle);
      }  
    return (Node)node;
  }
  
  protected Node representMapping(Tag tag, Map<?, ?> mapping, DumperOptions.FlowStyle flowStyle) {
    List<NodeTuple> value = new ArrayList<>(mapping.size());
    MappingNode node = new MappingNode(tag, value, flowStyle);
    this.representedObjects.put(this.objectToRepresent, node);
    DumperOptions.FlowStyle bestStyle = DumperOptions.FlowStyle.FLOW;
    for (Map.Entry<?, ?> entry : mapping.entrySet()) {
      Node nodeKey = representData(entry.getKey());
      Node nodeValue = representData(entry.getValue());
      if (!(nodeKey instanceof ScalarNode) || !((ScalarNode)nodeKey).isPlain())
        bestStyle = DumperOptions.FlowStyle.BLOCK; 
      if (!(nodeValue instanceof ScalarNode) || !((ScalarNode)nodeValue).isPlain())
        bestStyle = DumperOptions.FlowStyle.BLOCK; 
      value.add(new NodeTuple(nodeKey, nodeValue));
    } 
    if (flowStyle == DumperOptions.FlowStyle.AUTO)
      if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
        node.setFlowStyle(this.defaultFlowStyle);
      } else {
        node.setFlowStyle(bestStyle);
      }  
    return (Node)node;
  }
  
  public void setDefaultScalarStyle(DumperOptions.ScalarStyle defaultStyle) {
    this.defaultScalarStyle = defaultStyle;
  }
  
  public DumperOptions.ScalarStyle getDefaultScalarStyle() {
    if (this.defaultScalarStyle == null)
      return DumperOptions.ScalarStyle.PLAIN; 
    return this.defaultScalarStyle;
  }
  
  public void setDefaultFlowStyle(DumperOptions.FlowStyle defaultFlowStyle) {
    this.defaultFlowStyle = defaultFlowStyle;
  }
  
  public DumperOptions.FlowStyle getDefaultFlowStyle() {
    return this.defaultFlowStyle;
  }
  
  public void setPropertyUtils(PropertyUtils propertyUtils) {
    this.propertyUtils = propertyUtils;
    this.explicitPropertyUtils = true;
  }
  
  public final PropertyUtils getPropertyUtils() {
    if (this.propertyUtils == null)
      this.propertyUtils = new PropertyUtils(); 
    return this.propertyUtils;
  }
  
  public final boolean isExplicitPropertyUtils() {
    return this.explicitPropertyUtils;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\representer\BaseRepresenter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */