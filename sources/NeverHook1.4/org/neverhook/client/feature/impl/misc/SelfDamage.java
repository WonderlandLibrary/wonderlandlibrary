/*    */ package org.neverhook.client.feature.impl.misc;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ public class SelfDamage
/*    */   extends Feature {
/* 10 */   private int jumps = 0;
/*    */   
/*    */   public SelfDamage() {
/* 13 */     super("SelfDamage", "Вы наносите себе дамаг", Type.Misc);
/*    */   }
/*    */   
/*    */   public void onEnable() {
/* 17 */     this.jumps = 0;
/* 18 */     super.onEnable();
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventPreMotion event) {
/* 23 */     if (this.jumps < 14) {
/* 24 */       mc.timer.renderPartialTicks = 4.0F;
/* 25 */       for (int i = 0; i < 20; i++) {
/* 26 */         event.setOnGround(false);
/*    */       }
/*    */     } 
/* 29 */     if (mc.player.onGround)
/* 30 */       if (this.jumps < 14) {
/* 31 */         mc.player.jump();
/* 32 */         this.jumps++;
/*    */       } else {
/* 34 */         state();
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\SelfDamage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */