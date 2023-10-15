/*    */ package org.yaml.snakeyaml.introspector;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ import java.lang.reflect.GenericArrayType;
/*    */ import java.lang.reflect.ParameterizedType;
/*    */ import java.lang.reflect.Type;
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
/*    */ public abstract class GenericProperty
/*    */   extends Property
/*    */ {
/*    */   private Type genType;
/*    */   private boolean actualClassesChecked;
/*    */   private Class<?>[] actualClasses;
/*    */   
/*    */   public GenericProperty(String name, Class<?> aClass, Type aType) {
/* 28 */     super(name, aClass);
/* 29 */     this.genType = aType;
/* 30 */     this.actualClassesChecked = (aType == null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<?>[] getActualTypeArguments() {
/* 37 */     if (!this.actualClassesChecked) {
/* 38 */       if (this.genType instanceof ParameterizedType) {
/* 39 */         ParameterizedType parameterizedType = (ParameterizedType)this.genType;
/* 40 */         Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
/* 41 */         if (actualTypeArguments.length > 0) {
/* 42 */           this.actualClasses = new Class[actualTypeArguments.length];
/* 43 */           for (int i = 0; i < actualTypeArguments.length; i++) {
/* 44 */             if (actualTypeArguments[i] instanceof Class) {
/* 45 */               this.actualClasses[i] = (Class)actualTypeArguments[i];
/* 46 */             } else if (actualTypeArguments[i] instanceof ParameterizedType) {
/* 47 */               this.actualClasses[i] = (Class)((ParameterizedType)actualTypeArguments[i]).getRawType();
/*    */             }
/* 49 */             else if (actualTypeArguments[i] instanceof GenericArrayType) {
/* 50 */               Type componentType = ((GenericArrayType)actualTypeArguments[i]).getGenericComponentType();
/*    */               
/* 52 */               if (componentType instanceof Class) {
/* 53 */                 this.actualClasses[i] = Array.newInstance((Class)componentType, 0).getClass();
/*    */               } else {
/*    */                 
/* 56 */                 this.actualClasses = null;
/*    */                 break;
/*    */               } 
/*    */             } else {
/* 60 */               this.actualClasses = null;
/*    */               break;
/*    */             } 
/*    */           } 
/*    */         } 
/* 65 */       } else if (this.genType instanceof GenericArrayType) {
/* 66 */         Type componentType = ((GenericArrayType)this.genType).getGenericComponentType();
/* 67 */         if (componentType instanceof Class) {
/* 68 */           this.actualClasses = new Class[] { (Class)componentType };
/*    */         }
/* 70 */       } else if (this.genType instanceof Class) {
/*    */         
/* 72 */         Class<?> classType = (Class)this.genType;
/* 73 */         if (classType.isArray()) {
/* 74 */           this.actualClasses = new Class[1];
/* 75 */           this.actualClasses[0] = getType().getComponentType();
/*    */         } 
/*    */       } 
/* 78 */       this.actualClassesChecked = true;
/*    */     } 
/* 80 */     return this.actualClasses;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\introspector\GenericProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */