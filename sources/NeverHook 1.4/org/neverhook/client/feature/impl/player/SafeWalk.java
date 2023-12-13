/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.motion.EventSafeWalk;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ public class SafeWalk
/*    */   extends Feature {
/*    */   public SafeWalk() {
/* 11 */     super("SafeWalk", "Не дает упасть вам с блока", Type.Player);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onSafeWalk(EventSafeWalk event) {
/* 16 */     if (mc.player == null || mc.world == null)
/*    */       return; 
/* 18 */     event.setCancelled(mc.player.onGround);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\SafeWalk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */