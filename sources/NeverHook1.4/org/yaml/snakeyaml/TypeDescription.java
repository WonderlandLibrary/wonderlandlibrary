/*     */ package org.yaml.snakeyaml;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.introspector.BeanAccess;
/*     */ import org.yaml.snakeyaml.introspector.Property;
/*     */ import org.yaml.snakeyaml.introspector.PropertySubstitute;
/*     */ import org.yaml.snakeyaml.introspector.PropertyUtils;
/*     */ import org.yaml.snakeyaml.nodes.Node;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeDescription
/*     */ {
/*  48 */   private static final Logger log = Logger.getLogger(TypeDescription.class.getPackage().getName());
/*     */ 
/*     */   
/*     */   private final Class<? extends Object> type;
/*     */   
/*     */   private Class<?> impl;
/*     */   
/*     */   private Tag tag;
/*     */   
/*     */   private transient Set<Property> dumpProperties;
/*     */   
/*     */   private transient PropertyUtils propertyUtils;
/*     */   
/*     */   private transient boolean delegatesChecked;
/*     */   
/*  63 */   private Map<String, PropertySubstitute> properties = Collections.emptyMap();
/*     */   
/*  65 */   protected Set<String> excludes = Collections.emptySet();
/*  66 */   protected String[] includes = null;
/*     */   protected BeanAccess beanAccess;
/*     */   
/*     */   public TypeDescription(Class<? extends Object> clazz, Tag tag) {
/*  70 */     this(clazz, tag, null);
/*     */   }
/*     */   
/*     */   public TypeDescription(Class<? extends Object> clazz, Tag tag, Class<?> impl) {
/*  74 */     this.type = clazz;
/*  75 */     this.tag = tag;
/*  76 */     this.impl = impl;
/*  77 */     this.beanAccess = null;
/*     */   }
/*     */   
/*     */   public TypeDescription(Class<? extends Object> clazz, String tag) {
/*  81 */     this(clazz, new Tag(tag), null);
/*     */   }
/*     */   
/*     */   public TypeDescription(Class<? extends Object> clazz) {
/*  85 */     this(clazz, (Tag)null, null);
/*     */   }
/*     */   
/*     */   public TypeDescription(Class<? extends Object> clazz, Class<?> impl) {
/*  89 */     this(clazz, null, impl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tag getTag() {
/*  99 */     return this.tag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setTag(Tag tag) {
/* 110 */     this.tag = tag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setTag(String tag) {
/* 121 */     setTag(new Tag(tag));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<? extends Object> getType() {
/* 130 */     return this.type;
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
/*     */   @Deprecated
/*     */   public void putListPropertyType(String property, Class<? extends Object> type) {
/* 143 */     addPropertyParameters(property, new Class[] { type });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Class<? extends Object> getListPropertyType(String property) {
/* 155 */     if (this.properties.containsKey(property)) {
/* 156 */       Class<?>[] typeArguments = ((PropertySubstitute)this.properties.get(property)).getActualTypeArguments();
/* 157 */       if (typeArguments != null && typeArguments.length > 0) {
/* 158 */         return (Class)typeArguments[0];
/*     */       }
/*     */     } 
/* 161 */     return null;
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
/*     */   @Deprecated
/*     */   public void putMapPropertyType(String property, Class<? extends Object> key, Class<? extends Object> value) {
/* 177 */     addPropertyParameters(property, new Class[] { key, value });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Class<? extends Object> getMapKeyType(String property) {
/* 189 */     if (this.properties.containsKey(property)) {
/* 190 */       Class<?>[] typeArguments = ((PropertySubstitute)this.properties.get(property)).getActualTypeArguments();
/* 191 */       if (typeArguments != null && typeArguments.length > 0) {
/* 192 */         return (Class)typeArguments[0];
/*     */       }
/*     */     } 
/* 195 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Class<? extends Object> getMapValueType(String property) {
/* 207 */     if (this.properties.containsKey(property)) {
/* 208 */       Class<?>[] typeArguments = ((PropertySubstitute)this.properties.get(property)).getActualTypeArguments();
/* 209 */       if (typeArguments != null && typeArguments.length > 1) {
/* 210 */         return (Class)typeArguments[1];
/*     */       }
/*     */     } 
/* 213 */     return null;
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
/*     */   public void addPropertyParameters(String pName, Class<?>... classes) {
/* 226 */     if (!this.properties.containsKey(pName)) {
/* 227 */       substituteProperty(pName, null, null, null, classes);
/*     */     } else {
/* 229 */       PropertySubstitute pr = this.properties.get(pName);
/* 230 */       pr.setActualTypeArguments(classes);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 237 */     return "TypeDescription for " + getType() + " (tag='" + getTag() + "')";
/*     */   }
/*     */   
/*     */   private void checkDelegates() {
/* 241 */     Collection<PropertySubstitute> values = this.properties.values();
/* 242 */     for (PropertySubstitute p : values) {
/*     */       try {
/* 244 */         p.setDelegate(discoverProperty(p.getName()));
/* 245 */       } catch (YAMLException e) {}
/*     */     } 
/*     */     
/* 248 */     this.delegatesChecked = true;
/*     */   }
/*     */   
/*     */   private Property discoverProperty(String name) {
/* 252 */     if (this.propertyUtils != null) {
/* 253 */       if (this.beanAccess == null) {
/* 254 */         return this.propertyUtils.getProperty(this.type, name);
/*     */       }
/* 256 */       return this.propertyUtils.getProperty(this.type, name, this.beanAccess);
/*     */     } 
/* 258 */     return null;
/*     */   }
/*     */   
/*     */   public Property getProperty(String name) {
/* 262 */     if (!this.delegatesChecked) {
/* 263 */       checkDelegates();
/*     */     }
/* 265 */     return this.properties.containsKey(name) ? (Property)this.properties.get(name) : discoverProperty(name);
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
/*     */   
/*     */   public void substituteProperty(String pName, Class<?> pType, String getter, String setter, Class<?>... argParams) {
/* 284 */     substituteProperty(new PropertySubstitute(pName, pType, getter, setter, argParams));
/*     */   }
/*     */   
/*     */   public void substituteProperty(PropertySubstitute substitute) {
/* 288 */     if (Collections.EMPTY_MAP == this.properties) {
/* 289 */       this.properties = new LinkedHashMap<>();
/*     */     }
/* 291 */     substitute.setTargetType(this.type);
/* 292 */     this.properties.put(substitute.getName(), substitute);
/*     */   }
/*     */   
/*     */   public void setPropertyUtils(PropertyUtils propertyUtils) {
/* 296 */     this.propertyUtils = propertyUtils;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIncludes(String... propNames) {
/* 301 */     this.includes = (propNames != null && propNames.length > 0) ? propNames : null;
/*     */   }
/*     */   
/*     */   public void setExcludes(String... propNames) {
/* 305 */     if (propNames != null && propNames.length > 0) {
/* 306 */       this.excludes = new HashSet<>();
/* 307 */       for (String name : propNames) {
/* 308 */         this.excludes.add(name);
/*     */       }
/*     */     } else {
/* 311 */       this.excludes = Collections.emptySet();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Set<Property> getProperties() {
/* 316 */     if (this.dumpProperties != null) {
/* 317 */       return this.dumpProperties;
/*     */     }
/*     */     
/* 320 */     if (this.propertyUtils != null) {
/* 321 */       if (this.includes != null) {
/* 322 */         this.dumpProperties = new LinkedHashSet<>();
/* 323 */         for (String propertyName : this.includes) {
/* 324 */           if (!this.excludes.contains(propertyName)) {
/* 325 */             this.dumpProperties.add(getProperty(propertyName));
/*     */           }
/*     */         } 
/* 328 */         return this.dumpProperties;
/*     */       } 
/*     */       
/* 331 */       Set<Property> readableProps = (this.beanAccess == null) ? this.propertyUtils.getProperties(this.type) : this.propertyUtils.getProperties(this.type, this.beanAccess);
/*     */ 
/*     */ 
/*     */       
/* 335 */       if (this.properties.isEmpty()) {
/* 336 */         if (this.excludes.isEmpty()) {
/* 337 */           return this.dumpProperties = readableProps;
/*     */         }
/* 339 */         this.dumpProperties = new LinkedHashSet<>();
/* 340 */         for (Property property : readableProps) {
/* 341 */           if (!this.excludes.contains(property.getName())) {
/* 342 */             this.dumpProperties.add(property);
/*     */           }
/*     */         } 
/* 345 */         return this.dumpProperties;
/*     */       } 
/*     */       
/* 348 */       if (!this.delegatesChecked) {
/* 349 */         checkDelegates();
/*     */       }
/*     */       
/* 352 */       this.dumpProperties = new LinkedHashSet<>();
/*     */       
/* 354 */       for (Property property : this.properties.values()) {
/* 355 */         if (!this.excludes.contains(property.getName()) && property.isReadable()) {
/* 356 */           this.dumpProperties.add(property);
/*     */         }
/*     */       } 
/*     */       
/* 360 */       for (Property property : readableProps) {
/* 361 */         if (!this.excludes.contains(property.getName())) {
/* 362 */           this.dumpProperties.add(property);
/*     */         }
/*     */       } 
/*     */       
/* 366 */       return this.dumpProperties;
/*     */     } 
/* 368 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setupPropertyType(String key, Node valueNode) {
/* 376 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setProperty(Object targetBean, String propertyName, Object value) throws Exception {
/* 381 */     return false;
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
/*     */   public Object newInstance(Node node) {
/* 393 */     if (this.impl != null) {
/*     */       try {
/* 395 */         Constructor<?> c = this.impl.getDeclaredConstructor(new Class[0]);
/* 396 */         c.setAccessible(true);
/* 397 */         return c.newInstance(new Object[0]);
/* 398 */       } catch (Exception e) {
/* 399 */         log.fine(e.getLocalizedMessage());
/* 400 */         this.impl = null;
/*     */       } 
/*     */     }
/* 403 */     return null;
/*     */   }
/*     */   
/*     */   public Object newInstance(String propertyName, Node node) {
/* 407 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object finalizeConstruction(Object obj) {
/* 416 */     return obj;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\TypeDescription.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */