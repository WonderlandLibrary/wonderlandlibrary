/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.packet.EventSendPacket;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ public class XCarry
/*    */   extends Feature
/*    */ {
/*    */   public XCarry() {
/* 12 */     super("XCarry", "Позволяет хранить предметы в слотах для крафта", Type.Player);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onSendPacket(EventSendPacket event) {
/* 17 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketCloseWindow)
/* 18 */       event.setCancelled(true); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\XCarry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */