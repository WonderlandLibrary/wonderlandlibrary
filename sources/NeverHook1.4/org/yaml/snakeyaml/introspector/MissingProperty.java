/*    */ package org.yaml.snakeyaml.introspector;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MissingProperty
/*    */   extends Property
/*    */ {
/*    */   public MissingProperty(String name) {
/* 29 */     super(name, Object.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<?>[] getActualTypeArguments() {
/* 34 */     return new Class[0];
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void set(Object object, Object value) throws Exception {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object get(Object object) {
/* 46 */     return object;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<Annotation> getAnnotations() {
/* 51 */     return Collections.emptyList();
/*    */   }
/*    */ 
/*    */   
/*    */   public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
/* 56 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\introspector\MissingProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */