/*    */ package org.neverhook.client.helpers.math;
/*    */ 
/*    */ import org.neverhook.client.helpers.Helper;
/*    */ 
/*    */ public class GCDCalcHelper
/*    */   implements Helper {
/*    */   public static float getFixedRotation(float rot) {
/*  8 */     return getDeltaMouse(rot) * getGCDValue();
/*    */   }
/*    */   
/*    */   public static float getGCDValue() {
/* 12 */     return (float)(getGCD() * 0.15D);
/*    */   }
/*    */   
/*    */   public static float getGCD() {
/*    */     float f1;
/* 17 */     return (f1 = (float)(mc.gameSettings.mouseSensitivity * 0.6D + 0.2D)) * f1 * f1 * 8.0F;
/*    */   }
/*    */   
/*    */   public static float getDeltaMouse(float delta) {
/* 21 */     return Math.round(delta / getGCDValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\math\GCDCalcHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */