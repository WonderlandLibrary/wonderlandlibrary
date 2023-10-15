/*    */ package org.neverhook.client.feature.impl.misc;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.packet.EventSendPacket;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ 
/*    */ public class PortalFeatures
/*    */   extends Feature {
/* 12 */   public static BooleanSetting chat = new BooleanSetting("Chat", true, () -> Boolean.valueOf(true));
/* 13 */   public static BooleanSetting cancelTeleport = new BooleanSetting("Cancel Teleport", true, () -> Boolean.valueOf(true));
/*    */   
/*    */   public PortalFeatures() {
/* 16 */     super("PortalFeatures", "Позволяет открыть чат в портале", Type.Misc);
/* 17 */     addSettings(new Setting[] { (Setting)chat, (Setting)cancelTeleport });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onSendPacket(EventSendPacket event) {
/* 22 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketConfirmTeleport && cancelTeleport.getBoolValue())
/* 23 */       event.setCancelled(true); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\PortalFeatures.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */