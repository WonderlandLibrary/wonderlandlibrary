/*    */ package org.neverhook.client.feature.impl.misc;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class EntityFeatures
/*    */   extends Feature {
/*    */   public static BooleanSetting entityControl;
/*    */   public static BooleanSetting entitySpeed;
/*    */   public static NumberSetting entitySpeedValue;
/*    */   
/*    */   public EntityFeatures() {
/* 19 */     super("EntityFeatures", "Позволяет контролировать животных", Type.Misc);
/* 20 */     entityControl = new BooleanSetting("Entity Control", true, () -> Boolean.valueOf(true));
/* 21 */     entitySpeed = new BooleanSetting("Entity Speed", true, entityControl::getBoolValue);
/* 22 */     entitySpeedValue = new NumberSetting("Entity Speed Multiplier", 1.0F, 0.0F, 2.0F, 0.1F, entityControl::getBoolValue);
/* 23 */     addSettings(new Setting[] { (Setting)entityControl, (Setting)entitySpeed, (Setting)entitySpeedValue });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 28 */     if (entityControl.getBoolValue()) {
/* 29 */       Entity ridingEntity = mc.player.getRidingEntity();
/* 30 */       assert ridingEntity != null;
/* 31 */       if (ridingEntity instanceof net.minecraft.entity.passive.AbstractHorse) {
/* 32 */         mc.player.horseJumpPowerCounter = 9;
/* 33 */         mc.player.horseJumpPower = 1.0F;
/*    */       } 
/*    */     } 
/* 36 */     if (entitySpeed.getBoolValue() && 
/* 37 */       mc.player != null && mc.player.getRidingEntity() != null) {
/* 38 */       double forward = mc.player.movementInput.moveForward;
/* 39 */       double strafe = mc.player.movementInput.moveStrafe;
/* 40 */       float yaw = mc.player.rotationYaw;
/* 41 */       if (forward == 0.0D && strafe == 0.0D) {
/* 42 */         (mc.player.getRidingEntity()).motionX = 0.0D;
/* 43 */         (mc.player.getRidingEntity()).motionZ = 0.0D;
/*    */       } else {
/* 45 */         if (forward != 0.0D) {
/* 46 */           if (strafe > 0.0D) {
/* 47 */             yaw += ((forward > 0.0D) ? -45 : 45);
/* 48 */           } else if (strafe < 0.0D) {
/* 49 */             yaw += ((forward > 0.0D) ? 45 : -45);
/*    */           } 
/* 51 */           strafe = 0.0D;
/* 52 */           if (forward > 0.0D) {
/* 53 */             forward = 1.0D;
/* 54 */           } else if (forward < 0.0D) {
/* 55 */             forward = -1.0D;
/*    */           } 
/*    */         } 
/* 58 */         double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
/* 59 */         double sin = Math.sin(Math.toRadians((yaw + 90.0F)));
/* 60 */         (mc.player.getRidingEntity()).motionX = forward * entitySpeedValue.getNumberValue() * cos + strafe * entitySpeedValue.getNumberValue() * sin;
/* 61 */         (mc.player.getRidingEntity()).motionZ = forward * entitySpeedValue.getNumberValue() * sin - strafe * entitySpeedValue.getNumberValue() * cos;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\EntityFeatures.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */