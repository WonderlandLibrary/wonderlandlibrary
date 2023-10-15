/*    */ package org.neverhook.client.feature.impl.misc;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.misc.ChatHelper;
/*    */ import org.neverhook.client.ui.notification.NotificationManager;
/*    */ import org.neverhook.client.ui.notification.NotificationType;
/*    */ 
/*    */ public class DeathCoordinates
/*    */   extends Feature
/*    */ {
/*    */   public DeathCoordinates() {
/* 15 */     super("DeathCoordinates", "Показывает координаты смерти игрока", Type.Misc);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 20 */     if (mc.player.getHealth() < 1.0F && mc.currentScreen instanceof net.minecraft.client.gui.GuiGameOver) {
/* 21 */       int x = mc.player.getPosition().getX();
/* 22 */       int y = mc.player.getPosition().getY();
/* 23 */       int z = mc.player.getPosition().getZ();
/* 24 */       if (mc.player.ticksExisted % 20 == 0) {
/* 25 */         NotificationManager.publicity("Death Coordinates", "X: " + x + " Y: " + y + " Z: " + z, 10, NotificationType.INFO);
/* 26 */         ChatHelper.addChatMessage("Death Coordinates: X: " + x + " Y: " + y + " Z: " + z);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\DeathCoordinates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */