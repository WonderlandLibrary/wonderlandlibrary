/*    */ package org.neverhook.client.feature.impl.combat;
/*    */ 
/*    */ import net.minecraft.init.MobEffects;
/*    */ import net.minecraft.network.play.server.SPacketEntityVelocity;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.packet.EventReceivePacket;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.player.MovementHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class Velocity
/*    */   extends Feature
/*    */ {
/*    */   public static BooleanSetting cancelOtherDamage;
/*    */   public static ListSetting velocityMode;
/* 20 */   public static BooleanSetting cancelEntityVelocity = new BooleanSetting("Cancel Entity Velocity", true, () -> Boolean.valueOf(velocityMode.currentMode.equals("Custom")));
/* 21 */   public static BooleanSetting cancelExplosion = new BooleanSetting("Cancel Explosion", true, () -> Boolean.valueOf(velocityMode.currentMode.equals("Custom")));
/* 22 */   public static BooleanSetting useMotion = new BooleanSetting("Use Motion", true, () -> Boolean.valueOf(velocityMode.currentMode.equals("Custom")));
/* 23 */   public static NumberSetting motionX = new NumberSetting("Motion X", 0.0F, 0.0F, 100.0F, 1.0F, () -> Boolean.valueOf((velocityMode.currentMode.equals("Custom") && useMotion.getBoolValue() && !cancelEntityVelocity.getBoolValue())));
/* 24 */   public static NumberSetting motionY = new NumberSetting("Motion Y", 0.0F, 0.0F, 100.0F, 1.0F, () -> Boolean.valueOf((velocityMode.currentMode.equals("Custom") && useMotion.getBoolValue() && !cancelEntityVelocity.getBoolValue())));
/* 25 */   public static NumberSetting motionZ = new NumberSetting("Motion Z", 0.0F, 0.0F, 100.0F, 1.0F, () -> Boolean.valueOf((velocityMode.currentMode.equals("Custom") && useMotion.getBoolValue() && !cancelEntityVelocity.getBoolValue())));
/* 26 */   public static BooleanSetting usePacketMotion = new BooleanSetting("Use Packet Motion", true, () -> Boolean.valueOf(velocityMode.currentMode.equals("Custom")));
/* 27 */   public static NumberSetting packetX = new NumberSetting("Packet X", 0.0F, 0.0F, 100.0F, 1.0F, () -> Boolean.valueOf((velocityMode.currentMode.equals("Custom") && !cancelEntityVelocity.getBoolValue() && usePacketMotion.getBoolValue())));
/* 28 */   public static NumberSetting packetY = new NumberSetting("Packet Y", 0.0F, 0.0F, 100.0F, 1.0F, () -> Boolean.valueOf((velocityMode.currentMode.equals("Custom") && !cancelEntityVelocity.getBoolValue() && usePacketMotion.getBoolValue())));
/* 29 */   public static NumberSetting packetZ = new NumberSetting("Packet Z", 0.0F, 0.0F, 100.0F, 1.0F, () -> Boolean.valueOf((velocityMode.currentMode.equals("Custom") && !cancelEntityVelocity.getBoolValue() && usePacketMotion.getBoolValue())));
/* 30 */   public static NumberSetting hurt = new NumberSetting("Hurt", 0.0F, 0.0F, 10.0F, 1.0F, () -> Boolean.valueOf(velocityMode.currentMode.equals("Custom")));
/*    */   
/*    */   public Velocity() {
/* 33 */     super("Velocity", "Уменьшает кнокбэк при ударе", Type.Combat);
/* 34 */     velocityMode = new ListSetting("Velocity Mode", "Packet", () -> Boolean.valueOf(true), new String[] { "Packet", "Matrix", "Reverse", "Custom" });
/* 35 */     cancelOtherDamage = new BooleanSetting("Cancel Other Damage", true, () -> Boolean.valueOf(true));
/* 36 */     addSettings(new Setting[] { (Setting)velocityMode, (Setting)cancelEntityVelocity, (Setting)cancelExplosion, (Setting)useMotion, (Setting)usePacketMotion, (Setting)hurt, (Setting)packetX, (Setting)packetY, (Setting)packetZ, (Setting)motionX, (Setting)motionY, (Setting)motionZ, (Setting)cancelOtherDamage });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onReceivePacket(EventReceivePacket event) {
/* 41 */     String mode = velocityMode.getOptions();
/* 42 */     if (cancelOtherDamage.getBoolValue() && 
/* 43 */       mc.player.hurtTime > 0 && event.getPacket() instanceof SPacketEntityVelocity && (
/* 44 */       mc.player.isPotionActive(MobEffects.POISON) || mc.player.isPotionActive(MobEffects.WITHER) || mc.player.isBurning())) {
/* 45 */       event.setCancelled(true);
/*    */     }
/*    */ 
/*    */     
/* 49 */     if (mode.equalsIgnoreCase("Reverse") && 
/* 50 */       mc.player.hurtTime > 0) {
/* 51 */       MovementHelper.strafePlayer(MovementHelper.getSpeed());
/* 52 */       mc.player.speedInAir = 0.02F;
/*    */     } 
/*    */     
/* 55 */     if (mode.equalsIgnoreCase("Packet")) {
/* 56 */       if ((event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof net.minecraft.network.play.server.SPacketExplosion) && (
/* 57 */         (SPacketEntityVelocity)event.getPacket()).getEntityID() == mc.player.getEntityId()) {
/* 58 */         event.setCancelled(true);
/*    */       }
/*    */     }
/* 61 */     else if (mode.equals("Matrix")) {
/* 62 */       if (mc.player.hurtTime > 8) {
/* 63 */         mc.player.onGround = true;
/*    */       }
/* 65 */     } else if (mode.equals("Custom")) {
/* 66 */       if ((cancelEntityVelocity.getBoolValue() && event.getPacket() instanceof SPacketEntityVelocity) || (cancelExplosion.getBoolValue() && event.getPacket() instanceof net.minecraft.network.play.server.SPacketExplosion)) {
/* 67 */         event.setCancelled(true);
/* 68 */       } else if (mc.player.hurtTime > hurt.getNumberValue() || (cancelExplosion.getBoolValue() && event.getPacket() instanceof net.minecraft.network.play.server.SPacketExplosion)) {
/* 69 */         SPacketEntityVelocity sPacketEntityVelocity = (SPacketEntityVelocity)event.getPacket();
/*    */         
/* 71 */         if (usePacketMotion.getBoolValue()) {
/* 72 */           sPacketEntityVelocity.motionX = (int)((sPacketEntityVelocity.motionX / 100) * packetX.getNumberValue());
/* 73 */           sPacketEntityVelocity.motionY = (int)((sPacketEntityVelocity.motionY / 100) * packetY.getNumberValue());
/* 74 */           sPacketEntityVelocity.motionZ = (int)((sPacketEntityVelocity.motionZ / 100) * packetZ.getNumberValue());
/*    */         } 
/*    */         
/* 77 */         if (useMotion.getBoolValue()) {
/* 78 */           mc.player.motionX = (int)(mc.player.motionX / 100.0D * motionX.getNumberValue());
/* 79 */           mc.player.motionY = (int)(mc.player.motionY / 100.0D * motionY.getNumberValue());
/* 80 */           mc.player.motionZ = (int)(mc.player.motionZ / 100.0D * motionZ.getNumberValue());
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\combat\Velocity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */