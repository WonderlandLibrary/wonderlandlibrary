/*    */ package org.neverhook.client.helpers.render;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ public class AnimationHelper
/*    */ {
/*    */   public static float animation(float animation, float target, float speedTarget) {
/*  8 */     float dif = (target - animation) / Math.max(Minecraft.getDebugFPS(), 5.0F) * 15.0F;
/*    */     
/* 10 */     if (dif > 0.0F) {
/* 11 */       dif = Math.max(speedTarget, dif);
/* 12 */       dif = Math.min(target - animation, dif);
/* 13 */     } else if (dif < 0.0F) {
/* 14 */       dif = Math.min(-speedTarget, dif);
/* 15 */       dif = Math.max(target - animation, dif);
/*    */     } 
/* 17 */     return animation + dif;
/*    */   }
/*    */   
/*    */   public static float calculateCompensation(float target, float current, long delta, double speed) {
/* 21 */     float diff = current - target;
/* 22 */     if (delta < 1L) {
/* 23 */       delta = 1L;
/*    */     }
/* 25 */     if (delta > 1000L) {
/* 26 */       delta = 16L;
/*    */     }
/* 28 */     double dif = Math.max(speed * delta / 16.66666603088379D, 0.5D);
/* 29 */     if (diff > speed) {
/* 30 */       current = (float)(current - dif);
/* 31 */       if (current < target) {
/* 32 */         current = target;
/*    */       }
/* 34 */     } else if (diff < -speed) {
/* 35 */       current = (float)(current + dif);
/* 36 */       if (current > target) {
/* 37 */         current = target;
/*    */       }
/*    */     } else {
/* 40 */       current = target;
/*    */     } 
/* 42 */     return current;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\render\AnimationHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */