/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.feature.impl.misc.FreeCam;
/*    */ 
/*    */ public class HurtClip extends Feature {
/*    */   public boolean damageToggle = false;
/*    */   
/*    */   public HurtClip() {
/* 16 */     super("HurtClip", "Клипает вас под бедрок", Type.Player);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 21 */     if (!this.damageToggle) {
/* 22 */       for (int i = 0; i < 9; i++) {
/* 23 */         mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
/* 24 */         mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.4D, mc.player.posZ, false));
/*    */       } 
/* 26 */       mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
/* 27 */       mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
/*    */     } 
/* 29 */     if (mc.player.hurtTime > 0) {
/* 30 */       mc.player.setPositionAndUpdate(mc.player.posX, -2.0D, mc.player.posZ);
/* 31 */       this.damageToggle = true;
/* 32 */       state();
/* 33 */       NeverHook.instance.featureManager.getFeatureByClass(FreeCam.class).setState(true);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\HurtClip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */