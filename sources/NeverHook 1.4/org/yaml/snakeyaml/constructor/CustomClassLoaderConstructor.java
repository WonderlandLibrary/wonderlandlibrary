/*    */ package org.yaml.snakeyaml.constructor;
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
/*    */ public class CustomClassLoaderConstructor
/*    */   extends Constructor
/*    */ {
/* 22 */   private ClassLoader loader = CustomClassLoaderConstructor.class.getClassLoader();
/*    */   
/*    */   public CustomClassLoaderConstructor(ClassLoader cLoader) {
/* 25 */     this(Object.class, cLoader);
/*    */   }
/*    */   
/*    */   public CustomClassLoaderConstructor(Class<? extends Object> theRoot, ClassLoader theLoader) {
/* 29 */     super(theRoot);
/* 30 */     if (theLoader == null) {
/* 31 */       throw new NullPointerException("Loader must be provided.");
/*    */     }
/* 33 */     this.loader = theLoader;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Class<?> getClassForName(String name) throws ClassNotFoundException {
/* 38 */     return Class.forName(name, true, this.loader);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\constructor\CustomClassLoaderConstructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */