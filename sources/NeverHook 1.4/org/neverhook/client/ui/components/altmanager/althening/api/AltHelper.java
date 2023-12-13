/*    */ package org.neverhook.client.ui.components.altmanager.althening.api;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ 
/*    */ public class AltHelper {
/*    */   private String className;
/*    */   private Class<?> clazz;
/*    */   
/*    */   public AltHelper(String v1) {
/*    */     try {
/* 11 */       this.clazz = Class.forName(v1);
/* 12 */     } catch (ClassNotFoundException v2) {
/* 13 */       v2.printStackTrace();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setStaticField(String a2, Object v1) throws NoSuchFieldException, IllegalAccessException {
/* 18 */     Field v2 = this.clazz.getDeclaredField(a2);
/* 19 */     v2.setAccessible(true);
/* 20 */     Field v3 = Field.class.getDeclaredField("modifiers");
/* 21 */     v3.setAccessible(true);
/* 22 */     v3.setInt(v2, v2.getModifiers() & 0xFFFFFFEF);
/* 23 */     v2.set(null, v1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\altmanager\althening\api\AltHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */