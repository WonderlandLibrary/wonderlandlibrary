/*    */ package org.neverhook.client.event.events.impl.motion;
/*    */ 
/*    */ import org.neverhook.client.event.events.callables.EventCancellable;
/*    */ 
/*    */ public class EventStrafe extends EventCancellable {
/*    */   private float yaw;
/*    */   private float strafe;
/*    */   
/*    */   public EventStrafe(float yaw, float strafe, float forward, float friction) {
/* 10 */     this.yaw = yaw;
/* 11 */     this.strafe = strafe;
/* 12 */     this.forward = forward;
/* 13 */     this.friction = friction;
/*    */   }
/*    */   private float forward; private float friction;
/*    */   public float getStrafe() {
/* 17 */     return this.strafe;
/*    */   }
/*    */   
/*    */   public void setStrafe(float strafe) {
/* 21 */     this.strafe = strafe;
/*    */   }
/*    */   
/*    */   public float getForward() {
/* 25 */     return this.forward;
/*    */   }
/*    */   
/*    */   public void setForward(float forward) {
/* 29 */     this.forward = forward;
/*    */   }
/*    */   
/*    */   public float getFriction() {
/* 33 */     return this.friction;
/*    */   }
/*    */   
/*    */   public void setFriction(float friction) {
/* 37 */     this.friction = friction;
/*    */   }
/*    */   
/*    */   public float getYaw() {
/* 41 */     return this.yaw;
/*    */   }
/*    */   
/*    */   public void setYaw(float yaw) {
/* 45 */     this.yaw = yaw;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\motion\EventStrafe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */