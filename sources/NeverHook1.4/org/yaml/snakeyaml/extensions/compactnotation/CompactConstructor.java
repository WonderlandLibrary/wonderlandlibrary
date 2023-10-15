/*     */ package org.yaml.snakeyaml.extensions.compactnotation;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.constructor.Construct;
/*     */ import org.yaml.snakeyaml.constructor.Constructor;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.introspector.Property;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
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
/*     */ public class CompactConstructor
/*     */   extends Constructor
/*     */ {
/*  40 */   private static final Pattern GUESS_COMPACT = Pattern.compile("\\p{Alpha}.*\\s*\\((?:,?\\s*(?:(?:\\w*)|(?:\\p{Alpha}\\w*\\s*=.+))\\s*)+\\)");
/*     */   
/*  42 */   private static final Pattern FIRST_PATTERN = Pattern.compile("(\\p{Alpha}.*)(\\s*)\\((.*?)\\)");
/*  43 */   private static final Pattern PROPERTY_NAME_PATTERN = Pattern.compile("\\s*(\\p{Alpha}\\w*)\\s*=(.+)");
/*     */   
/*     */   private Construct compactConstruct;
/*     */   
/*     */   protected Object constructCompactFormat(ScalarNode node, CompactData data) {
/*     */     try {
/*  49 */       Object obj = createInstance(node, data);
/*  50 */       Map<String, Object> properties = new HashMap<>(data.getProperties());
/*  51 */       setProperties(obj, properties);
/*  52 */       return obj;
/*  53 */     } catch (Exception e) {
/*  54 */       throw new YAMLException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Object createInstance(ScalarNode node, CompactData data) throws Exception {
/*  59 */     Class<?> clazz = getClassForName(data.getPrefix());
/*  60 */     Class<?>[] args = new Class[data.getArguments().size()];
/*  61 */     for (int i = 0; i < args.length; i++)
/*     */     {
/*  63 */       args[i] = String.class;
/*     */     }
/*  65 */     Constructor<?> c = clazz.getDeclaredConstructor(args);
/*  66 */     c.setAccessible(true);
/*  67 */     return c.newInstance(data.getArguments().toArray());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setProperties(Object bean, Map<String, Object> data) throws Exception {
/*  72 */     if (data == null) {
/*  73 */       throw new NullPointerException("Data for Compact Object Notation cannot be null.");
/*     */     }
/*  75 */     for (Map.Entry<String, Object> entry : data.entrySet()) {
/*  76 */       String key = entry.getKey();
/*  77 */       Property property = getPropertyUtils().getProperty(bean.getClass(), key);
/*     */       try {
/*  79 */         property.set(bean, entry.getValue());
/*  80 */       } catch (IllegalArgumentException e) {
/*  81 */         throw new YAMLException("Cannot set property='" + key + "' with value='" + data.get(key) + "' (" + data.get(key).getClass() + ") in " + bean);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public CompactData getCompactData(String scalar) {
/*  88 */     if (!scalar.endsWith(")")) {
/*  89 */       return null;
/*     */     }
/*  91 */     if (scalar.indexOf('(') < 0) {
/*  92 */       return null;
/*     */     }
/*  94 */     Matcher m = FIRST_PATTERN.matcher(scalar);
/*  95 */     if (m.matches()) {
/*  96 */       String tag = m.group(1).trim();
/*  97 */       String content = m.group(3);
/*  98 */       CompactData data = new CompactData(tag);
/*  99 */       if (content.length() == 0)
/* 100 */         return data; 
/* 101 */       String[] names = content.split("\\s*,\\s*");
/* 102 */       for (int i = 0; i < names.length; i++) {
/* 103 */         String section = names[i];
/* 104 */         if (section.indexOf('=') < 0) {
/* 105 */           data.getArguments().add(section);
/*     */         } else {
/* 107 */           Matcher sm = PROPERTY_NAME_PATTERN.matcher(section);
/* 108 */           if (sm.matches()) {
/* 109 */             String name = sm.group(1);
/* 110 */             String value = sm.group(2).trim();
/* 111 */             data.getProperties().put(name, value);
/*     */           } else {
/* 113 */             return null;
/*     */           } 
/*     */         } 
/*     */       } 
/* 117 */       return data;
/*     */     } 
/* 119 */     return null;
/*     */   }
/*     */   
/*     */   private Construct getCompactConstruct() {
/* 123 */     if (this.compactConstruct == null) {
/* 124 */       this.compactConstruct = createCompactConstruct();
/*     */     }
/* 126 */     return this.compactConstruct;
/*     */   }
/*     */   
/*     */   protected Construct createCompactConstruct() {
/* 130 */     return (Construct)new ConstructCompactObject();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Construct getConstructor(Node node) {
/* 135 */     if (node instanceof MappingNode) {
/* 136 */       MappingNode mnode = (MappingNode)node;
/* 137 */       List<NodeTuple> list = mnode.getValue();
/* 138 */       if (list.size() == 1) {
/* 139 */         NodeTuple tuple = list.get(0);
/* 140 */         Node key = tuple.getKeyNode();
/* 141 */         if (key instanceof ScalarNode) {
/* 142 */           ScalarNode scalar = (ScalarNode)key;
/* 143 */           if (GUESS_COMPACT.matcher(scalar.getValue()).matches()) {
/* 144 */             return getCompactConstruct();
/*     */           }
/*     */         } 
/*     */       } 
/* 148 */     } else if (node instanceof ScalarNode) {
/* 149 */       ScalarNode scalar = (ScalarNode)node;
/* 150 */       if (GUESS_COMPACT.matcher(scalar.getValue()).matches()) {
/* 151 */         return getCompactConstruct();
/*     */       }
/*     */     } 
/* 154 */     return super.getConstructor(node);
/*     */   }
/*     */   public class ConstructCompactObject extends Constructor.ConstructMapping { public ConstructCompactObject() {
/* 157 */       super(CompactConstructor.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 162 */       MappingNode mnode = (MappingNode)node;
/* 163 */       NodeTuple nodeTuple = mnode.getValue().iterator().next();
/*     */       
/* 165 */       Node valueNode = nodeTuple.getValueNode();
/*     */       
/* 167 */       if (valueNode instanceof MappingNode) {
/* 168 */         valueNode.setType(object.getClass());
/* 169 */         constructJavaBean2ndStep((MappingNode)valueNode, object);
/*     */       } else {
/*     */         
/* 172 */         CompactConstructor.this.applySequence(object, CompactConstructor.this.constructSequence((SequenceNode)valueNode));
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object construct(Node node) {
/*     */       ScalarNode tmpNode;
/* 182 */       if (node instanceof MappingNode) {
/*     */         
/* 184 */         MappingNode mnode = (MappingNode)node;
/* 185 */         NodeTuple nodeTuple = mnode.getValue().iterator().next();
/* 186 */         node.setTwoStepsConstruction(true);
/* 187 */         tmpNode = (ScalarNode)nodeTuple.getKeyNode();
/*     */       } else {
/*     */         
/* 190 */         tmpNode = (ScalarNode)node;
/*     */       } 
/*     */       
/* 193 */       CompactData data = CompactConstructor.this.getCompactData(tmpNode.getValue());
/* 194 */       if (data == null) {
/* 195 */         return CompactConstructor.this.constructScalar(tmpNode);
/*     */       }
/* 197 */       return CompactConstructor.this.constructCompactFormat(tmpNode, data);
/*     */     } }
/*     */ 
/*     */   
/*     */   protected void applySequence(Object bean, List<?> value) {
/*     */     try {
/* 203 */       Property property = getPropertyUtils().getProperty(bean.getClass(), getSequencePropertyName(bean.getClass()));
/*     */       
/* 205 */       property.set(bean, value);
/* 206 */     } catch (Exception e) {
/* 207 */       throw new YAMLException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getSequencePropertyName(Class<?> bean) {
/* 218 */     Set<Property> properties = getPropertyUtils().getProperties(bean);
/* 219 */     for (Iterator<Property> iterator = properties.iterator(); iterator.hasNext(); ) {
/* 220 */       Property property = iterator.next();
/* 221 */       if (!List.class.isAssignableFrom(property.getType())) {
/* 222 */         iterator.remove();
/*     */       }
/*     */     } 
/* 225 */     if (properties.size() == 0)
/* 226 */       throw new YAMLException("No list property found in " + bean); 
/* 227 */     if (properties.size() > 1) {
/* 228 */       throw new YAMLException("Many list properties found in " + bean + "; Please override getSequencePropertyName() to specify which property to use.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 233 */     return ((Property)properties.iterator().next()).getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\extensions\compactnotation\CompactConstructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */