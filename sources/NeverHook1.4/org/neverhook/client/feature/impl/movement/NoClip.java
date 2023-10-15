/*    */ package org.neverhook.client.feature.impl.movement;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventFullCube;
/*    */ import org.neverhook.client.event.events.impl.player.EventPush;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdateLiving;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.player.MovementHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class NoClip extends Feature {
/*    */   public static NumberSetting speed;
/*    */   
/*    */   public NoClip() {
/* 17 */     super("No Clip", "Позволяет ходить сквозь стены", Type.Movement);
/* 18 */     speed = new NumberSetting("Speed", 0.02F, 0.0F, 2.0F, 0.01F, () -> Boolean.valueOf(true));
/* 19 */     addSettings(new Setting[] { (Setting)speed });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onFullCube(EventFullCube event) {
/* 24 */     if (mc.world != null) {
/* 25 */       event.setCancelled(true);
/*    */     }
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onPush(EventPush event) {
/* 31 */     event.setCancelled(true);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onLivingUpdate(EventUpdateLiving event) {
/* 36 */     if (mc.player != null) {
/* 37 */       mc.player.noClip = true;
/* 38 */       mc.player.motionY = 0.0D;
/* 39 */       mc.player.onGround = false;
/* 40 */       mc.player.capabilities.isFlying = false;
/* 41 */       MovementHelper.setSpeed((speed.getNumberValue() == 0.0F) ? MovementHelper.getBaseMoveSpeed() : speed.getNumberValue());
/* 42 */       if (mc.gameSettings.keyBindJump.isKeyDown()) {
/* 43 */         mc.player.motionY += 0.5D;
/*    */       }
/* 45 */       if (mc.gameSettings.keyBindSneak.isKeyDown()) {
/* 46 */         mc.player.motionY -= 0.5D;
/*    */       }
/* 48 */       event.setCancelled(true);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\NoClip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */