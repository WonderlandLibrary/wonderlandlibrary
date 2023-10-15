/*    */ package org.yaml.snakeyaml.introspector;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.Field;
/*    */ import java.util.List;
/*    */ import org.yaml.snakeyaml.error.YAMLException;
/*    */ import org.yaml.snakeyaml.util.ArrayUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FieldProperty
/*    */   extends GenericProperty
/*    */ {
/*    */   private final Field field;
/*    */   
/*    */   public FieldProperty(Field field) {
/* 37 */     super(field.getName(), field.getType(), field.getGenericType());
/* 38 */     this.field = field;
/* 39 */     field.setAccessible(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(Object object, Object value) throws Exception {
/* 44 */     this.field.set(object, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object get(Object object) {
/*    */     try {
/* 50 */       return this.field.get(object);
/* 51 */     } catch (Exception e) {
/* 52 */       throw new YAMLException("Unable to access field " + this.field.getName() + " on object " + object + " : " + e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<Annotation> getAnnotations() {
/* 59 */     return ArrayUtils.toUnmodifiableList((Object[])this.field.getAnnotations());
/*    */   }
/*    */ 
/*    */   
/*    */   public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
/* 64 */     return this.field.getAnnotation(annotationType);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\introspector\FieldProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */