/*    */ package org.neverhook.client.feature.impl.combat;
/*    */ 
/*    */ import net.minecraft.network.play.client.CPacketUseEntity;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.packet.EventSendPacket;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ public class NoFriendDamage
/*    */   extends Feature {
/*    */   public NoFriendDamage() {
/* 13 */     super("NoFriendDamage", "Не даёт ударить друга", Type.Combat);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onSendPacket(EventSendPacket event) {
/* 18 */     if (event.getPacket() instanceof CPacketUseEntity) {
/* 19 */       CPacketUseEntity cpacketUseEntity = (CPacketUseEntity)event.getPacket();
/* 20 */       if (cpacketUseEntity.getAction().equals(CPacketUseEntity.Action.ATTACK) && NeverHook.instance.friendManager.isFriend(mc.objectMouseOver.entityHit.getName()))
/* 21 */         event.setCancelled(true); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\combat\NoFriendDamage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */