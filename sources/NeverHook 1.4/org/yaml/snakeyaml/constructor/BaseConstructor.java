/*     */ package org.yaml.snakeyaml.constructor;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import org.yaml.snakeyaml.LoaderOptions;
/*     */ import org.yaml.snakeyaml.TypeDescription;
/*     */ import org.yaml.snakeyaml.composer.Composer;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.introspector.PropertyUtils;
/*     */ import org.yaml.snakeyaml.nodes.CollectionNode;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseConstructor
/*     */ {
/*  56 */   protected final Map<NodeId, Construct> yamlClassConstructors = new EnumMap<>(NodeId.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   protected final Map<Tag, Construct> yamlConstructors = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   protected final Map<String, Construct> yamlMultiConstructors = new HashMap<>();
/*     */   
/*     */   protected Composer composer;
/*     */   
/*     */   final Map<Node, Object> constructedObjects;
/*     */   
/*     */   private final Set<Node> recursiveObjects;
/*     */   
/*     */   private final ArrayList<RecursiveTuple<Map<Object, Object>, RecursiveTuple<Object, Object>>> maps2fill;
/*     */   
/*     */   private final ArrayList<RecursiveTuple<Set<Object>, Object>> sets2fill;
/*     */   protected Tag rootTag;
/*     */   private PropertyUtils propertyUtils;
/*     */   private boolean explicitPropertyUtils;
/*     */   private boolean allowDuplicateKeys = true;
/*     */   private boolean wrappedToRootException = false;
/*     */   protected final Map<Class<? extends Object>, TypeDescription> typeDefinitions;
/*     */   protected final Map<Tag, Class<? extends Object>> typeTags;
/*     */   protected LoaderOptions loadingConfig;
/*     */   
/*     */   public BaseConstructor() {
/*  91 */     this(new LoaderOptions());
/*     */   }
/*     */   
/*     */   public BaseConstructor(LoaderOptions loadingConfig) {
/*  95 */     this.constructedObjects = new HashMap<>();
/*  96 */     this.recursiveObjects = new HashSet<>();
/*  97 */     this.maps2fill = new ArrayList<>();
/*  98 */     this.sets2fill = new ArrayList<>();
/*  99 */     this.typeDefinitions = new HashMap<>();
/* 100 */     this.typeTags = new HashMap<>();
/*     */     
/* 102 */     this.rootTag = null;
/* 103 */     this.explicitPropertyUtils = false;
/*     */     
/* 105 */     this.typeDefinitions.put(SortedMap.class, new TypeDescription(SortedMap.class, Tag.OMAP, TreeMap.class));
/*     */     
/* 107 */     this.typeDefinitions.put(SortedSet.class, new TypeDescription(SortedSet.class, Tag.SET, TreeSet.class));
/*     */     
/* 109 */     this.loadingConfig = loadingConfig;
/*     */   }
/*     */   
/*     */   public void setComposer(Composer composer) {
/* 113 */     this.composer = composer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkData() {
/* 123 */     return this.composer.checkNode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getData() throws NoSuchElementException {
/* 133 */     if (!this.composer.checkNode()) throw new NoSuchElementException("No document is available."); 
/* 134 */     Node node = this.composer.getNode();
/* 135 */     if (this.rootTag != null) {
/* 136 */       node.setTag(this.rootTag);
/*     */     }
/* 138 */     return constructDocument(node);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getSingleData(Class<?> type) {
/* 150 */     Node node = this.composer.getSingleNode();
/* 151 */     if (node != null && !Tag.NULL.equals(node.getTag())) {
/* 152 */       if (Object.class != type) {
/* 153 */         node.setTag(new Tag(type));
/* 154 */       } else if (this.rootTag != null) {
/* 155 */         node.setTag(this.rootTag);
/*     */       } 
/* 157 */       return constructDocument(node);
/*     */     } 
/* 159 */     Construct construct = this.yamlConstructors.get(Tag.NULL);
/* 160 */     return construct.construct(node);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Object constructDocument(Node node) {
/*     */     try {
/* 173 */       Object data = constructObject(node);
/* 174 */       fillRecursive();
/* 175 */       return data;
/* 176 */     } catch (RuntimeException e) {
/* 177 */       if (this.wrappedToRootException && !(e instanceof YAMLException)) {
/* 178 */         throw new YAMLException(e);
/*     */       }
/* 180 */       throw e;
/*     */     }
/*     */     finally {
/*     */       
/* 184 */       this.constructedObjects.clear();
/* 185 */       this.recursiveObjects.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fillRecursive() {
/* 193 */     if (!this.maps2fill.isEmpty()) {
/* 194 */       for (RecursiveTuple<Map<Object, Object>, RecursiveTuple<Object, Object>> entry : this.maps2fill) {
/* 195 */         RecursiveTuple<Object, Object> key_value = entry._2();
/* 196 */         ((Map)entry._1()).put(key_value._1(), key_value._2());
/*     */       } 
/* 198 */       this.maps2fill.clear();
/*     */     } 
/* 200 */     if (!this.sets2fill.isEmpty()) {
/* 201 */       for (RecursiveTuple<Set<Object>, Object> value : this.sets2fill) {
/* 202 */         ((Set)value._1()).add(value._2());
/*     */       }
/* 204 */       this.sets2fill.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object constructObject(Node node) {
/* 216 */     if (this.constructedObjects.containsKey(node)) {
/* 217 */       return this.constructedObjects.get(node);
/*     */     }
/* 219 */     return constructObjectNoCheck(node);
/*     */   }
/*     */   
/*     */   protected Object constructObjectNoCheck(Node node) {
/* 223 */     if (this.recursiveObjects.contains(node)) {
/* 224 */       throw new ConstructorException(null, null, "found unconstructable recursive node", node.getStartMark());
/*     */     }
/*     */     
/* 227 */     this.recursiveObjects.add(node);
/* 228 */     Construct constructor = getConstructor(node);
/* 229 */     Object data = this.constructedObjects.containsKey(node) ? this.constructedObjects.get(node) : constructor.construct(node);
/*     */ 
/*     */     
/* 232 */     finalizeConstruction(node, data);
/* 233 */     this.constructedObjects.put(node, data);
/* 234 */     this.recursiveObjects.remove(node);
/* 235 */     if (node.isTwoStepsConstruction()) {
/* 236 */       constructor.construct2ndStep(node, data);
/*     */     }
/* 238 */     return data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Construct getConstructor(Node node) {
/* 250 */     if (node.useClassConstructor()) {
/* 251 */       return this.yamlClassConstructors.get(node.getNodeId());
/*     */     }
/* 253 */     Construct constructor = this.yamlConstructors.get(node.getTag());
/* 254 */     if (constructor == null) {
/* 255 */       for (String prefix : this.yamlMultiConstructors.keySet()) {
/* 256 */         if (node.getTag().startsWith(prefix)) {
/* 257 */           return this.yamlMultiConstructors.get(prefix);
/*     */         }
/*     */       } 
/* 260 */       return this.yamlConstructors.get(null);
/*     */     } 
/* 262 */     return constructor;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String constructScalar(ScalarNode node) {
/* 267 */     return node.getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<Object> createDefaultList(int initSize) {
/* 272 */     return new ArrayList(initSize);
/*     */   }
/*     */   
/*     */   protected Set<Object> createDefaultSet(int initSize) {
/* 276 */     return new LinkedHashSet(initSize);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<Object, Object> createDefaultMap(int initSize) {
/* 281 */     return new LinkedHashMap<>(initSize);
/*     */   }
/*     */   
/*     */   protected Object createArray(Class<?> type, int size) {
/* 285 */     return Array.newInstance(type.getComponentType(), size);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object finalizeConstruction(Node node, Object data) {
/* 291 */     Class<? extends Object> type = node.getType();
/* 292 */     if (this.typeDefinitions.containsKey(type)) {
/* 293 */       return ((TypeDescription)this.typeDefinitions.get(type)).finalizeConstruction(data);
/*     */     }
/* 295 */     return data;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object newInstance(Node node) {
/*     */     try {
/* 301 */       return newInstance(Object.class, node);
/* 302 */     } catch (InstantiationException e) {
/* 303 */       throw new YAMLException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final Object newInstance(Class<?> ancestor, Node node) throws InstantiationException {
/* 308 */     return newInstance(ancestor, node, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object newInstance(Class<?> ancestor, Node node, boolean tryDefault) throws InstantiationException {
/* 313 */     Class<? extends Object> type = node.getType();
/* 314 */     if (this.typeDefinitions.containsKey(type)) {
/* 315 */       TypeDescription td = this.typeDefinitions.get(type);
/* 316 */       Object instance = td.newInstance(node);
/* 317 */       if (instance != null) {
/* 318 */         return instance;
/*     */       }
/*     */     } 
/* 321 */     if (tryDefault)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 326 */       if (ancestor.isAssignableFrom(type) && !Modifier.isAbstract(type.getModifiers())) {
/*     */         try {
/* 328 */           Constructor<?> c = type.getDeclaredConstructor(new Class[0]);
/* 329 */           c.setAccessible(true);
/* 330 */           return c.newInstance(new Object[0]);
/* 331 */         } catch (NoSuchMethodException e) {
/* 332 */           throw new InstantiationException("NoSuchMethodException:" + e.getLocalizedMessage());
/*     */         }
/* 334 */         catch (Exception e) {
/* 335 */           throw new YAMLException(e);
/*     */         } 
/*     */       }
/*     */     }
/* 339 */     throw new InstantiationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Set<Object> newSet(CollectionNode<?> node) {
/*     */     try {
/* 345 */       return (Set<Object>)newInstance(Set.class, (Node)node);
/* 346 */     } catch (InstantiationException e) {
/* 347 */       return createDefaultSet(node.getValue().size());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<Object> newList(SequenceNode node) {
/*     */     try {
/* 354 */       return (List<Object>)newInstance(List.class, (Node)node);
/* 355 */     } catch (InstantiationException e) {
/* 356 */       return createDefaultList(node.getValue().size());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<Object, Object> newMap(MappingNode node) {
/*     */     try {
/* 363 */       return (Map<Object, Object>)newInstance(Map.class, (Node)node);
/* 364 */     } catch (InstantiationException e) {
/* 365 */       return createDefaultMap(node.getValue().size());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<? extends Object> constructSequence(SequenceNode node) {
/* 373 */     List<Object> result = newList(node);
/* 374 */     constructSequenceStep2(node, result);
/* 375 */     return result;
/*     */   }
/*     */   
/*     */   protected Set<? extends Object> constructSet(SequenceNode node) {
/* 379 */     Set<Object> result = newSet((CollectionNode<?>)node);
/* 380 */     constructSequenceStep2(node, result);
/* 381 */     return result;
/*     */   }
/*     */   
/*     */   protected Object constructArray(SequenceNode node) {
/* 385 */     return constructArrayStep2(node, createArray(node.getType(), node.getValue().size()));
/*     */   }
/*     */   
/*     */   protected void constructSequenceStep2(SequenceNode node, Collection<Object> collection) {
/* 389 */     for (Node child : node.getValue()) {
/* 390 */       collection.add(constructObject(child));
/*     */     }
/*     */   }
/*     */   
/*     */   protected Object constructArrayStep2(SequenceNode node, Object array) {
/* 395 */     Class<?> componentType = node.getType().getComponentType();
/*     */     
/* 397 */     int index = 0;
/* 398 */     for (Node child : node.getValue()) {
/*     */       
/* 400 */       if (child.getType() == Object.class) {
/* 401 */         child.setType(componentType);
/*     */       }
/*     */       
/* 404 */       Object value = constructObject(child);
/*     */       
/* 406 */       if (componentType.isPrimitive()) {
/*     */         
/* 408 */         if (value == null) {
/* 409 */           throw new NullPointerException("Unable to construct element value for " + child);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 414 */         if (byte.class.equals(componentType)) {
/* 415 */           Array.setByte(array, index, ((Number)value).byteValue());
/*     */         }
/* 417 */         else if (short.class.equals(componentType)) {
/* 418 */           Array.setShort(array, index, ((Number)value).shortValue());
/*     */         }
/* 420 */         else if (int.class.equals(componentType)) {
/* 421 */           Array.setInt(array, index, ((Number)value).intValue());
/*     */         }
/* 423 */         else if (long.class.equals(componentType)) {
/* 424 */           Array.setLong(array, index, ((Number)value).longValue());
/*     */         }
/* 426 */         else if (float.class.equals(componentType)) {
/* 427 */           Array.setFloat(array, index, ((Number)value).floatValue());
/*     */         }
/* 429 */         else if (double.class.equals(componentType)) {
/* 430 */           Array.setDouble(array, index, ((Number)value).doubleValue());
/*     */         }
/* 432 */         else if (char.class.equals(componentType)) {
/* 433 */           Array.setChar(array, index, ((Character)value).charValue());
/*     */         }
/* 435 */         else if (boolean.class.equals(componentType)) {
/* 436 */           Array.setBoolean(array, index, ((Boolean)value).booleanValue());
/*     */         } else {
/*     */           
/* 439 */           throw new YAMLException("unexpected primitive type");
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 444 */         Array.set(array, index, value);
/*     */       } 
/*     */       
/* 447 */       index++;
/*     */     } 
/* 449 */     return array;
/*     */   }
/*     */   
/*     */   protected Set<Object> constructSet(MappingNode node) {
/* 453 */     Set<Object> set = newSet((CollectionNode<?>)node);
/* 454 */     constructSet2ndStep(node, set);
/* 455 */     return set;
/*     */   }
/*     */   
/*     */   protected Map<Object, Object> constructMapping(MappingNode node) {
/* 459 */     Map<Object, Object> mapping = newMap(node);
/* 460 */     constructMapping2ndStep(node, mapping);
/* 461 */     return mapping;
/*     */   }
/*     */   
/*     */   protected void constructMapping2ndStep(MappingNode node, Map<Object, Object> mapping) {
/* 465 */     List<NodeTuple> nodeValue = node.getValue();
/* 466 */     for (NodeTuple tuple : nodeValue) {
/* 467 */       Node keyNode = tuple.getKeyNode();
/* 468 */       Node valueNode = tuple.getValueNode();
/* 469 */       Object key = constructObject(keyNode);
/* 470 */       if (key != null) {
/*     */         try {
/* 472 */           key.hashCode();
/* 473 */         } catch (Exception e) {
/* 474 */           throw new ConstructorException("while constructing a mapping", node.getStartMark(), "found unacceptable key " + key, tuple.getKeyNode().getStartMark(), e);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 479 */       Object value = constructObject(valueNode);
/* 480 */       if (keyNode.isTwoStepsConstruction()) {
/* 481 */         if (this.loadingConfig.getAllowRecursiveKeys()) {
/* 482 */           postponeMapFilling(mapping, key, value); continue;
/*     */         } 
/* 484 */         throw new YAMLException("Recursive key for mapping is detected but it is not configured to be allowed.");
/*     */       } 
/*     */       
/* 487 */       mapping.put(key, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postponeMapFilling(Map<Object, Object> mapping, Object key, Object value) {
/* 499 */     this.maps2fill.add(0, new RecursiveTuple<>(mapping, new RecursiveTuple<>(key, value)));
/*     */   }
/*     */   
/*     */   protected void constructSet2ndStep(MappingNode node, Set<Object> set) {
/* 503 */     List<NodeTuple> nodeValue = node.getValue();
/* 504 */     for (NodeTuple tuple : nodeValue) {
/* 505 */       Node keyNode = tuple.getKeyNode();
/* 506 */       Object key = constructObject(keyNode);
/* 507 */       if (key != null) {
/*     */         try {
/* 509 */           key.hashCode();
/* 510 */         } catch (Exception e) {
/* 511 */           throw new ConstructorException("while constructing a Set", node.getStartMark(), "found unacceptable key " + key, tuple.getKeyNode().getStartMark(), e);
/*     */         } 
/*     */       }
/*     */       
/* 515 */       if (keyNode.isTwoStepsConstruction()) {
/* 516 */         postponeSetFilling(set, key); continue;
/*     */       } 
/* 518 */       set.add(key);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postponeSetFilling(Set<Object> set, Object key) {
/* 530 */     this.sets2fill.add(0, new RecursiveTuple<>(set, key));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropertyUtils(PropertyUtils propertyUtils) {
/* 548 */     this.propertyUtils = propertyUtils;
/* 549 */     this.explicitPropertyUtils = true;
/* 550 */     Collection<TypeDescription> tds = this.typeDefinitions.values();
/* 551 */     for (TypeDescription typeDescription : tds) {
/* 552 */       typeDescription.setPropertyUtils(propertyUtils);
/*     */     }
/*     */   }
/*     */   
/*     */   public final PropertyUtils getPropertyUtils() {
/* 557 */     if (this.propertyUtils == null) {
/* 558 */       this.propertyUtils = new PropertyUtils();
/*     */     }
/* 560 */     return this.propertyUtils;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDescription addTypeDescription(TypeDescription definition) {
/* 573 */     if (definition == null) {
/* 574 */       throw new NullPointerException("TypeDescription is required.");
/*     */     }
/* 576 */     Tag tag = definition.getTag();
/* 577 */     this.typeTags.put(tag, definition.getType());
/* 578 */     definition.setPropertyUtils(getPropertyUtils());
/* 579 */     return this.typeDefinitions.put(definition.getType(), definition);
/*     */   }
/*     */   
/*     */   private static class RecursiveTuple<T, K> {
/*     */     private final T _1;
/*     */     private final K _2;
/*     */     
/*     */     public RecursiveTuple(T _1, K _2) {
/* 587 */       this._1 = _1;
/* 588 */       this._2 = _2;
/*     */     }
/*     */     
/*     */     public K _2() {
/* 592 */       return this._2;
/*     */     }
/*     */     
/*     */     public T _1() {
/* 596 */       return this._1;
/*     */     }
/*     */   }
/*     */   
/*     */   public final boolean isExplicitPropertyUtils() {
/* 601 */     return this.explicitPropertyUtils;
/*     */   }
/*     */   
/*     */   public boolean isAllowDuplicateKeys() {
/* 605 */     return this.allowDuplicateKeys;
/*     */   }
/*     */   
/*     */   public void setAllowDuplicateKeys(boolean allowDuplicateKeys) {
/* 609 */     this.allowDuplicateKeys = allowDuplicateKeys;
/*     */   }
/*     */   
/*     */   public boolean isWrappedToRootException() {
/* 613 */     return this.wrappedToRootException;
/*     */   }
/*     */   
/*     */   public void setWrappedToRootException(boolean wrappedToRootException) {
/* 617 */     this.wrappedToRootException = wrappedToRootException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\constructor\BaseConstructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */