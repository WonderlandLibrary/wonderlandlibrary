package org.yaml.snakeyaml.extensions.compactnotation;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;

public class CompactConstructor extends Constructor {
  private static final Pattern GUESS_COMPACT = Pattern.compile("\\p{Alpha}.*\\s*\\((?:,?\\s*(?:(?:\\w*)|(?:\\p{Alpha}\\w*\\s*=.+))\\s*)+\\)");
  
  private static final Pattern FIRST_PATTERN = Pattern.compile("(\\p{Alpha}.*)(\\s*)\\((.*?)\\)");
  
  private static final Pattern PROPERTY_NAME_PATTERN = Pattern.compile("\\s*(\\p{Alpha}\\w*)\\s*=(.+)");
  
  private Construct compactConstruct;
  
  protected Object constructCompactFormat(ScalarNode node, CompactData data) {
    try {
      Object obj = createInstance(node, data);
      Map<String, Object> properties = new HashMap<>(data.getProperties());
      setProperties(obj, properties);
      return obj;
    } catch (Exception e) {
      throw new YAMLException(e);
    } 
  }
  
  protected Object createInstance(ScalarNode node, CompactData data) throws Exception {
    Class<?> clazz = getClassForName(data.getPrefix());
    Class<?>[] args = new Class[data.getArguments().size()];
    for (int i = 0; i < args.length; i++)
      args[i] = String.class; 
    Constructor<?> c = clazz.getDeclaredConstructor(args);
    c.setAccessible(true);
    return c.newInstance(data.getArguments().toArray());
  }
  
  protected void setProperties(Object bean, Map<String, Object> data) throws Exception {
    if (data == null)
      throw new NullPointerException("Data for Compact Object Notation cannot be null."); 
    for (Map.Entry<String, Object> entry : data.entrySet()) {
      String key = entry.getKey();
      Property property = getPropertyUtils().getProperty(bean.getClass(), key);
      try {
        property.set(bean, entry.getValue());
      } catch (IllegalArgumentException e) {
        throw new YAMLException("Cannot set property='" + key + "' with value='" + data.get(key) + "' (" + data.get(key).getClass() + ") in " + bean);
      } 
    } 
  }
  
  public CompactData getCompactData(String scalar) {
    if (!scalar.endsWith(")"))
      return null; 
    if (scalar.indexOf('(') < 0)
      return null; 
    Matcher m = FIRST_PATTERN.matcher(scalar);
    if (m.matches()) {
      String tag = m.group(1).trim();
      String content = m.group(3);
      CompactData data = new CompactData(tag);
      if (content.length() == 0)
        return data; 
      String[] names = content.split("\\s*,\\s*");
      for (int i = 0; i < names.length; i++) {
        String section = names[i];
        if (section.indexOf('=') < 0) {
          data.getArguments().add(section);
        } else {
          Matcher sm = PROPERTY_NAME_PATTERN.matcher(section);
          if (sm.matches()) {
            String name = sm.group(1);
            String value = sm.group(2).trim();
            data.getProperties().put(name, value);
          } else {
            return null;
          } 
        } 
      } 
      return data;
    } 
    return null;
  }
  
  private Construct getCompactConstruct() {
    if (this.compactConstruct == null)
      this.compactConstruct = createCompactConstruct(); 
    return this.compactConstruct;
  }
  
  protected Construct createCompactConstruct() {
    return (Construct)new ConstructCompactObject();
  }
  
  protected Construct getConstructor(Node node) {
    if (node instanceof MappingNode) {
      MappingNode mnode = (MappingNode)node;
      List<NodeTuple> list = mnode.getValue();
      if (list.size() == 1) {
        NodeTuple tuple = list.get(0);
        Node key = tuple.getKeyNode();
        if (key instanceof ScalarNode) {
          ScalarNode scalar = (ScalarNode)key;
          if (GUESS_COMPACT.matcher(scalar.getValue()).matches())
            return getCompactConstruct(); 
        } 
      } 
    } else if (node instanceof ScalarNode) {
      ScalarNode scalar = (ScalarNode)node;
      if (GUESS_COMPACT.matcher(scalar.getValue()).matches())
        return getCompactConstruct(); 
    } 
    return super.getConstructor(node);
  }
  
  public class ConstructCompactObject extends Constructor.ConstructMapping {
    public ConstructCompactObject() {
      super(CompactConstructor.this);
    }
    
    public void construct2ndStep(Node node, Object object) {
      MappingNode mnode = (MappingNode)node;
      NodeTuple nodeTuple = mnode.getValue().iterator().next();
      Node valueNode = nodeTuple.getValueNode();
      if (valueNode instanceof MappingNode) {
        valueNode.setType(object.getClass());
        constructJavaBean2ndStep((MappingNode)valueNode, object);
      } else {
        CompactConstructor.this.applySequence(object, CompactConstructor.this.constructSequence((SequenceNode)valueNode));
      } 
    }
    
    public Object construct(Node node) {
      ScalarNode tmpNode;
      if (node instanceof MappingNode) {
        MappingNode mnode = (MappingNode)node;
        NodeTuple nodeTuple = mnode.getValue().iterator().next();
        node.setTwoStepsConstruction(true);
        tmpNode = (ScalarNode)nodeTuple.getKeyNode();
      } else {
        tmpNode = (ScalarNode)node;
      } 
      CompactData data = CompactConstructor.this.getCompactData(tmpNode.getValue());
      if (data == null)
        return CompactConstructor.this.constructScalar(tmpNode); 
      return CompactConstructor.this.constructCompactFormat(tmpNode, data);
    }
  }
  
  protected void applySequence(Object bean, List<?> value) {
    try {
      Property property = getPropertyUtils().getProperty(bean.getClass(), getSequencePropertyName(bean.getClass()));
      property.set(bean, value);
    } catch (Exception e) {
      throw new YAMLException(e);
    } 
  }
  
  protected String getSequencePropertyName(Class<?> bean) {
    Set<Property> properties = getPropertyUtils().getProperties(bean);
    for (Iterator<Property> iterator = properties.iterator(); iterator.hasNext(); ) {
      Property property = iterator.next();
      if (!List.class.isAssignableFrom(property.getType()))
        iterator.remove(); 
    } 
    if (properties.size() == 0)
      throw new YAMLException("No list property found in " + bean); 
    if (properties.size() > 1)
      throw new YAMLException("Many list properties found in " + bean + "; Please override getSequencePropertyName() to specify which property to use."); 
    return ((Property)properties.iterator().next()).getName();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\extensions\compactnotation\CompactConstructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */