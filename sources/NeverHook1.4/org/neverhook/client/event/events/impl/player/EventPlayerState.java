/*    */ package org.neverhook.client.event.events.impl.player;
/*    */ 
/*    */ import org.neverhook.client.event.events.callables.EventCancellable;
/*    */ 
/*    */ 
/*    */ public class EventPlayerState
/*    */   extends EventCancellable
/*    */ {
/*    */   private final boolean isPre;
/*    */   private float yaw;
/*    */   private float pitch;
/*    */   
/*    */   public EventPlayerState(double x, double y, double z, float yaw, float pitch, boolean onGround) {
/* 14 */     this.y = y;
/* 15 */     this.x = x;
/* 16 */     this.z = z;
/* 17 */     this.isPre = true;
/* 18 */     this.yaw = yaw;
/* 19 */     this.pitch = pitch;
/* 20 */     this.onGround = onGround;
/*    */   }
/*    */   private double x; private double y; private double z; private boolean onGround;
/*    */   public EventPlayerState() {
/* 24 */     this.isPre = false;
/*    */   }
/*    */   
/*    */   public boolean isPre() {
/* 28 */     return this.isPre;
/*    */   }
/*    */   
/*    */   public boolean isPost() {
/* 32 */     return !this.isPre;
/*    */   }
/*    */   
/*    */   public float getYaw() {
/* 36 */     return this.yaw;
/*    */   }
/*    */   
/*    */   public void setYaw(float yaw) {
/* 40 */     this.yaw = yaw;
/*    */   }
/*    */   
/*    */   public float getPitch() {
/* 44 */     return this.pitch;
/*    */   }
/*    */   
/*    */   public void setPitch(float pitch) {
/* 48 */     this.pitch = pitch;
/*    */   }
/*    */   
/*    */   public double getX() {
/* 52 */     return this.x;
/*    */   }
/*    */   
/*    */   public void setX(double x) {
/* 56 */     this.x = x;
/*    */   }
/*    */   
/*    */   public double getY() {
/* 60 */     return this.y;
/*    */   }
/*    */   
/*    */   public void setY(double y) {
/* 64 */     this.y = y;
/*    */   }
/*    */   
/*    */   public double getZ() {
/* 68 */     return this.z;
/*    */   }
/*    */   
/*    */   public void setZ(double z) {
/* 72 */     this.z = z;
/*    */   }
/*    */   
/*    */   public boolean isOnGround() {
/* 76 */     return this.onGround;
/*    */   }
/*    */   
/*    */   public void setOnGround(boolean onGround) {
/* 80 */     this.onGround = onGround;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\player\EventPlayerState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */