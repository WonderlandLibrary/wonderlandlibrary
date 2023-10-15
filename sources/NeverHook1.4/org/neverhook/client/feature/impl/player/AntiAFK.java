/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.math.GCDCalcHelper;
/*    */ import org.neverhook.client.helpers.math.MathematicHelper;
/*    */ import org.neverhook.client.helpers.player.MovementHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class AntiAFK extends Feature {
/*    */   private final BooleanSetting sendMessage;
/* 18 */   private final BooleanSetting click = new BooleanSetting("Click", true, () -> Boolean.valueOf(true)); private final BooleanSetting spin;
/*    */   public NumberSetting sendDelay;
/* 20 */   public BooleanSetting invalidRotation = new BooleanSetting("Invalid Rotation", true, () -> Boolean.valueOf(true));
/* 21 */   public BooleanSetting jump = new BooleanSetting("Jump", true, () -> Boolean.valueOf(true));
/* 22 */   public float rot = 0.0F;
/*    */   
/*    */   public AntiAFK() {
/* 25 */     super("AntiAFK", "Позволяет встать в афк режим, без риска кикнуться", Type.Player);
/* 26 */     this.spin = new BooleanSetting("Spin", true, () -> Boolean.valueOf(true));
/* 27 */     this.sendMessage = new BooleanSetting("Send Message", true, () -> Boolean.valueOf(true));
/* 28 */     this.sendDelay = new NumberSetting("Send Delay", 500.0F, 100.0F, 1000.0F, 100.0F, this.sendMessage::getBoolValue);
/* 29 */     addSettings(new Setting[] { (Setting)this.spin, (Setting)this.sendMessage, (Setting)this.click, (Setting)this.sendDelay, (Setting)this.invalidRotation, (Setting)this.jump });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onPreMotion(EventPreMotion event) {
/* 34 */     if (!MovementHelper.isMoving()) {
/* 35 */       if (this.spin.getBoolValue()) {
/* 36 */         float yaw = GCDCalcHelper.getFixedRotation((float)(Math.floor(spinAim(1.0F)) + MathematicHelper.randomizeFloat(-4.0F, 1.0F)));
/* 37 */         event.setYaw(yaw);
/* 38 */         mc.player.renderYawOffset = yaw;
/* 39 */         mc.player.rotationPitchHead = 0.0F;
/* 40 */         mc.player.rotationYawHead = yaw;
/*    */       } 
/*    */       
/* 43 */       if (mc.player.ticksExisted % 60 == 0 && this.invalidRotation.getBoolValue()) {
/* 44 */         mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(mc.player.posX + 1.0D, mc.player.posY + 1.0D, mc.player.posZ + 1.0D, mc.player.rotationYaw, mc.player.rotationPitch, true));
/*    */       }
/*    */       
/* 47 */       if (mc.player.ticksExisted % 60 == 0 && this.click.getBoolValue()) {
/* 48 */         mc.clickMouse();
/*    */       }
/*    */       
/* 51 */       if (mc.player.ticksExisted % 40 == 0 && this.jump.getBoolValue()) {
/* 52 */         mc.player.jump();
/*    */       }
/*    */       
/* 55 */       if (mc.player.ticksExisted % 400 == 0 && this.sendMessage.getBoolValue()) {
/* 56 */         mc.player.sendChatMessage("/homehome");
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public float spinAim(float rots) {
/* 62 */     this.rot += rots;
/* 63 */     return this.rot;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\AntiAFK.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */