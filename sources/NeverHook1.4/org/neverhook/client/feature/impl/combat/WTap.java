/*    */ package org.neverhook.client.feature.impl.combat;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketEntityAction;
/*    */ import net.minecraft.network.play.client.CPacketUseEntity;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.packet.EventSendPacket;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ public class WTap extends Feature {
/*    */   public WTap() {
/* 13 */     super("WTap", "Вы откидываете противника дальше", Type.Ghost);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onSendPacket(EventSendPacket event) {
/* 18 */     if (event.getPacket() instanceof CPacketUseEntity) {
/* 19 */       CPacketUseEntity packet = (CPacketUseEntity)event.getPacket();
/* 20 */       if (packet.getAction() == CPacketUseEntity.Action.ATTACK) {
/* 21 */         mc.player.setSprinting(false);
/* 22 */         mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
/* 23 */         mc.player.setSprinting(true);
/* 24 */         mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_SPRINTING));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\combat\WTap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */