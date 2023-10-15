/*    */ package org.neverhook.client.event.events.impl.motion;
/*    */ 
/*    */ import org.neverhook.client.event.events.Event;
/*    */ 
/*    */ public class EventStep
/*    */   implements Event
/*    */ {
/*    */   private final boolean pre;
/*    */   private double stepHeight;
/*    */   
/*    */   public EventStep(boolean pre, double stepHeight) {
/* 12 */     this.pre = pre;
/* 13 */     this.stepHeight = stepHeight;
/*    */   }
/*    */   
/*    */   public boolean isPre() {
/* 17 */     return this.pre;
/*    */   }
/*    */   
/*    */   public double getStepHeight() {
/* 21 */     return this.stepHeight;
/*    */   }
/*    */   
/*    */   public void setStepHeight(double stepHeight) {
/* 25 */     this.stepHeight = stepHeight;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\motion\EventStep.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */