/*    */ package org.neverhook.client.feature.impl.movement;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ public class AirJump
/*    */   extends Feature {
/*    */   public AirJump() {
/* 11 */     super("AirJump", "Позволяет прыгать по воздуху", Type.Movement);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onPreUpdate(EventPreMotion event) {
/* 16 */     if (mc.gameSettings.keyBindJump.pressed)
/* 17 */       mc.player.onGround = true; 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\AirJump.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */