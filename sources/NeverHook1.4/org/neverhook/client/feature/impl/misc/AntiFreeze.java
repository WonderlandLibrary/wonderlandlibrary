/*    */ package org.neverhook.client.feature.impl.misc;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.packet.EventReceivePacket;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AntiFreeze
/*    */   extends Feature
/*    */ {
/*    */   public AntiFreeze() {
/* 14 */     super("Anti Freeze", "Убирает заморозку игрока", Type.Misc);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onReceivePacket(EventReceivePacket event) {
/* 19 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketEntityTeleport) {
/* 20 */       event.setCancelled(true);
/*    */     }
/* 22 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketPlayerPosLook) {
/* 23 */       event.setCancelled(true);
/*    */     }
/* 25 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketOpenWindow)
/* 26 */       event.setCancelled(true); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\AntiFreeze.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */