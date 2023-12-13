/*    */ package org.neverhook.client.event.events.impl.player;
/*    */ 
/*    */ import org.neverhook.client.event.events.callables.EventCancellable;
/*    */ 
/*    */ public class EventPostMotion
/*    */   extends EventCancellable {
/*    */   public float pitch;
/*    */   private float yaw;
/*    */   
/*    */   public EventPostMotion(float yaw, float pitch) {
/* 11 */     this.yaw = yaw;
/* 12 */     this.pitch = pitch;
/*    */   }
/*    */   
/*    */   public float getYaw() {
/* 16 */     return this.yaw;
/*    */   }
/*    */   
/*    */   public void setYaw(float yaw) {
/* 20 */     this.yaw = yaw;
/*    */   }
/*    */   
/*    */   public float getPitch() {
/* 24 */     return this.pitch;
/*    */   }
/*    */   
/*    */   public void setPitch(float pitch) {
/* 28 */     this.pitch = pitch;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\player\EventPostMotion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */