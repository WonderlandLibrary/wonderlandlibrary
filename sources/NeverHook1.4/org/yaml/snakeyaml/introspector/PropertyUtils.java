/*     */ package org.yaml.snakeyaml.introspector;
/*     */ 
/*     */ import java.beans.FeatureDescriptor;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.util.PlatformFeatureDetector;
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
/*     */ public class PropertyUtils
/*     */ {
/*  37 */   private final Map<Class<?>, Map<String, Property>> propertiesCache = new HashMap<>();
/*  38 */   private final Map<Class<?>, Set<Property>> readableProperties = new HashMap<>();
/*  39 */   private BeanAccess beanAccess = BeanAccess.DEFAULT;
/*     */   private boolean allowReadOnlyProperties = false;
/*     */   private boolean skipMissingProperties = false;
/*     */   private PlatformFeatureDetector platformFeatureDetector;
/*     */   private static final String TRANSIENT = "transient";
/*     */   
/*     */   public PropertyUtils() {
/*  46 */     this(new PlatformFeatureDetector());
/*     */   }
/*     */   
/*     */   PropertyUtils(PlatformFeatureDetector platformFeatureDetector) {
/*  50 */     this.platformFeatureDetector = platformFeatureDetector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  56 */     if (platformFeatureDetector.isRunningOnAndroid())
/*  57 */       this.beanAccess = BeanAccess.FIELD; 
/*     */   }
/*     */   
/*     */   protected Map<String, Property> getPropertiesMap(Class<?> type, BeanAccess bAccess) {
/*     */     Class<?> c;
/*  62 */     if (this.propertiesCache.containsKey(type)) {
/*  63 */       return this.propertiesCache.get(type);
/*     */     }
/*     */     
/*  66 */     Map<String, Property> properties = new LinkedHashMap<>();
/*  67 */     boolean inaccessableFieldsExist = false;
/*  68 */     switch (bAccess) {
/*     */       case FIELD:
/*  70 */         for (c = type; c != null; c = c.getSuperclass()) {
/*  71 */           for (Field field : c.getDeclaredFields()) {
/*  72 */             int modifiers = field.getModifiers();
/*  73 */             if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers) && !properties.containsKey(field.getName()))
/*     */             {
/*  75 */               properties.put(field.getName(), new FieldProperty(field));
/*     */             }
/*     */           } 
/*     */         } 
/*     */         break;
/*     */       
/*     */       default:
/*     */         try {
/*  83 */           for (PropertyDescriptor property : Introspector.getBeanInfo(type).getPropertyDescriptors()) {
/*     */             
/*  85 */             Method readMethod = property.getReadMethod();
/*  86 */             if ((readMethod == null || !readMethod.getName().equals("getClass")) && !isTransient(property))
/*     */             {
/*  88 */               properties.put(property.getName(), new MethodProperty(property));
/*     */             }
/*     */           } 
/*  91 */         } catch (IntrospectionException e) {
/*  92 */           throw new YAMLException(e);
/*     */         } 
/*     */ 
/*     */         
/*  96 */         for (c = type; c != null; c = c.getSuperclass()) {
/*  97 */           for (Field field : c.getDeclaredFields()) {
/*  98 */             int modifiers = field.getModifiers();
/*  99 */             if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
/* 100 */               if (Modifier.isPublic(modifiers)) {
/* 101 */                 properties.put(field.getName(), new FieldProperty(field));
/*     */               } else {
/* 103 */                 inaccessableFieldsExist = true;
/*     */               } 
/*     */             }
/*     */           } 
/*     */         } 
/*     */         break;
/*     */     } 
/* 110 */     if (properties.isEmpty() && inaccessableFieldsExist) {
/* 111 */       throw new YAMLException("No JavaBean properties found in " + type.getName());
/*     */     }
/* 113 */     this.propertiesCache.put(type, properties);
/* 114 */     return properties;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isTransient(FeatureDescriptor fd) {
/* 120 */     return Boolean.TRUE.equals(fd.getValue("transient"));
/*     */   }
/*     */   
/*     */   public Set<Property> getProperties(Class<? extends Object> type) {
/* 124 */     return getProperties(type, this.beanAccess);
/*     */   }
/*     */   
/*     */   public Set<Property> getProperties(Class<? extends Object> type, BeanAccess bAccess) {
/* 128 */     if (this.readableProperties.containsKey(type)) {
/* 129 */       return this.readableProperties.get(type);
/*     */     }
/* 131 */     Set<Property> properties = createPropertySet(type, bAccess);
/* 132 */     this.readableProperties.put(type, properties);
/* 133 */     return properties;
/*     */   }
/*     */   
/*     */   protected Set<Property> createPropertySet(Class<? extends Object> type, BeanAccess bAccess) {
/* 137 */     Set<Property> properties = new TreeSet<>();
/* 138 */     Collection<Property> props = getPropertiesMap(type, bAccess).values();
/* 139 */     for (Property property : props) {
/* 140 */       if (property.isReadable() && (this.allowReadOnlyProperties || property.isWritable())) {
/* 141 */         properties.add(property);
/*     */       }
/*     */     } 
/* 144 */     return properties;
/*     */   }
/*     */   
/*     */   public Property getProperty(Class<? extends Object> type, String name) {
/* 148 */     return getProperty(type, name, this.beanAccess);
/*     */   }
/*     */   
/*     */   public Property getProperty(Class<? extends Object> type, String name, BeanAccess bAccess) {
/* 152 */     Map<String, Property> properties = getPropertiesMap(type, bAccess);
/* 153 */     Property property = properties.get(name);
/* 154 */     if (property == null && this.skipMissingProperties) {
/* 155 */       property = new MissingProperty(name);
/*     */     }
/* 157 */     if (property == null) {
/* 158 */       throw new YAMLException("Unable to find property '" + name + "' on class: " + type.getName());
/*     */     }
/*     */     
/* 161 */     return property;
/*     */   }
/*     */   
/*     */   public void setBeanAccess(BeanAccess beanAccess) {
/* 165 */     if (this.platformFeatureDetector.isRunningOnAndroid() && beanAccess != BeanAccess.FIELD) {
/* 166 */       throw new IllegalArgumentException("JVM is Android - only BeanAccess.FIELD is available");
/*     */     }
/*     */ 
/*     */     
/* 170 */     if (this.beanAccess != beanAccess) {
/* 171 */       this.beanAccess = beanAccess;
/* 172 */       this.propertiesCache.clear();
/* 173 */       this.readableProperties.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setAllowReadOnlyProperties(boolean allowReadOnlyProperties) {
/* 178 */     if (this.allowReadOnlyProperties != allowReadOnlyProperties) {
/* 179 */       this.allowReadOnlyProperties = allowReadOnlyProperties;
/* 180 */       this.readableProperties.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isAllowReadOnlyProperties() {
/* 185 */     return this.allowReadOnlyProperties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSkipMissingProperties(boolean skipMissingProperties) {
/* 196 */     if (this.skipMissingProperties != skipMissingProperties) {
/* 197 */       this.skipMissingProperties = skipMissingProperties;
/* 198 */       this.readableProperties.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isSkipMissingProperties() {
/* 203 */     return this.skipMissingProperties;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\introspector\PropertyUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */