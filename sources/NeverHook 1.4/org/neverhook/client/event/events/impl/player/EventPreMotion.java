/*    */ package org.neverhook.client.event.events.impl.player;
/*    */ 
/*    */ import org.neverhook.client.event.events.Event;
/*    */ 
/*    */ public class EventPreMotion
/*    */   implements Event {
/*    */   private float yaw;
/*    */   private float pitch;
/*    */   private double posX;
/*    */   
/*    */   public EventPreMotion(float yaw, float pitch, double posX, double posY, double posZ, boolean onGround) {
/* 12 */     this.yaw = yaw;
/* 13 */     this.pitch = pitch;
/* 14 */     this.posX = posX;
/* 15 */     this.posY = posY;
/* 16 */     this.posZ = posZ;
/* 17 */     this.onGround = onGround;
/*    */   }
/*    */   private double posY; private double posZ; private boolean onGround;
/*    */   public float getYaw() {
/* 21 */     return this.yaw;
/*    */   }
/*    */   
/*    */   public void setYaw(float yaw) {
/* 25 */     this.yaw = yaw;
/*    */   }
/*    */   
/*    */   public float getPitch() {
/* 29 */     return this.pitch;
/*    */   }
/*    */   
/*    */   public void setPitch(float pitch) {
/* 33 */     this.pitch = pitch;
/*    */   }
/*    */   
/*    */   public double getPosX() {
/* 37 */     return this.posX;
/*    */   }
/*    */   
/*    */   public void setPosX(double posX) {
/* 41 */     this.posX = posX;
/*    */   }
/*    */   
/*    */   public double getPosY() {
/* 45 */     return this.posY;
/*    */   }
/*    */   
/*    */   public void setPosY(double posY) {
/* 49 */     this.posY = posY;
/*    */   }
/*    */   
/*    */   public double getPosZ() {
/* 53 */     return this.posZ;
/*    */   }
/*    */   
/*    */   public void setPosZ(double posZ) {
/* 57 */     this.posZ = posZ;
/*    */   }
/*    */   
/*    */   public boolean isOnGround() {
/* 61 */     return this.onGround;
/*    */   }
/*    */   
/*    */   public void setOnGround(boolean onGround) {
/* 65 */     this.onGround = onGround;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\player\EventPreMotion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */