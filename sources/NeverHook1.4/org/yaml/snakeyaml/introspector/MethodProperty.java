/*     */ package org.yaml.snakeyaml.introspector;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.List;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.util.ArrayUtils;
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
/*     */ public class MethodProperty
/*     */   extends GenericProperty
/*     */ {
/*     */   private final PropertyDescriptor property;
/*     */   private final boolean readable;
/*     */   private final boolean writable;
/*     */   
/*     */   private static Type discoverGenericType(PropertyDescriptor property) {
/*  43 */     Method readMethod = property.getReadMethod();
/*  44 */     if (readMethod != null) {
/*  45 */       return readMethod.getGenericReturnType();
/*     */     }
/*     */     
/*  48 */     Method writeMethod = property.getWriteMethod();
/*  49 */     if (writeMethod != null) {
/*  50 */       Type[] paramTypes = writeMethod.getGenericParameterTypes();
/*  51 */       if (paramTypes.length > 0) {
/*  52 */         return paramTypes[0];
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     return null;
/*     */   }
/*     */   
/*     */   public MethodProperty(PropertyDescriptor property) {
/*  64 */     super(property.getName(), property.getPropertyType(), discoverGenericType(property));
/*     */     
/*  66 */     this.property = property;
/*  67 */     this.readable = (property.getReadMethod() != null);
/*  68 */     this.writable = (property.getWriteMethod() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(Object object, Object value) throws Exception {
/*  73 */     if (!this.writable) {
/*  74 */       throw new YAMLException("No writable property '" + getName() + "' on class: " + object.getClass().getName());
/*     */     }
/*     */     
/*  77 */     this.property.getWriteMethod().invoke(object, new Object[] { value });
/*     */   }
/*     */ 
/*     */   
/*     */   public Object get(Object object) {
/*     */     try {
/*  83 */       this.property.getReadMethod().setAccessible(true);
/*  84 */       return this.property.getReadMethod().invoke(object, new Object[0]);
/*  85 */     } catch (Exception e) {
/*  86 */       throw new YAMLException("Unable to find getter for property '" + this.property.getName() + "' on object " + object + ":" + e);
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
/*     */   public List<Annotation> getAnnotations() {
/*     */     List<Annotation> annotations;
/* 100 */     if (isReadable() && isWritable()) {
/* 101 */       annotations = ArrayUtils.toUnmodifiableCompositeList((Object[])this.property.getReadMethod().getAnnotations(), (Object[])this.property.getWriteMethod().getAnnotations());
/* 102 */     } else if (isReadable()) {
/* 103 */       annotations = ArrayUtils.toUnmodifiableList((Object[])this.property.getReadMethod().getAnnotations());
/*     */     } else {
/* 105 */       annotations = ArrayUtils.toUnmodifiableList((Object[])this.property.getWriteMethod().getAnnotations());
/*     */     } 
/* 107 */     return annotations;
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
/*     */   public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
/* 119 */     A annotation = null;
/* 120 */     if (isReadable()) {
/* 121 */       annotation = this.property.getReadMethod().getAnnotation(annotationType);
/*     */     }
/* 123 */     if (annotation == null && isWritable()) {
/* 124 */       annotation = this.property.getWriteMethod().getAnnotation(annotationType);
/*     */     }
/* 126 */     return annotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable() {
/* 131 */     return this.writable;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReadable() {
/* 136 */     return this.readable;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\introspector\MethodProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */