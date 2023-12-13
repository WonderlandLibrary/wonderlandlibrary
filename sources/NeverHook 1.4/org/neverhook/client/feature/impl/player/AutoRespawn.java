/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ public class AutoRespawn
/*    */   extends Feature
/*    */ {
/*    */   public AutoRespawn() {
/* 12 */     super("AutoRespawn", "Автоматический респавн при смерти", Type.Player);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 17 */     if (mc.player.getHealth() < 0.0F || !mc.player.isEntityAlive() || mc.currentScreen instanceof net.minecraft.client.gui.GuiGameOver) {
/* 18 */       mc.player.respawnPlayer();
/* 19 */       mc.displayGuiScreen(null);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\AutoRespawn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */