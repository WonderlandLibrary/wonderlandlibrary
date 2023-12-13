/*    */ package org.neverhook.client.event.events.impl.motion;
/*    */ 
/*    */ import org.neverhook.client.event.events.Event;
/*    */ import org.neverhook.client.event.events.callables.EventCancellable;
/*    */ 
/*    */ public class EventJump
/*    */   extends EventCancellable implements Event {
/*    */   private float yaw;
/*    */   
/*    */   public EventJump(float yaw) {
/* 11 */     this.yaw = yaw;
/*    */   }
/*    */   
/*    */   public float getYaw() {
/* 15 */     return this.yaw;
/*    */   }
/*    */   
/*    */   public void setYaw(float yaw) {
/* 19 */     this.yaw = yaw;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\motion\EventJump.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */