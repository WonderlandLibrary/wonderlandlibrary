package org.yaml.snakeyaml.representer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;

public class Representer extends SafeRepresenter {
  protected Map<Class<? extends Object>, TypeDescription> typeDefinitions = Collections.emptyMap();
  
  public Representer() {
    this.representers.put(null, new RepresentJavaBean());
  }
  
  public Representer(DumperOptions options) {
    super(options);
    this.representers.put(null, new RepresentJavaBean());
  }
  
  public TypeDescription addTypeDescription(TypeDescription td) {
    if (Collections.EMPTY_MAP == this.typeDefinitions)
      this.typeDefinitions = new HashMap<>(); 
    if (td.getTag() != null)
      addClassTag(td.getType(), td.getTag()); 
    td.setPropertyUtils(getPropertyUtils());
    return this.typeDefinitions.put(td.getType(), td);
  }
  
  public void setPropertyUtils(PropertyUtils propertyUtils) {
    super.setPropertyUtils(propertyUtils);
    Collection<TypeDescription> tds = this.typeDefinitions.values();
    for (TypeDescription typeDescription : tds)
      typeDescription.setPropertyUtils(propertyUtils); 
  }
  
  protected class RepresentJavaBean implements Represent {
    public Node representData(Object data) {
      return (Node)Representer.this.representJavaBean(Representer.this.getProperties((Class)data.getClass()), data);
    }
  }
  
  protected MappingNode representJavaBean(Set<Property> properties, Object javaBean) {
    List<NodeTuple> value = new ArrayList<>(properties.size());
    Tag customTag = this.classTags.get(javaBean.getClass());
    Tag tag = (customTag != null) ? customTag : new Tag(javaBean.getClass());
    MappingNode node = new MappingNode(tag, value, DumperOptions.FlowStyle.AUTO);
    this.representedObjects.put(javaBean, node);
    DumperOptions.FlowStyle bestStyle = DumperOptions.FlowStyle.FLOW;
    for (Property property : properties) {
      Object memberValue = property.get(javaBean);
      Tag customPropertyTag = (memberValue == null) ? null : this.classTags.get(memberValue.getClass());
      NodeTuple tuple = representJavaBeanProperty(javaBean, property, memberValue, customPropertyTag);
      if (tuple == null)
        continue; 
      if (!((ScalarNode)tuple.getKeyNode()).isPlain())
        bestStyle = DumperOptions.FlowStyle.BLOCK; 
      Node nodeValue = tuple.getValueNode();
      if (!(nodeValue instanceof ScalarNode) || !((ScalarNode)nodeValue).isPlain())
        bestStyle = DumperOptions.FlowStyle.BLOCK; 
      value.add(tuple);
    } 
    if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
      node.setFlowStyle(this.defaultFlowStyle);
    } else {
      node.setFlowStyle(bestStyle);
    } 
    return node;
  }
  
  protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag) {
    ScalarNode nodeKey = (ScalarNode)representData(property.getName());
    boolean hasAlias = this.representedObjects.containsKey(propertyValue);
    Node nodeValue = representData(propertyValue);
    if (propertyValue != null && !hasAlias) {
      NodeId nodeId = nodeValue.getNodeId();
      if (customTag == null)
        if (nodeId == NodeId.scalar) {
          if (property.getType() != Enum.class && 
            propertyValue instanceof Enum)
            nodeValue.setTag(Tag.STR); 
        } else {
          if (nodeId == NodeId.mapping && 
            property.getType() == propertyValue.getClass() && 
            !(propertyValue instanceof Map) && 
            !nodeValue.getTag().equals(Tag.SET))
            nodeValue.setTag(Tag.MAP); 
          checkGlobalTag(property, nodeValue, propertyValue);
        }  
    } 
    return new NodeTuple((Node)nodeKey, nodeValue);
  }
  
  protected void checkGlobalTag(Property property, Node node, Object object) {
    if (object.getClass().isArray() && object.getClass().getComponentType().isPrimitive())
      return; 
    Class<?>[] arguments = property.getActualTypeArguments();
    if (arguments != null)
      if (node.getNodeId() == NodeId.sequence) {
        Class<? extends Object> t = (Class)arguments[0];
        SequenceNode snode = (SequenceNode)node;
        Iterable<Object> memberList = Collections.EMPTY_LIST;
        if (object.getClass().isArray()) {
          memberList = Arrays.asList((Object[])object);
        } else if (object instanceof Iterable) {
          memberList = (Iterable<Object>)object;
        } 
        Iterator<Object> iter = memberList.iterator();
        if (iter.hasNext())
          for (Node childNode : snode.getValue()) {
            Object member = iter.next();
            if (member != null && 
              t.equals(member.getClass()) && 
              childNode.getNodeId() == NodeId.mapping)
              childNode.setTag(Tag.MAP); 
          }  
      } else if (object instanceof Set) {
        Class<?> t = arguments[0];
        MappingNode mnode = (MappingNode)node;
        Iterator<NodeTuple> iter = mnode.getValue().iterator();
        Set<?> set = (Set)object;
        for (Object member : set) {
          NodeTuple tuple = iter.next();
          Node keyNode = tuple.getKeyNode();
          if (t.equals(member.getClass()) && 
            keyNode.getNodeId() == NodeId.mapping)
            keyNode.setTag(Tag.MAP); 
        } 
      } else if (object instanceof Map) {
        Class<?> keyType = arguments[0];
        Class<?> valueType = arguments[1];
        MappingNode mnode = (MappingNode)node;
        for (NodeTuple tuple : mnode.getValue()) {
          resetTag((Class)keyType, tuple.getKeyNode());
          resetTag((Class)valueType, tuple.getValueNode());
        } 
      }  
  }
  
  private void resetTag(Class<? extends Object> type, Node node) {
    Tag tag = node.getTag();
    if (tag.matches(type))
      if (Enum.class.isAssignableFrom(type)) {
        node.setTag(Tag.STR);
      } else {
        node.setTag(Tag.MAP);
      }  
  }
  
  protected Set<Property> getProperties(Class<? extends Object> type) {
    if (this.typeDefinitions.containsKey(type))
      return ((TypeDescription)this.typeDefinitions.get(type)).getProperties(); 
    return getPropertyUtils().getProperties(type);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\representer\Representer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */