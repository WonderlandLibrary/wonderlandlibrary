/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.misc.TimerHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ 
/*    */ public class NoFall extends Feature {
/* 14 */   public TimerHelper timerHelper = new TimerHelper(); public static ListSetting noFallMode;
/*    */   
/*    */   public NoFall() {
/* 17 */     super("NoFall", "Позволяет получить меньший дамаг при падении", Type.Player);
/* 18 */     noFallMode = new ListSetting("NoFall Mode", "Vanilla", () -> Boolean.valueOf(true), new String[] { "Vanilla", "GroundCancel", "Spartan", "AAC-Flags", "Matrix", "Hypixel" });
/* 19 */     addSettings(new Setting[] { (Setting)noFallMode });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onPreMotion(EventPreMotion event) {
/* 24 */     String mode = noFallMode.getOptions();
/* 25 */     setSuffix(mode);
/* 26 */     if (mode.equalsIgnoreCase("Vanilla")) {
/* 27 */       if (mc.player.fallDistance > 3.0F) {
/* 28 */         event.setOnGround(true);
/* 29 */         mc.player.connection.sendPacket((Packet)new CPacketPlayer(true));
/*    */       } 
/* 31 */     } else if (mode.equalsIgnoreCase("Spartan")) {
/* 32 */       if (mc.player.fallDistance > 3.5F) {
/* 33 */         if (this.timerHelper.hasReached(150.0F)) {
/* 34 */           mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
/* 35 */           this.timerHelper.reset();
/*    */         } else {
/* 37 */           mc.player.onGround = false;
/*    */         } 
/*    */       }
/* 40 */     } else if (mode.equalsIgnoreCase("AAC-Flags")) {
/* 41 */       mc.player.motionY -= 0.1D;
/* 42 */       event.setOnGround(true);
/* 43 */       mc.player.capabilities.disableDamage = true;
/* 44 */     } else if (mode.equalsIgnoreCase("Hypixel")) {
/* 45 */       if (mc.player.fallDistance > 3.4D) {
/* 46 */         event.setOnGround((mc.player.ticksExisted % 2 == 0));
/*    */       }
/* 48 */     } else if (mode.equalsIgnoreCase("Matrix")) {
/* 49 */       if (mc.player.fallDistance > 3.0F) {
/* 50 */         mc.player.fallDistance = (float)(Math.random() * 1.0E-12D);
/* 51 */         mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, true));
/* 52 */         mc.player.fallDistance = 0.0F;
/*    */       } 
/* 54 */     } else if (mode.equalsIgnoreCase("GroundCancel")) {
/* 55 */       event.setOnGround(false);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\NoFall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */