/*    */ package org.neverhook.client.event.events.impl.motion;
/*    */ 
/*    */ import org.neverhook.client.event.events.callables.EventCancellable;
/*    */ 
/*    */ public class EventMove
/*    */   extends EventCancellable {
/*    */   private double x;
/*    */   
/*    */   public EventMove(double x, double y, double z) {
/* 10 */     this.x = x;
/* 11 */     this.y = y;
/* 12 */     this.z = z;
/*    */   }
/*    */   private double y; private double z;
/*    */   public double getX() {
/* 16 */     return this.x;
/*    */   }
/*    */   
/*    */   public void setX(double x) {
/* 20 */     this.x = x;
/*    */   }
/*    */   
/*    */   public double getY() {
/* 24 */     return this.y;
/*    */   }
/*    */   
/*    */   public void setY(double y) {
/* 28 */     this.y = y;
/*    */   }
/*    */   
/*    */   public double getZ() {
/* 32 */     return this.z;
/*    */   }
/*    */   
/*    */   public void setZ(double z) {
/* 36 */     this.z = z;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\motion\EventMove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */