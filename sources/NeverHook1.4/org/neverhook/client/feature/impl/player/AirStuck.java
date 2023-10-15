/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.packet.EventReceivePacket;
/*    */ import org.neverhook.client.event.events.impl.packet.EventSendPacket;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AirStuck
/*    */   extends Feature
/*    */ {
/*    */   public AirStuck() {
/* 18 */     super("AirStuck", "Вы зависаете в воздухе", Type.Player);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 23 */     mc.player.motionX = 0.0D;
/* 24 */     mc.player.motionY = 0.0D;
/* 25 */     mc.player.motionZ = 0.0D;
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onReceivePacket(EventReceivePacket event) {
/* 30 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketPlayerPosLook) {
/* 31 */       event.setCancelled(true);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   @EventTarget
/*    */   public void onSendPacket(EventSendPacket event) {
/* 38 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayer) {
/* 39 */       event.setCancelled(true);
/*    */     }
/* 41 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayer.Position) {
/* 42 */       event.setCancelled(true);
/*    */     }
/* 44 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayer.PositionRotation) {
/* 45 */       event.setCancelled(true);
/*    */     }
/* 47 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketEntityAction)
/* 48 */       event.setCancelled(true); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\AirStuck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */