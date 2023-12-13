/*    */ package org.neverhook.client.feature.impl.movement;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class LongJump extends Feature {
/*    */   public ListSetting mode;
/*    */   public NumberSetting boostMultiplier;
/*    */   public NumberSetting motionBoost;
/* 16 */   public BooleanSetting motionYBoost = new BooleanSetting("MotionY boost", false, () -> Boolean.valueOf(true));
/*    */   
/*    */   public LongJump() {
/* 19 */     super("Long Jump", "Позволяет прыгать на большую длинну", Type.Movement);
/* 20 */     this.mode = new ListSetting("LongJump Mode", "Matrix Pearle", () -> Boolean.valueOf(true), new String[] { "Redesky", "Matrix Pearle" });
/* 21 */     this.boostMultiplier = new NumberSetting("Boost Speed", 0.3F, 0.1F, 1.0F, 0.1F, () -> Boolean.valueOf(this.mode.currentMode.equals("Matrix Pearle")));
/* 22 */     this.motionBoost = new NumberSetting("Motion Boost", 0.6F, 0.1F, 8.0F, 0.1F, () -> Boolean.valueOf((this.mode.currentMode.equals("Matrix Pearle") && this.motionYBoost.getBoolValue())));
/* 23 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)this.boostMultiplier, (Setting)this.motionYBoost, (Setting)this.motionBoost });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onPreUpdate(EventPreMotion event) {
/* 28 */     String longMode = this.mode.getOptions();
/* 29 */     setSuffix(longMode);
/* 30 */     if (!getState())
/*    */       return; 
/* 32 */     if (longMode.equalsIgnoreCase("Redesky")) {
/* 33 */       if (mc.player.hurtTime > 0) {
/* 34 */         mc.timer.timerSpeed = 1.0F;
/* 35 */         if (mc.player.fallDistance != 0.0F) {
/* 36 */           mc.player.motionY += 0.039D;
/*    */         }
/* 38 */         if (mc.player.onGround) {
/* 39 */           mc.player.jump();
/*    */         } else {
/* 41 */           mc.timer.timerSpeed = 0.2F;
/* 42 */           mc.player.motionY += 0.075D;
/* 43 */           mc.player.motionX *= 1.065000057220459D;
/* 44 */           mc.player.motionZ *= 1.065000057220459D;
/*    */         } 
/*    */       } 
/* 47 */     } else if (longMode.equalsIgnoreCase("Matrix Pearle") && 
/* 48 */       mc.player.hurtTime > 0) {
/* 49 */       mc.player.isAirBorne = true;
/* 50 */       if (this.motionYBoost.getBoolValue()) {
/* 51 */         mc.player.motionY = this.motionBoost.getNumberValue();
/*    */       }
/* 53 */       mc.player.jumpMovementFactor = this.boostMultiplier.getNumberValue();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 60 */     mc.timer.timerSpeed = 1.0F;
/* 61 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\LongJump.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */