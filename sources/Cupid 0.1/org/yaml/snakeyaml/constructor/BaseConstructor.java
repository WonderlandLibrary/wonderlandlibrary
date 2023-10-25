package org.yaml.snakeyaml.constructor;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.CollectionNode;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;

public abstract class BaseConstructor {
  protected final Map<NodeId, Construct> yamlClassConstructors = new EnumMap<>(NodeId.class);
  
  protected final Map<Tag, Construct> yamlConstructors = new HashMap<>();
  
  protected final Map<String, Construct> yamlMultiConstructors = new HashMap<>();
  
  protected Composer composer;
  
  final Map<Node, Object> constructedObjects;
  
  private final Set<Node> recursiveObjects;
  
  private final ArrayList<RecursiveTuple<Map<Object, Object>, RecursiveTuple<Object, Object>>> maps2fill;
  
  private final ArrayList<RecursiveTuple<Set<Object>, Object>> sets2fill;
  
  protected Tag rootTag;
  
  private PropertyUtils propertyUtils;
  
  private boolean explicitPropertyUtils;
  
  private boolean allowDuplicateKeys = true;
  
  private boolean wrappedToRootException = false;
  
  protected final Map<Class<? extends Object>, TypeDescription> typeDefinitions;
  
  protected final Map<Tag, Class<? extends Object>> typeTags;
  
  protected LoaderOptions loadingConfig;
  
  public BaseConstructor() {
    this(new LoaderOptions());
  }
  
  public BaseConstructor(LoaderOptions loadingConfig) {
    this.constructedObjects = new HashMap<>();
    this.recursiveObjects = new HashSet<>();
    this.maps2fill = new ArrayList<>();
    this.sets2fill = new ArrayList<>();
    this.typeDefinitions = new HashMap<>();
    this.typeTags = new HashMap<>();
    this.rootTag = null;
    this.explicitPropertyUtils = false;
    this.typeDefinitions.put(SortedMap.class, new TypeDescription(SortedMap.class, Tag.OMAP, TreeMap.class));
    this.typeDefinitions.put(SortedSet.class, new TypeDescription(SortedSet.class, Tag.SET, TreeSet.class));
    this.loadingConfig = loadingConfig;
  }
  
  public void setComposer(Composer composer) {
    this.composer = composer;
  }
  
  public boolean checkData() {
    return this.composer.checkNode();
  }
  
  public Object getData() throws NoSuchElementException {
    if (!this.composer.checkNode())
      throw new NoSuchElementException("No document is available."); 
    Node node = this.composer.getNode();
    if (this.rootTag != null)
      node.setTag(this.rootTag); 
    return constructDocument(node);
  }
  
  public Object getSingleData(Class<?> type) {
    Node node = this.composer.getSingleNode();
    if (node != null && !Tag.NULL.equals(node.getTag())) {
      if (Object.class != type) {
        node.setTag(new Tag(type));
      } else if (this.rootTag != null) {
        node.setTag(this.rootTag);
      } 
      return constructDocument(node);
    } 
    Construct construct = this.yamlConstructors.get(Tag.NULL);
    return construct.construct(node);
  }
  
  protected final Object constructDocument(Node node) {
    try {
      Object data = constructObject(node);
      fillRecursive();
      return data;
    } catch (RuntimeException e) {
      if (this.wrappedToRootException && !(e instanceof YAMLException))
        throw new YAMLException(e); 
      throw e;
    } finally {
      this.constructedObjects.clear();
      this.recursiveObjects.clear();
    } 
  }
  
  private void fillRecursive() {
    if (!this.maps2fill.isEmpty()) {
      for (RecursiveTuple<Map<Object, Object>, RecursiveTuple<Object, Object>> entry : this.maps2fill) {
        RecursiveTuple<Object, Object> key_value = entry._2();
        ((Map)entry._1()).put(key_value._1(), key_value._2());
      } 
      this.maps2fill.clear();
    } 
    if (!this.sets2fill.isEmpty()) {
      for (RecursiveTuple<Set<Object>, Object> value : this.sets2fill)
        ((Set)value._1()).add(value._2()); 
      this.sets2fill.clear();
    } 
  }
  
  protected Object constructObject(Node node) {
    if (this.constructedObjects.containsKey(node))
      return this.constructedObjects.get(node); 
    return constructObjectNoCheck(node);
  }
  
  protected Object constructObjectNoCheck(Node node) {
    if (this.recursiveObjects.contains(node))
      throw new ConstructorException(null, null, "found unconstructable recursive node", node.getStartMark()); 
    this.recursiveObjects.add(node);
    Construct constructor = getConstructor(node);
    Object data = this.constructedObjects.containsKey(node) ? this.constructedObjects.get(node) : constructor.construct(node);
    finalizeConstruction(node, data);
    this.constructedObjects.put(node, data);
    this.recursiveObjects.remove(node);
    if (node.isTwoStepsConstruction())
      constructor.construct2ndStep(node, data); 
    return data;
  }
  
  protected Construct getConstructor(Node node) {
    if (node.useClassConstructor())
      return this.yamlClassConstructors.get(node.getNodeId()); 
    Construct constructor = this.yamlConstructors.get(node.getTag());
    if (constructor == null) {
      for (String prefix : this.yamlMultiConstructors.keySet()) {
        if (node.getTag().startsWith(prefix))
          return this.yamlMultiConstructors.get(prefix); 
      } 
      return this.yamlConstructors.get(null);
    } 
    return constructor;
  }
  
  protected String constructScalar(ScalarNode node) {
    return node.getValue();
  }
  
  protected List<Object> createDefaultList(int initSize) {
    return new ArrayList(initSize);
  }
  
  protected Set<Object> createDefaultSet(int initSize) {
    return new LinkedHashSet(initSize);
  }
  
  protected Map<Object, Object> createDefaultMap(int initSize) {
    return new LinkedHashMap<>(initSize);
  }
  
  protected Object createArray(Class<?> type, int size) {
    return Array.newInstance(type.getComponentType(), size);
  }
  
  protected Object finalizeConstruction(Node node, Object data) {
    Class<? extends Object> type = node.getType();
    if (this.typeDefinitions.containsKey(type))
      return ((TypeDescription)this.typeDefinitions.get(type)).finalizeConstruction(data); 
    return data;
  }
  
  protected Object newInstance(Node node) {
    try {
      return newInstance(Object.class, node);
    } catch (InstantiationException e) {
      throw new YAMLException(e);
    } 
  }
  
  protected final Object newInstance(Class<?> ancestor, Node node) throws InstantiationException {
    return newInstance(ancestor, node, true);
  }
  
  protected Object newInstance(Class<?> ancestor, Node node, boolean tryDefault) throws InstantiationException {
    Class<? extends Object> type = node.getType();
    if (this.typeDefinitions.containsKey(type)) {
      TypeDescription td = this.typeDefinitions.get(type);
      Object instance = td.newInstance(node);
      if (instance != null)
        return instance; 
    } 
    if (tryDefault)
      if (ancestor.isAssignableFrom(type) && !Modifier.isAbstract(type.getModifiers()))
        try {
          Constructor<?> c = type.getDeclaredConstructor(new Class[0]);
          c.setAccessible(true);
          return c.newInstance(new Object[0]);
        } catch (NoSuchMethodException e) {
          throw new InstantiationException("NoSuchMethodException:" + e.getLocalizedMessage());
        } catch (Exception e) {
          throw new YAMLException(e);
        }   
    throw new InstantiationException();
  }
  
  protected Set<Object> newSet(CollectionNode<?> node) {
    try {
      return (Set<Object>)newInstance(Set.class, (Node)node);
    } catch (InstantiationException e) {
      return createDefaultSet(node.getValue().size());
    } 
  }
  
  protected List<Object> newList(SequenceNode node) {
    try {
      return (List<Object>)newInstance(List.class, (Node)node);
    } catch (InstantiationException e) {
      return createDefaultList(node.getValue().size());
    } 
  }
  
  protected Map<Object, Object> newMap(MappingNode node) {
    try {
      return (Map<Object, Object>)newInstance(Map.class, (Node)node);
    } catch (InstantiationException e) {
      return createDefaultMap(node.getValue().size());
    } 
  }
  
  protected List<? extends Object> constructSequence(SequenceNode node) {
    List<Object> result = newList(node);
    constructSequenceStep2(node, result);
    return result;
  }
  
  protected Set<? extends Object> constructSet(SequenceNode node) {
    Set<Object> result = newSet((CollectionNode<?>)node);
    constructSequenceStep2(node, result);
    return result;
  }
  
  protected Object constructArray(SequenceNode node) {
    return constructArrayStep2(node, createArray(node.getType(), node.getValue().size()));
  }
  
  protected void constructSequenceStep2(SequenceNode node, Collection<Object> collection) {
    for (Node child : node.getValue())
      collection.add(constructObject(child)); 
  }
  
  protected Object constructArrayStep2(SequenceNode node, Object array) {
    Class<?> componentType = node.getType().getComponentType();
    int index = 0;
    for (Node child : node.getValue()) {
      if (child.getType() == Object.class)
        child.setType(componentType); 
      Object value = constructObject(child);
      if (componentType.isPrimitive()) {
        if (value == null)
          throw new NullPointerException("Unable to construct element value for " + child); 
        if (byte.class.equals(componentType)) {
          Array.setByte(array, index, ((Number)value).byteValue());
        } else if (short.class.equals(componentType)) {
          Array.setShort(array, index, ((Number)value).shortValue());
        } else if (int.class.equals(componentType)) {
          Array.setInt(array, index, ((Number)value).intValue());
        } else if (long.class.equals(componentType)) {
          Array.setLong(array, index, ((Number)value).longValue());
        } else if (float.class.equals(componentType)) {
          Array.setFloat(array, index, ((Number)value).floatValue());
        } else if (double.class.equals(componentType)) {
          Array.setDouble(array, index, ((Number)value).doubleValue());
        } else if (char.class.equals(componentType)) {
          Array.setChar(array, index, ((Character)value).charValue());
        } else if (boolean.class.equals(componentType)) {
          Array.setBoolean(array, index, ((Boolean)value).booleanValue());
        } else {
          throw new YAMLException("unexpected primitive type");
        } 
      } else {
        Array.set(array, index, value);
      } 
      index++;
    } 
    return array;
  }
  
  protected Set<Object> constructSet(MappingNode node) {
    Set<Object> set = newSet((CollectionNode<?>)node);
    constructSet2ndStep(node, set);
    return set;
  }
  
  protected Map<Object, Object> constructMapping(MappingNode node) {
    Map<Object, Object> mapping = newMap(node);
    constructMapping2ndStep(node, mapping);
    return mapping;
  }
  
  protected void constructMapping2ndStep(MappingNode node, Map<Object, Object> mapping) {
    List<NodeTuple> nodeValue = node.getValue();
    for (NodeTuple tuple : nodeValue) {
      Node keyNode = tuple.getKeyNode();
      Node valueNode = tuple.getValueNode();
      Object key = constructObject(keyNode);
      if (key != null)
        try {
          key.hashCode();
        } catch (Exception e) {
          throw new ConstructorException("while constructing a mapping", node.getStartMark(), "found unacceptable key " + key, tuple.getKeyNode().getStartMark(), e);
        }  
      Object value = constructObject(valueNode);
      if (keyNode.isTwoStepsConstruction()) {
        if (this.loadingConfig.getAllowRecursiveKeys()) {
          postponeMapFilling(mapping, key, value);
          continue;
        } 
        throw new YAMLException("Recursive key for mapping is detected but it is not configured to be allowed.");
      } 
      mapping.put(key, value);
    } 
  }
  
  protected void postponeMapFilling(Map<Object, Object> mapping, Object key, Object value) {
    this.maps2fill.add(0, new RecursiveTuple<>(mapping, new RecursiveTuple<>(key, value)));
  }
  
  protected void constructSet2ndStep(MappingNode node, Set<Object> set) {
    List<NodeTuple> nodeValue = node.getValue();
    for (NodeTuple tuple : nodeValue) {
      Node keyNode = tuple.getKeyNode();
      Object key = constructObject(keyNode);
      if (key != null)
        try {
          key.hashCode();
        } catch (Exception e) {
          throw new ConstructorException("while constructing a Set", node.getStartMark(), "found unacceptable key " + key, tuple.getKeyNode().getStartMark(), e);
        }  
      if (keyNode.isTwoStepsConstruction()) {
        postponeSetFilling(set, key);
        continue;
      } 
      set.add(key);
    } 
  }
  
  protected void postponeSetFilling(Set<Object> set, Object key) {
    this.sets2fill.add(0, new RecursiveTuple<>(set, key));
  }
  
  public void setPropertyUtils(PropertyUtils propertyUtils) {
    this.propertyUtils = propertyUtils;
    this.explicitPropertyUtils = true;
    Collection<TypeDescription> tds = this.typeDefinitions.values();
    for (TypeDescription typeDescription : tds)
      typeDescription.setPropertyUtils(propertyUtils); 
  }
  
  public final PropertyUtils getPropertyUtils() {
    if (this.propertyUtils == null)
      this.propertyUtils = new PropertyUtils(); 
    return this.propertyUtils;
  }
  
  public TypeDescription addTypeDescription(TypeDescription definition) {
    if (definition == null)
      throw new NullPointerException("TypeDescription is required."); 
    Tag tag = definition.getTag();
    this.typeTags.put(tag, definition.getType());
    definition.setPropertyUtils(getPropertyUtils());
    return this.typeDefinitions.put(definition.getType(), definition);
  }
  
  private static class RecursiveTuple<T, K> {
    private final T _1;
    
    private final K _2;
    
    public RecursiveTuple(T _1, K _2) {
      this._1 = _1;
      this._2 = _2;
    }
    
    public K _2() {
      return this._2;
    }
    
    public T _1() {
      return this._1;
    }
  }
  
  public final boolean isExplicitPropertyUtils() {
    return this.explicitPropertyUtils;
  }
  
  public boolean isAllowDuplicateKeys() {
    return this.allowDuplicateKeys;
  }
  
  public void setAllowDuplicateKeys(boolean allowDuplicateKeys) {
    this.allowDuplicateKeys = allowDuplicateKeys;
  }
  
  public boolean isWrappedToRootException() {
    return this.wrappedToRootException;
  }
  
  public void setWrappedToRootException(boolean wrappedToRootException) {
    this.wrappedToRootException = wrappedToRootException;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\constructor\BaseConstructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */