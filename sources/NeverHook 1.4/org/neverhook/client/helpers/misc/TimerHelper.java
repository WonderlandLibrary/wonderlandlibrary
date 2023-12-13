/*    */ package org.neverhook.client.helpers.misc;
/*    */ 
/*    */ import org.neverhook.client.helpers.Helper;
/*    */ 
/*    */ public class TimerHelper
/*    */   implements Helper {
/*  7 */   private long ms = getCurrentMS();
/*    */   
/*    */   private long getCurrentMS() {
/* 10 */     return System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   public boolean hasReached(float milliseconds) {
/* 14 */     return ((float)(getCurrentMS() - this.ms) > milliseconds);
/*    */   }
/*    */   
/*    */   public void reset() {
/* 18 */     this.ms = getCurrentMS();
/*    */   }
/*    */   
/*    */   public long getTime() {
/* 22 */     return getCurrentMS() - this.ms;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\misc\TimerHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */