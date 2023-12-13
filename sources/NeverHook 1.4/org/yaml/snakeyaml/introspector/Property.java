/*     */ package org.yaml.snakeyaml.introspector;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.List;
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
/*     */ 
/*     */ public abstract class Property
/*     */   implements Comparable<Property>
/*     */ {
/*     */   private final String name;
/*     */   private final Class<?> type;
/*     */   
/*     */   public Property(String name, Class<?> type) {
/*  40 */     this.name = name;
/*  41 */     this.type = type;
/*     */   }
/*     */   
/*     */   public Class<?> getType() {
/*  45 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  51 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  56 */     return getName() + " of " + getType();
/*     */   }
/*     */   
/*     */   public int compareTo(Property o) {
/*  60 */     return getName().compareTo(o.getName());
/*     */   }
/*     */   
/*     */   public boolean isWritable() {
/*  64 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isReadable() {
/*  68 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  94 */     return getName().hashCode() + getType().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/*  99 */     if (other instanceof Property) {
/* 100 */       Property p = (Property)other;
/* 101 */       return (getName().equals(p.getName()) && getType().equals(p.getType()));
/*     */     } 
/* 103 */     return false;
/*     */   }
/*     */   
/*     */   public abstract Class<?>[] getActualTypeArguments();
/*     */   
/*     */   public abstract void set(Object paramObject1, Object paramObject2) throws Exception;
/*     */   
/*     */   public abstract Object get(Object paramObject);
/*     */   
/*     */   public abstract List<Annotation> getAnnotations();
/*     */   
/*     */   public abstract <A extends Annotation> A getAnnotation(Class<A> paramClass);
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\introspector\Property.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */