/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.math.GCDCalcHelper;
/*    */ import org.neverhook.client.helpers.math.MathematicHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class AntiAim extends Feature {
/* 14 */   public float rot = 0.0F;
/*    */   public NumberSetting spinSpeed;
/*    */   public ListSetting pitchMode;
/*    */   public ListSetting mode;
/*    */   public ListSetting degreeMode;
/*    */   
/*    */   public AntiAim() {
/* 21 */     super("AntiAim", "АнтиАим как в CSGO", Type.Player);
/* 22 */     this.mode = new ListSetting("Yaw Mode", "Jitter", () -> Boolean.valueOf(true), new String[] { "Freestanding", "Spin", "Jitter" });
/* 23 */     this.spinSpeed = new NumberSetting("Spin Speed", 1.0F, 0.0F, 10.0F, 0.1F, () -> Boolean.valueOf(this.degreeMode.currentMode.equals("Spin")));
/* 24 */     this.pitchMode = new ListSetting("Custom Pitch", "Down", () -> Boolean.valueOf(true), new String[] { "None", "Down", "Up", "Fake-Down", "Fake-Up" });
/* 25 */     this.degreeMode = new ListSetting("Degree Mode", "Spin", () -> Boolean.valueOf(true), new String[] { "Random", "Spin" });
/* 26 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)this.spinSpeed, (Setting)this.pitchMode, (Setting)this.degreeMode });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onPreMotion(EventPreMotion event) {
/* 31 */     String antiAimMode = this.mode.getCurrentMode();
/* 32 */     setSuffix(antiAimMode);
/* 33 */     float speed = this.spinSpeed.getNumberValue() * 10.0F;
/* 34 */     switch (this.pitchMode.currentMode) {
/*    */       case "Down":
/* 36 */         event.setPitch(90.0F);
/* 37 */         mc.player.rotationPitchHead = 90.0F;
/*    */         break;
/*    */       case "Up":
/* 40 */         event.setPitch(-90.0F);
/* 41 */         mc.player.rotationPitchHead = -90.0F;
/*    */         break;
/*    */       case "Fake-Down":
/* 44 */         mc.player.rotationPitchHead = 90.0F;
/*    */         break;
/*    */       case "Fake-Up":
/* 47 */         mc.player.rotationPitchHead = -90.0F;
/*    */         break;
/*    */     } 
/* 50 */     if (this.mode.currentMode.equals("Jitter")) {
/* 51 */       float yaw = mc.player.rotationYaw + 180.0F + ((mc.player.ticksExisted % 8 < 4) ? MathematicHelper.randomizeFloat(-90.0F, 90.0F) : -MathematicHelper.randomizeFloat(90.0F, -90.0F));
/* 52 */       event.setYaw(GCDCalcHelper.getFixedRotation(yaw));
/* 53 */       mc.player.renderYawOffset = yaw;
/* 54 */       mc.player.rotationYawHead = yaw;
/* 55 */     } else if (antiAimMode.equals("Freestanding")) {
/* 56 */       float yaw = (float)((mc.player.rotationYaw + 5.0F) + Math.random() * 175.0D);
/* 57 */       event.setYaw(GCDCalcHelper.getFixedRotation(yaw));
/* 58 */       mc.player.renderYawOffset = yaw;
/* 59 */       mc.player.rotationYawHead = yaw;
/* 60 */     } else if (antiAimMode.equalsIgnoreCase("Spin")) {
/* 61 */       float yaw = GCDCalcHelper.getFixedRotation((float)(Math.floor(spinAim(speed)) + MathematicHelper.randomizeFloat(-4.0F, 1.0F)));
/* 62 */       event.setYaw(yaw);
/* 63 */       mc.player.renderYawOffset = yaw;
/* 64 */       mc.player.rotationYawHead = yaw;
/*    */     } 
/*    */     
/* 67 */     if (mc.player.isSneaking()) {
/* 68 */       if (this.degreeMode.currentMode.equals("Spin")) {
/* 69 */         float yaw = GCDCalcHelper.getFixedRotation((float)(Math.floor(spinAim(speed)) + MathematicHelper.randomizeFloat(-4.0F, 1.0F)));
/* 70 */         event.setYaw(yaw);
/* 71 */         mc.player.renderYawOffset = yaw;
/* 72 */         mc.player.rotationYawHead = yaw;
/* 73 */       } else if (this.degreeMode.currentMode.equals("Random")) {
/* 74 */         float yaw = (float)(mc.player.rotationYaw + Math.floor((spinAim(speed) + ((mc.player.ticksExisted % 8 < 4) ? MathematicHelper.randomizeFloat(33.0F, 22.0F) : -MathematicHelper.randomizeFloat(33.0F, 22.0F)))));
/* 75 */         event.setYaw(yaw);
/* 76 */         mc.player.renderYawOffset = yaw;
/* 77 */         mc.player.rotationYawHead = yaw;
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   public float spinAim(float rots) {
/* 83 */     this.rot += rots;
/* 84 */     return this.rot;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\AntiAim.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */