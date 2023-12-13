/*    */ package org.neverhook.client.feature.impl.combat;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketEntityAction;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ 
/*    */ public class AutoShift extends Feature {
/*    */   public AutoShift() {
/* 16 */     super("AutoShift", "Игрок автоматически приседает при нажатии на ЛКМ", Type.Combat);
/* 17 */     mode = new ListSetting("ShiftTap Mode", "Client", () -> Boolean.valueOf(true), new String[] { "Client", "Packet" });
/* 18 */     addSettings(new Setting[] { (Setting)mode });
/*    */   }
/*    */   public static ListSetting mode;
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 23 */     if (mode.currentMode.equals("Client")) {
/* 24 */       if (!Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
/* 25 */         mc.gameSettings.keyBindSneak.pressed = mc.gameSettings.keyBindAttack.isKeyDown();
/*    */       }
/* 27 */     } else if (mode.currentMode.equals("Packet")) {
/* 28 */       if (mc.gameSettings.keyBindAttack.isKeyDown()) {
/* 29 */         mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_SNEAKING));
/*    */       } else {
/* 31 */         mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 38 */     mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
/* 39 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\combat\AutoShift.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */