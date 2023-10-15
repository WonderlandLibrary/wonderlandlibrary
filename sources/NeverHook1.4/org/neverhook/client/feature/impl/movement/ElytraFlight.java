/*    */ package org.neverhook.client.feature.impl.movement;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.player.MovementHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class ElytraFlight extends Feature {
/*    */   public static NumberSetting motion;
/*    */   
/*    */   public ElytraFlight() {
/* 15 */     super("ElytraFlight", "Позволяет летать на элитрах без фейерверков", Type.Movement);
/* 16 */     motion = new NumberSetting("Elytra Speed", 1.5F, 0.5F, 5.0F, 0.5F, () -> Boolean.valueOf(true));
/* 17 */     addSettings(new Setting[] { (Setting)motion });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 22 */     if (mc.player.isElytraFlying()) {
/* 23 */       mc.player.onGround = false;
/* 24 */       mc.player.setVelocity(0.0D, 0.0D, 0.0D);
/* 25 */       if (mc.gameSettings.keyBindSneak.isKeyDown())
/* 26 */         mc.player.motionY = -motion.getNumberValue(); 
/* 27 */       if (mc.gameSettings.keyBindJump.isKeyDown())
/* 28 */         mc.player.motionY = motion.getNumberValue(); 
/* 29 */       if (MovementHelper.isMoving()) {
/* 30 */         MovementHelper.setSpeed(motion.getNumberValue());
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 37 */     mc.player.capabilities.isFlying = false;
/* 38 */     mc.player.capabilities.setFlySpeed(0.05F);
/* 39 */     if (!mc.player.capabilities.isCreativeMode) {
/* 40 */       mc.player.capabilities.allowFlying = false;
/*    */     }
/* 42 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\ElytraFlight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */