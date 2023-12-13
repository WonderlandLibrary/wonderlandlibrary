/*     */ package org.yaml.snakeyaml.constructor;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import org.yaml.snakeyaml.LoaderOptions;
/*     */ import org.yaml.snakeyaml.TypeDescription;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.introspector.Property;
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
/*     */ public class Constructor
/*     */   extends SafeConstructor
/*     */ {
/*     */   public Constructor() {
/*  47 */     this(Object.class);
/*     */   }
/*     */   
/*     */   public Constructor(LoaderOptions loadingConfig) {
/*  51 */     this(Object.class, loadingConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Constructor(Class<? extends Object> theRoot) {
/*  61 */     this(new TypeDescription(checkRoot(theRoot)));
/*     */   }
/*     */   
/*     */   public Constructor(Class<? extends Object> theRoot, LoaderOptions loadingConfig) {
/*  65 */     this(new TypeDescription(checkRoot(theRoot)), loadingConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<? extends Object> checkRoot(Class<? extends Object> theRoot) {
/*  72 */     if (theRoot == null) {
/*  73 */       throw new NullPointerException("Root class must be provided.");
/*     */     }
/*  75 */     return theRoot;
/*     */   }
/*     */   
/*     */   public Constructor(TypeDescription theRoot) {
/*  79 */     this(theRoot, (Collection<TypeDescription>)null, new LoaderOptions());
/*     */   }
/*     */   
/*     */   public Constructor(TypeDescription theRoot, LoaderOptions loadingConfig) {
/*  83 */     this(theRoot, (Collection<TypeDescription>)null, loadingConfig);
/*     */   }
/*     */   
/*     */   public Constructor(TypeDescription theRoot, Collection<TypeDescription> moreTDs) {
/*  87 */     this(theRoot, moreTDs, new LoaderOptions());
/*     */   }
/*     */   
/*     */   public Constructor(TypeDescription theRoot, Collection<TypeDescription> moreTDs, LoaderOptions loadingConfig) {
/*  91 */     super(loadingConfig);
/*  92 */     if (theRoot == null) {
/*  93 */       throw new NullPointerException("Root type must be provided.");
/*     */     }
/*  95 */     this.yamlConstructors.put(null, new ConstructYamlObject());
/*  96 */     if (!Object.class.equals(theRoot.getType())) {
/*  97 */       this.rootTag = new Tag(theRoot.getType());
/*     */     }
/*  99 */     this.yamlClassConstructors.put(NodeId.scalar, new ConstructScalar());
/* 100 */     this.yamlClassConstructors.put(NodeId.mapping, new ConstructMapping());
/* 101 */     this.yamlClassConstructors.put(NodeId.sequence, new ConstructSequence());
/* 102 */     addTypeDescription(theRoot);
/* 103 */     if (moreTDs != null) {
/* 104 */       for (TypeDescription td : moreTDs) {
/* 105 */         addTypeDescription(td);
/*     */       }
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
/*     */ 
/*     */   
/*     */   public Constructor(String theRoot) throws ClassNotFoundException {
/* 120 */     this((Class)Class.forName(check(theRoot)));
/*     */   }
/*     */   
/*     */   public Constructor(String theRoot, LoaderOptions loadingConfig) throws ClassNotFoundException {
/* 124 */     this((Class)Class.forName(check(theRoot)), loadingConfig);
/*     */   }
/*     */   
/*     */   private static final String check(String s) {
/* 128 */     if (s == null) {
/* 129 */       throw new NullPointerException("Root type must be provided.");
/*     */     }
/* 131 */     if (s.trim().length() == 0) {
/* 132 */       throw new YAMLException("Root type must be provided.");
/*     */     }
/* 134 */     return s;
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
/*     */   protected class ConstructMapping
/*     */     implements Construct
/*     */   {
/*     */     public Object construct(Node node) {
/* 153 */       MappingNode mnode = (MappingNode)node;
/* 154 */       if (Map.class.isAssignableFrom(node.getType())) {
/* 155 */         if (node.isTwoStepsConstruction()) {
/* 156 */           return Constructor.this.newMap(mnode);
/*     */         }
/* 158 */         return Constructor.this.constructMapping(mnode);
/*     */       } 
/* 160 */       if (Collection.class.isAssignableFrom(node.getType())) {
/* 161 */         if (node.isTwoStepsConstruction()) {
/* 162 */           return Constructor.this.newSet((CollectionNode<?>)mnode);
/*     */         }
/* 164 */         return Constructor.this.constructSet(mnode);
/*     */       } 
/*     */       
/* 167 */       Object obj = Constructor.this.newInstance((Node)mnode);
/* 168 */       if (node.isTwoStepsConstruction()) {
/* 169 */         return obj;
/*     */       }
/* 171 */       return constructJavaBean2ndStep(mnode, obj);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 178 */       if (Map.class.isAssignableFrom(node.getType())) {
/* 179 */         Constructor.this.constructMapping2ndStep((MappingNode)node, (Map<Object, Object>)object);
/* 180 */       } else if (Set.class.isAssignableFrom(node.getType())) {
/* 181 */         Constructor.this.constructSet2ndStep((MappingNode)node, (Set<Object>)object);
/*     */       } else {
/* 183 */         constructJavaBean2ndStep((MappingNode)node, object);
/*     */       } 
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Object constructJavaBean2ndStep(MappingNode node, Object object) {
/* 212 */       Constructor.this.flattenMapping(node);
/* 213 */       Class<? extends Object> beanType = node.getType();
/* 214 */       List<NodeTuple> nodeValue = node.getValue();
/* 215 */       for (NodeTuple tuple : nodeValue) {
/*     */         ScalarNode keyNode;
/* 217 */         if (tuple.getKeyNode() instanceof ScalarNode) {
/*     */           
/* 219 */           keyNode = (ScalarNode)tuple.getKeyNode();
/*     */         } else {
/* 221 */           throw new YAMLException("Keys must be scalars but found: " + tuple.getKeyNode());
/*     */         } 
/*     */         
/* 224 */         Node valueNode = tuple.getValueNode();
/*     */         
/* 226 */         keyNode.setType(String.class);
/* 227 */         String key = (String)Constructor.this.constructObject((Node)keyNode);
/*     */         try {
/* 229 */           TypeDescription memberDescription = Constructor.this.typeDefinitions.get(beanType);
/* 230 */           Property property = (memberDescription == null) ? getProperty(beanType, key) : memberDescription.getProperty(key);
/*     */ 
/*     */           
/* 233 */           if (!property.isWritable()) {
/* 234 */             throw new YAMLException("No writable property '" + key + "' on class: " + beanType.getName());
/*     */           }
/*     */ 
/*     */           
/* 238 */           valueNode.setType(property.getType());
/* 239 */           boolean typeDetected = (memberDescription != null) ? memberDescription.setupPropertyType(key, valueNode) : false;
/*     */ 
/*     */           
/* 242 */           if (!typeDetected && valueNode.getNodeId() != NodeId.scalar) {
/*     */             
/* 244 */             Class<?>[] arguments = property.getActualTypeArguments();
/* 245 */             if (arguments != null && arguments.length > 0)
/*     */             {
/*     */               
/* 248 */               if (valueNode.getNodeId() == NodeId.sequence) {
/* 249 */                 Class<?> t = arguments[0];
/* 250 */                 SequenceNode snode = (SequenceNode)valueNode;
/* 251 */                 snode.setListType(t);
/* 252 */               } else if (Set.class.isAssignableFrom(valueNode.getType())) {
/* 253 */                 Class<?> t = arguments[0];
/* 254 */                 MappingNode mnode = (MappingNode)valueNode;
/* 255 */                 mnode.setOnlyKeyType(t);
/* 256 */                 mnode.setUseClassConstructor(Boolean.valueOf(true));
/* 257 */               } else if (Map.class.isAssignableFrom(valueNode.getType())) {
/* 258 */                 Class<?> keyType = arguments[0];
/* 259 */                 Class<?> valueType = arguments[1];
/* 260 */                 MappingNode mnode = (MappingNode)valueNode;
/* 261 */                 mnode.setTypes(keyType, valueType);
/* 262 */                 mnode.setUseClassConstructor(Boolean.valueOf(true));
/*     */               } 
/*     */             }
/*     */           } 
/*     */           
/* 267 */           Object value = (memberDescription != null) ? newInstance(memberDescription, key, valueNode) : Constructor.this.constructObject(valueNode);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 272 */           if ((property.getType() == float.class || property.getType() == Float.class) && 
/* 273 */             value instanceof Double) {
/* 274 */             value = Float.valueOf(((Double)value).floatValue());
/*     */           }
/*     */ 
/*     */           
/* 278 */           if (property.getType() == String.class && Tag.BINARY.equals(valueNode.getTag()) && value instanceof byte[])
/*     */           {
/* 280 */             value = new String((byte[])value);
/*     */           }
/*     */           
/* 283 */           if (memberDescription == null || !memberDescription.setProperty(object, key, value))
/*     */           {
/* 285 */             property.set(object, value);
/*     */           }
/* 287 */         } catch (DuplicateKeyException e) {
/* 288 */           throw e;
/* 289 */         } catch (Exception e) {
/* 290 */           throw new ConstructorException("Cannot create property=" + key + " for JavaBean=" + object, node.getStartMark(), e.getMessage(), valueNode.getStartMark(), e);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 295 */       return object;
/*     */     }
/*     */ 
/*     */     
/*     */     private Object newInstance(TypeDescription memberDescription, String propertyName, Node node) {
/* 300 */       Object newInstance = memberDescription.newInstance(propertyName, node);
/* 301 */       if (newInstance != null) {
/* 302 */         Constructor.this.constructedObjects.put(node, newInstance);
/* 303 */         return Constructor.this.constructObjectNoCheck(node);
/*     */       } 
/* 305 */       return Constructor.this.constructObject(node);
/*     */     }
/*     */     
/*     */     protected Property getProperty(Class<? extends Object> type, String name) {
/* 309 */       return Constructor.this.getPropertyUtils().getProperty(type, name);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class ConstructYamlObject
/*     */     implements Construct
/*     */   {
/*     */     private Construct getConstructor(Node node) {
/* 322 */       Class<?> cl = Constructor.this.getClassForNode(node);
/* 323 */       node.setType(cl);
/*     */       
/* 325 */       Construct constructor = Constructor.this.yamlClassConstructors.get(node.getNodeId());
/* 326 */       return constructor;
/*     */     }
/*     */     
/*     */     public Object construct(Node node) {
/*     */       try {
/* 331 */         return getConstructor(node).construct(node);
/* 332 */       } catch (ConstructorException e) {
/* 333 */         throw e;
/* 334 */       } catch (Exception e) {
/* 335 */         throw new ConstructorException(null, null, "Can't construct a java object for " + node.getTag() + "; exception=" + e.getMessage(), node.getStartMark(), e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/*     */       try {
/* 342 */         getConstructor(node).construct2ndStep(node, object);
/* 343 */       } catch (Exception e) {
/* 344 */         throw new ConstructorException(null, null, "Can't construct a second step for a java object for " + node.getTag() + "; exception=" + e.getMessage(), node.getStartMark(), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class ConstructScalar
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node nnode) {
/* 358 */       ScalarNode node = (ScalarNode)nnode;
/* 359 */       Class<?> type = node.getType();
/*     */       
/*     */       try {
/* 362 */         return Constructor.this.newInstance(type, (Node)node, false);
/* 363 */       } catch (InstantiationException e1) {
/*     */         Object result;
/*     */ 
/*     */         
/* 367 */         if (type.isPrimitive() || type == String.class || Number.class.isAssignableFrom(type) || type == Boolean.class || Date.class.isAssignableFrom(type) || type == Character.class || type == BigInteger.class || type == BigDecimal.class || Enum.class.isAssignableFrom(type) || Tag.BINARY.equals(node.getTag()) || Calendar.class.isAssignableFrom(type) || type == UUID.class) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 374 */           result = constructStandardJavaInstance(type, node);
/*     */         } else {
/*     */           Object argument;
/* 377 */           Constructor[] arrayOfConstructor = (Constructor[])type.getDeclaredConstructors();
/*     */           
/* 379 */           int oneArgCount = 0;
/* 380 */           Constructor<?> javaConstructor = null;
/* 381 */           for (Constructor<?> c : arrayOfConstructor) {
/* 382 */             if ((c.getParameterTypes()).length == 1) {
/* 383 */               oneArgCount++;
/* 384 */               javaConstructor = c;
/*     */             } 
/*     */           } 
/*     */           
/* 388 */           if (javaConstructor == null) {
/*     */             try {
/* 390 */               return Constructor.this.newInstance(type, (Node)node, false);
/* 391 */             } catch (InstantiationException ie) {
/* 392 */               throw new YAMLException("No single argument constructor found for " + type + " : " + ie.getMessage());
/*     */             } 
/*     */           }
/* 395 */           if (oneArgCount == 1) {
/* 396 */             argument = constructStandardJavaInstance(javaConstructor.getParameterTypes()[0], node);
/*     */ 
/*     */ 
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */ 
/*     */             
/* 405 */             argument = Constructor.this.constructScalar(node);
/*     */             try {
/* 407 */               javaConstructor = type.getDeclaredConstructor(new Class[] { String.class });
/* 408 */             } catch (Exception e) {
/* 409 */               throw new YAMLException("Can't construct a java object for scalar " + node.getTag() + "; No String constructor found. Exception=" + e.getMessage(), e);
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/*     */           try {
/* 415 */             javaConstructor.setAccessible(true);
/* 416 */             result = javaConstructor.newInstance(new Object[] { argument });
/* 417 */           } catch (Exception e) {
/* 418 */             throw new ConstructorException(null, null, "Can't construct a java object for scalar " + node.getTag() + "; exception=" + e.getMessage(), node.getStartMark(), e);
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 424 */         return result;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private Object constructStandardJavaInstance(Class<String> type, ScalarNode node) {
/*     */       Object result;
/* 431 */       if (type == String.class) {
/* 432 */         Construct stringConstructor = Constructor.this.yamlConstructors.get(Tag.STR);
/* 433 */         result = stringConstructor.construct((Node)node);
/* 434 */       } else if (type == Boolean.class || type == boolean.class) {
/* 435 */         Construct boolConstructor = Constructor.this.yamlConstructors.get(Tag.BOOL);
/* 436 */         result = boolConstructor.construct((Node)node);
/* 437 */       } else if (type == Character.class || type == char.class) {
/* 438 */         Construct charConstructor = Constructor.this.yamlConstructors.get(Tag.STR);
/* 439 */         String ch = (String)charConstructor.construct((Node)node);
/* 440 */         if (ch.length() == 0)
/* 441 */         { result = null; }
/* 442 */         else { if (ch.length() != 1) {
/* 443 */             throw new YAMLException("Invalid node Character: '" + ch + "'; length: " + ch.length());
/*     */           }
/*     */           
/* 446 */           result = Character.valueOf(ch.charAt(0)); }
/*     */       
/* 448 */       } else if (Date.class.isAssignableFrom(type)) {
/* 449 */         Construct dateConstructor = Constructor.this.yamlConstructors.get(Tag.TIMESTAMP);
/* 450 */         Date date = (Date)dateConstructor.construct((Node)node);
/* 451 */         if (type == Date.class) {
/* 452 */           result = date;
/*     */         } else {
/*     */           try {
/* 455 */             Constructor<?> constr = type.getConstructor(new Class[] { long.class });
/* 456 */             result = constr.newInstance(new Object[] { Long.valueOf(date.getTime()) });
/* 457 */           } catch (RuntimeException e) {
/* 458 */             throw e;
/* 459 */           } catch (Exception e) {
/* 460 */             throw new YAMLException("Cannot construct: '" + type + "'");
/*     */           } 
/*     */         } 
/* 463 */       } else if (type == Float.class || type == Double.class || type == float.class || type == double.class || type == BigDecimal.class) {
/*     */         
/* 465 */         if (type == BigDecimal.class) {
/* 466 */           result = new BigDecimal(node.getValue());
/*     */         } else {
/* 468 */           Construct doubleConstructor = Constructor.this.yamlConstructors.get(Tag.FLOAT);
/* 469 */           result = doubleConstructor.construct((Node)node);
/* 470 */           if (type == Float.class || type == float.class) {
/* 471 */             result = Float.valueOf(((Double)result).floatValue());
/*     */           }
/*     */         } 
/* 474 */       } else if (type == Byte.class || type == Short.class || type == Integer.class || type == Long.class || type == BigInteger.class || type == byte.class || type == short.class || type == int.class || type == long.class) {
/*     */ 
/*     */         
/* 477 */         Construct intConstructor = Constructor.this.yamlConstructors.get(Tag.INT);
/* 478 */         result = intConstructor.construct((Node)node);
/* 479 */         if (type == Byte.class || type == byte.class) {
/* 480 */           result = Byte.valueOf(Integer.valueOf(result.toString()).byteValue());
/* 481 */         } else if (type == Short.class || type == short.class) {
/* 482 */           result = Short.valueOf(Integer.valueOf(result.toString()).shortValue());
/* 483 */         } else if (type == Integer.class || type == int.class) {
/* 484 */           result = Integer.valueOf(Integer.parseInt(result.toString()));
/* 485 */         } else if (type == Long.class || type == long.class) {
/* 486 */           result = Long.valueOf(result.toString());
/*     */         } else {
/*     */           
/* 489 */           result = new BigInteger(result.toString());
/*     */         } 
/* 491 */       } else if (Enum.class.isAssignableFrom(type)) {
/* 492 */         String enumValueName = node.getValue();
/*     */         try {
/* 494 */           result = Enum.valueOf(type, enumValueName);
/* 495 */         } catch (Exception ex) {
/* 496 */           throw new YAMLException("Unable to find enum value '" + enumValueName + "' for enum class: " + type.getName());
/*     */         }
/*     */       
/* 499 */       } else if (Calendar.class.isAssignableFrom(type)) {
/* 500 */         SafeConstructor.ConstructYamlTimestamp contr = new SafeConstructor.ConstructYamlTimestamp();
/* 501 */         contr.construct((Node)node);
/* 502 */         result = contr.getCalendar();
/* 503 */       } else if (Number.class.isAssignableFrom(type)) {
/*     */         
/* 505 */         SafeConstructor.ConstructYamlFloat contr = new SafeConstructor.ConstructYamlFloat(Constructor.this);
/* 506 */         result = contr.construct((Node)node);
/* 507 */       } else if (UUID.class == type) {
/* 508 */         result = UUID.fromString(node.getValue());
/*     */       }
/* 510 */       else if (Constructor.this.yamlConstructors.containsKey(node.getTag())) {
/* 511 */         result = ((Construct)Constructor.this.yamlConstructors.get(node.getTag())).construct((Node)node);
/*     */       } else {
/* 513 */         throw new YAMLException("Unsupported class: " + type);
/*     */       } 
/*     */       
/* 516 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class ConstructSequence
/*     */     implements Construct
/*     */   {
/*     */     public Object construct(Node node) {
/* 527 */       SequenceNode snode = (SequenceNode)node;
/* 528 */       if (Set.class.isAssignableFrom(node.getType())) {
/* 529 */         if (node.isTwoStepsConstruction()) {
/* 530 */           throw new YAMLException("Set cannot be recursive.");
/*     */         }
/* 532 */         return Constructor.this.constructSet(snode);
/*     */       } 
/* 534 */       if (Collection.class.isAssignableFrom(node.getType())) {
/* 535 */         if (node.isTwoStepsConstruction()) {
/* 536 */           return Constructor.this.newList(snode);
/*     */         }
/* 538 */         return Constructor.this.constructSequence(snode);
/*     */       } 
/* 540 */       if (node.getType().isArray()) {
/* 541 */         if (node.isTwoStepsConstruction()) {
/* 542 */           return Constructor.this.createArray(node.getType(), snode.getValue().size());
/*     */         }
/* 544 */         return Constructor.this.constructArray(snode);
/*     */       } 
/*     */ 
/*     */       
/* 548 */       List<Constructor<?>> possibleConstructors = new ArrayList<>(snode.getValue().size());
/*     */       
/* 550 */       for (Constructor<?> constructor : node.getType().getDeclaredConstructors()) {
/*     */         
/* 552 */         if (snode.getValue().size() == (constructor.getParameterTypes()).length) {
/* 553 */           possibleConstructors.add(constructor);
/*     */         }
/*     */       } 
/* 556 */       if (!possibleConstructors.isEmpty()) {
/* 557 */         if (possibleConstructors.size() == 1) {
/* 558 */           Object[] arrayOfObject = new Object[snode.getValue().size()];
/* 559 */           Constructor<?> c = possibleConstructors.get(0);
/* 560 */           int i = 0;
/* 561 */           for (Node argumentNode : snode.getValue()) {
/* 562 */             Class<?> type = c.getParameterTypes()[i];
/*     */             
/* 564 */             argumentNode.setType(type);
/* 565 */             arrayOfObject[i++] = Constructor.this.constructObject(argumentNode);
/*     */           } 
/*     */           
/*     */           try {
/* 569 */             c.setAccessible(true);
/* 570 */             return c.newInstance(arrayOfObject);
/* 571 */           } catch (Exception e) {
/* 572 */             throw new YAMLException(e);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 577 */         List<Object> argumentList = (List)Constructor.this.constructSequence(snode);
/* 578 */         Class<?>[] parameterTypes = new Class[argumentList.size()];
/* 579 */         int index = 0;
/* 580 */         for (Object parameter : argumentList) {
/* 581 */           parameterTypes[index] = parameter.getClass();
/* 582 */           index++;
/*     */         } 
/*     */         
/* 585 */         for (Constructor<?> c : possibleConstructors) {
/* 586 */           Class<?>[] argTypes = c.getParameterTypes();
/* 587 */           boolean foundConstructor = true;
/* 588 */           for (int i = 0; i < argTypes.length; i++) {
/* 589 */             if (!wrapIfPrimitive(argTypes[i]).isAssignableFrom(parameterTypes[i])) {
/* 590 */               foundConstructor = false;
/*     */               break;
/*     */             } 
/*     */           } 
/* 594 */           if (foundConstructor) {
/*     */             try {
/* 596 */               c.setAccessible(true);
/* 597 */               return c.newInstance(argumentList.toArray());
/* 598 */             } catch (Exception e) {
/* 599 */               throw new YAMLException(e);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/* 604 */       throw new YAMLException("No suitable constructor with " + String.valueOf(snode.getValue().size()) + " arguments found for " + node.getType());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Class<? extends Object> wrapIfPrimitive(Class<?> clazz) {
/* 612 */       if (!clazz.isPrimitive()) {
/* 613 */         return (Class)clazz;
/*     */       }
/* 615 */       if (clazz == int.class) {
/* 616 */         return (Class)Integer.class;
/*     */       }
/* 618 */       if (clazz == float.class) {
/* 619 */         return (Class)Float.class;
/*     */       }
/* 621 */       if (clazz == double.class) {
/* 622 */         return (Class)Double.class;
/*     */       }
/* 624 */       if (clazz == boolean.class) {
/* 625 */         return (Class)Boolean.class;
/*     */       }
/* 627 */       if (clazz == long.class) {
/* 628 */         return (Class)Long.class;
/*     */       }
/* 630 */       if (clazz == char.class) {
/* 631 */         return (Class)Character.class;
/*     */       }
/* 633 */       if (clazz == short.class) {
/* 634 */         return (Class)Short.class;
/*     */       }
/* 636 */       if (clazz == byte.class) {
/* 637 */         return (Class)Byte.class;
/*     */       }
/* 639 */       throw new YAMLException("Unexpected primitive " + clazz);
/*     */     }
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 644 */       SequenceNode snode = (SequenceNode)node;
/* 645 */       if (List.class.isAssignableFrom(node.getType())) {
/* 646 */         List<Object> list = (List<Object>)object;
/* 647 */         Constructor.this.constructSequenceStep2(snode, list);
/* 648 */       } else if (node.getType().isArray()) {
/* 649 */         Constructor.this.constructArrayStep2(snode, object);
/*     */       } else {
/* 651 */         throw new YAMLException("Immutable objects cannot be recursive.");
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected Class<?> getClassForNode(Node node) {
/* 657 */     Class<? extends Object> classForTag = this.typeTags.get(node.getTag());
/* 658 */     if (classForTag == null) {
/* 659 */       Class<?> cl; String name = node.getTag().getClassName();
/*     */       
/*     */       try {
/* 662 */         cl = getClassForName(name);
/* 663 */       } catch (ClassNotFoundException e) {
/* 664 */         throw new YAMLException("Class not found: " + name);
/*     */       } 
/* 666 */       this.typeTags.put(node.getTag(), cl);
/* 667 */       return cl;
/*     */     } 
/* 669 */     return classForTag;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Class<?> getClassForName(String name) throws ClassNotFoundException {
/*     */     try {
/* 675 */       return Class.forName(name, true, Thread.currentThread().getContextClassLoader());
/* 676 */     } catch (ClassNotFoundException e) {
/* 677 */       return Class.forName(name);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\constructor\Constructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */