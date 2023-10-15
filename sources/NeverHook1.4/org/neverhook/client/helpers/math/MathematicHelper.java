/*    */ package org.neverhook.client.helpers.math;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ import java.math.RoundingMode;
/*    */ import net.minecraft.util.math.MathHelper;
/*    */ import org.neverhook.client.helpers.Helper;
/*    */ 
/*    */ public class MathematicHelper
/*    */   implements Helper
/*    */ {
/*    */   public static BigDecimal round(float f, int times) {
/* 12 */     BigDecimal bd = new BigDecimal(Float.toString(f));
/* 13 */     bd = bd.setScale(times, RoundingMode.HALF_UP);
/* 14 */     return bd;
/*    */   }
/*    */   
/*    */   public static int getMiddle(int old, int newValue) {
/* 18 */     return (old + newValue) / 2;
/*    */   }
/*    */   
/*    */   public static double round(double num, double increment) {
/* 22 */     double v = Math.round(num / increment) * increment;
/* 23 */     BigDecimal bd = new BigDecimal(v);
/* 24 */     bd = bd.setScale(2, RoundingMode.HALF_UP);
/* 25 */     return bd.doubleValue();
/*    */   }
/*    */   
/*    */   public static float checkAngle(float one, float two, float three) {
/* 29 */     float f = MathHelper.wrapDegrees(one - two);
/* 30 */     if (f < -three) {
/* 31 */       f = -three;
/*    */     }
/* 33 */     if (f >= three) {
/* 34 */       f = three;
/*    */     }
/* 36 */     return one - f;
/*    */   }
/*    */   
/*    */   public static float clamp(float val, float min, float max) {
/* 40 */     if (val <= min) {
/* 41 */       val = min;
/*    */     }
/* 43 */     if (val >= max) {
/* 44 */       val = max;
/*    */     }
/* 46 */     return val;
/*    */   }
/*    */   
/*    */   public static float randomizeFloat(float min, float max) {
/* 50 */     return (float)(min + (max - min) * Math.random());
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\math\MathematicHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */