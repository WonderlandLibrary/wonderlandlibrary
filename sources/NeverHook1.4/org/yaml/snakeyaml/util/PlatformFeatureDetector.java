/*    */ package org.yaml.snakeyaml.util;
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
/*    */ public class PlatformFeatureDetector
/*    */ {
/* 20 */   private Boolean isRunningOnAndroid = null;
/*    */   
/*    */   public boolean isRunningOnAndroid() {
/* 23 */     if (this.isRunningOnAndroid == null) {
/* 24 */       String name = System.getProperty("java.runtime.name");
/* 25 */       this.isRunningOnAndroid = Boolean.valueOf((name != null && name.startsWith("Android Runtime")));
/*    */     } 
/* 27 */     return this.isRunningOnAndroid.booleanValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyam\\util\PlatformFeatureDetector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */